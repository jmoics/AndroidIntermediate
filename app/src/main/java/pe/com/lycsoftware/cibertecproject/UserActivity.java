package pe.com.lycsoftware.cibertecproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.util.Constants;

public class UserActivity extends AppCompatActivity {

    private static final String ARG_USER = "user";
    private static final String TAG = "UserFragment";
    private TextView txt_email, txt_fullname;
    private ImageView img_photo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setToolbarProperties();

        user = getIntent().getParcelableExtra(Constants.USER_PARAM);

        if(savedInstanceState != null) {
            user = savedInstanceState.getParcelable(Constants.USER_PARAM);
        }

        txt_email = findViewById(R.id.txt_email);
        txt_fullname = findViewById(R.id.txt_fullname);
        img_photo = findViewById(R.id.img_photo);

        txt_fullname.setText(user.getDisplayName());
        txt_email.setText(user.getEmail());

        Glide.with(this)
                .load(Constants.hardcode_userphoto_url)
                .into(img_photo);
    }

    private void setToolbarProperties() {
        Toolbar main_toolbar = findViewById(R.id.toolbarUser);
        setSupportActionBar(main_toolbar);
        // Enable the UP button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Not show the title
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        /*main_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callReturnActivity();
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.USER_PARAM, user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        switch (requestCode) {
            case Constants.USEREDIT_REQUEST_CODE:
                if (requestCode == Activity.RESULT_OK) {
                    user = data.getParcelableExtra(Constants.USER_PARAM);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showOptimalImage(String url)
            throws IOException {

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... urls) {
                String url = urls[0];

                Bitmap bitmap = null;

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                try {
                    InputStream is = new URL(url).openConnection().getInputStream();
                    bitmap = BitmapFactory.decodeStream(is, null, bmOptions);
                    is.close();

                    Log.d(TAG, "imagen final w: " + bitmap.getWidth() + " - h: " + bitmap.getHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return bitmap;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    //bitmap = rotarBitmapSiSeRequiere(bitmap);
                    img_photo.setImageBitmap(bitmap);
                }
            }

        }.execute(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, UserEditActivity.class);
                intent.putExtra(Constants.USER_PARAM, user);
                startActivityForResult(intent, Constants.USEREDIT_REQUEST_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed: ");
        callReturnActivity();
    }

    private void callReturnActivity() {
        Log.i(TAG, "callReturnActivity: ");
        Intent intent = new Intent();
        intent.putExtra(Constants.USER_PARAM, user);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
