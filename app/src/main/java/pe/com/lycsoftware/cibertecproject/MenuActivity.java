package pe.com.lycsoftware.cibertecproject;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.Networking;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    TaskListFragment.OnTaskListFragmentInteractionListener,
                    UserFragment.OnFragmentInteractionListener {

    private static final String TAG = "MenuActivity";
    private TextView txt_navfullname, txt_navemail;
    private ImageView img_navphoto;
    private User user;
    private int menuSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //menuSelected = menuSelected != 1 ? menuSelected : R.id.navTask;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initNavControllers(navigationView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        /*user.setDisplayName("Jorge Moises Cueva Samames");
        user.setEmail("jmoics@gmail.com");
        user.setObjectId(1);*/

        loadUser();
        if (user != null) {
            txt_navemail.setText(user.getEmail());
            txt_navfullname.setText(user.getDisplayName());

            //executeFragment(menuSelected);
        } else {
            Toast.makeText(this,
                    "Existe un error con la conexión al servidor 1", Toast.LENGTH_LONG).show();
        }
    }

    private void loadUser() {
        Networking.getUser4Email("jmoics@gmail.com", new Networking.NetworkingCallback<List<User>>() {
            @Override
            public void onResponse(List<User> response) {
                user = !response.isEmpty() ? response.get(0) : null;
                Log.d(TAG, "onResponse: User correctly loaded Name = " + user.getDisplayName());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "onError: Error in user loading", throwable);
            }
        });
    }

    private void initNavControllers(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        txt_navfullname = headerView.findViewById(R.id.txt_navfullname);
        txt_navemail = headerView.findViewById(R.id.txt_navemail);
        img_navphoto = headerView.findViewById(R.id.img_navphoto);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        execute(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void execute(int selected) {
        Fragment fragment = null;
        switch (selected) {
            case R.id.navTask:
                fragment = TaskListFragment.newInstance();
                menuSelected = R.id.navTask;
                break;
            case R.id.navProfile:
                menuSelected = R.id.navProfile;
                if (user != null) {
                    Intent intent = new Intent(this, UserActivity.class);
                    intent.putExtra(Constants.USER_PARAM, user);
                    startActivityForResult(intent, Constants.USER_REQUEST_CODE);
                } else {
                    Toast.makeText(this,
                            "Existe un error con la conexión al servidor 2", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                fragment = TaskListFragment.newInstance();
                menuSelected = R.id.navTask;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        switch (requestCode) {
            case Constants.USER_REQUEST_CODE:
                user = data.getParcelableExtra(Constants.USER_PARAM);
                break;
        }
    }

    @Override
    public void onUserFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTaskListFragmentInteraction(Uri uri) {

    }
}
