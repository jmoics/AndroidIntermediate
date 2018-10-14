package pe.com.lycsoftware.cibertecproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private static final String TAG = "UserActivity";
    private TextView txtEmail, txtFullname;
    private ImageView imgUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_user);
        setToolbarProperties();

        user = getIntent().getParcelableExtra(Constants.USER_PARAM);

        if(savedInstanceState != null) {
            user = savedInstanceState.getParcelable(Constants.USER_PARAM);
        }

        txtEmail = findViewById(R.id.txtEmail);
        txtFullname = findViewById(R.id.txtFullname);
        imgUser = findViewById(R.id.imgUser);

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserImageActivity.class);
                intent.putExtra(Constants.USER_PARAM, user);
                startActivity(intent);
            }
        });
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
        txtFullname.setText(user.getDisplayName());
        txtEmail.setText(user.getEmail());

        Glide.with(this)
                .load(user.getUrlImage())
                .into(imgUser);
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
            case Constants.REQUEST_CODE_USER_EDIT:
                if (resultCode == Activity.RESULT_OK) {
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
                    imgUser.setImageBitmap(bitmap);
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
                startActivityForResult(intent, Constants.REQUEST_CODE_USER_EDIT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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

    @Override
    protected void onPause()
    {
        super.onPause();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
