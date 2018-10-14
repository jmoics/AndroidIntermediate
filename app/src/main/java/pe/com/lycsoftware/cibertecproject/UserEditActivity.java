package pe.com.lycsoftware.cibertecproject;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.Networking;

public class UserEditActivity extends AppCompatActivity {

    private static final String TAG = "UserEditActivity";
    private EditText edtEmail, edtName, edtUrlImage;
    private ImageView imgUserPicture;
    private ProgressBar progressBarSave;
    private String imagePath;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_user_edit);

        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtUrlImage = findViewById(R.id.edtUrlImage);
        imgUserPicture = findViewById(R.id.imgUserPicture);
        progressBarSave = findViewById(R.id.progressBarSave);
        ImageButton btnCamera = findViewById(R.id.btnCamera);
        ImageButton btnGallery = findViewById(R.id.btnGallery);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIfPermission();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        edtUrlImage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus)
            {
                if (!hasFocus) {
                    Glide.with(UserEditActivity.this)
                         .load(edtUrlImage.getText().toString())
                         .into(imgUserPicture);
                }
            }
        });

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

    private void openCameraIfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Permisos de Camara")
                        .setMessage("Esta funcionalidad requiere permisos para acceder")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getCameraPermission();
                            }
                        }).setNegativeButton("Cancelar", null);
                builder.show();
            } else {
                getCameraPermission();
            }
        } else {
            openCamera();
        }
    }

    private void getCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, 10);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File imagen = null;
            try {
                imagen = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imagen != null) {
                // Desde Android 7 se necesita un FileProvider
                Uri imagenUri = FileProvider.getUriForFile(this,
                        Constants.CHANNEL_ID, imagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
                startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.REQUEST_CODE_GALLERY);
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timestamp + "_";
        // DIRECTORY_PICTURES es el default que se crea para galeria
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File fileImage = File.createTempFile(fileName, ".jpeg", path);
        imagePath = fileImage.getAbsolutePath();
        return fileImage;
    }

    private void saveImageFile(Uri uri)
    {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(getContentResolver().openInputStream(uri));
            bos = new BufferedOutputStream(new FileOutputStream(createImageFile(), false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    //mostrarImagenOptima(imageView, rutaImagen);
                    edtUrlImage.setText(imagePath);
                    Glide.with(this)
                         .load(imagePath)
                         .into(imgUserPicture);
                }
                break;
            case Constants.REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    saveImageFile(uri);
                    edtUrlImage.setText(imagePath);
                    Glide.with(this)
                         .load(uri)
                         .into(imgUserPicture);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "No se encontró permisos para la cámara", Toast.LENGTH_SHORT).show();
                }
        }
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
