package pe.com.lycsoftware.cibertecproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.util.Constants;


public class UserFragment extends Fragment {

    private static final String ARG_USER = "user";
    private static final String TAG = "UserFragment";
    private TextView txt_email, txt_fullname;
    private ImageView img_photo;
    private User user;
    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(User _user) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, _user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_user, container, false);
        txt_email = ret.findViewById(R.id.txtEmail);
        txt_fullname = ret.findViewById(R.id.txtFullname);
        img_photo = ret.findViewById(R.id.imgUser);

        txt_fullname.setText(user.getDisplayName());
        txt_email.setText(user.getEmail());

        Glide.with(this)
                .load(user.getUrlImage())
                .into(img_photo);
        /*try {
            Log.d(TAG, "contenedor de imagen w: " + img_photo.getMaxWidth() + " - h: " + img_photo.getMaxHeight());
            showOptimalImage(Constants.hardcode_userphoto_url);

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return ret;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Intent intent = new Intent(getActivity(), UserEditActivity.class);
            intent.putExtra(Constants.USER_PARAM, user);
            startActivityForResult(intent, Constants.USER_REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onUserFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onUserFragmentInteraction(Uri uri);
    }
}
