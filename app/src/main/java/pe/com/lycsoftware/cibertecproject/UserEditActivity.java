package pe.com.lycsoftware.cibertecproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.Networking;

public class UserEditActivity extends AppCompatActivity {

    private static final String TAG = "UserEditActivity";
    private EditText edtEmail, edtName, edtUrlImage;
    private ImageView imgUserPicture;
    private ProgressBar progressBarSave;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtUrlImage = findViewById(R.id.edtUrlImage);
        imgUserPicture = findViewById(R.id.imgUserPicture);
        progressBarSave = findViewById(R.id.progressBarSave);

        setToolbarProperties();

        user = getIntent().getParcelableExtra(Constants.USER_PARAM);

        setData();
    }

    private void setToolbarProperties() {
        Toolbar main_toolbar = findViewById(R.id.toolbarUserEdit);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void setData() {
        edtEmail.setText(user.getEmail());
        edtName.setText(user.getDisplayName());
        edtUrlImage.setText(user.getUrlImage());

        Glide.with(this)
                .load(user.getUrlImage())
                .into(imgUserPicture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                update();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void update() {
        user.setDisplayName(edtName.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setUrlImage(edtUrlImage.getText().toString());

        progressBarSave.setVisibility(View.VISIBLE);
        Networking.updateUser(user, new Networking.NetworkingCallback<User>() {
            @Override
            public void onResponse(User response) {
                Intent intent = new Intent();
                intent.putExtra(Constants.USER_PARAM, response);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                progressBarSave.setVisibility(View.GONE);
                Toast.makeText(UserEditActivity.this,
                        "Nose pudo actualizar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
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
