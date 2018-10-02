package pe.com.lycsoftware.cibertecproject;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.util.Constants;

public class UserImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image);

        setToolbarProperties();

        User user = getIntent().getParcelableExtra(Constants.USER_PARAM);

        ImageView imgNoticiaScale = findViewById(R.id.imgNoticiaScale);

        Glide.with(this)
                .load(user.getUrlImage())
                .into(imgNoticiaScale);
    }

    private void setToolbarProperties() {
        Toolbar main_toolbar = findViewById(R.id.toolbarUserImage);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }
}
