package pe.com.lycsoftware.cibertecproject;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pe.com.lycsoftware.cibertecproject.model.User;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    TaskListFragment.OnTaskListFragmentInteractionListener,
                    UserFragment.OnFragmentInteractionListener {

    private static final String TAG = "MenuActivity";
    private TextView txt_navfullname, txt_navemail;
    private ImageView img_navphoto;
    private final User user = new User();
    private int menuSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuSelected = menuSelected != 1 ? menuSelected : R.id.nav_task;

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
        user.setDisplayName("Jorge Moises Cueva Samames");
        user.setEmail("jmoics@gmail.com");
        user.setId(1);

        txt_navemail.setText(user.getEmail());
        txt_navfullname.setText(user.getDisplayName());

        executeFragment(menuSelected);
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
        executeFragment(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void executeFragment(int selected) {
        Fragment fragment = null;
        switch (selected) {
            case R.id.nav_task:
                fragment = TaskListFragment.newInstance();
                menuSelected = R.id.nav_task;
                break;
            case R.id.nav_profile:
                fragment = UserFragment.newInstance(user);
                //menuSelected = R.id.nav_cattles;
                break;
            default:
                fragment = TaskListFragment.newInstance();
                menuSelected = R.id.nav_task;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onUserFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTaskListFragmentInteraction(Uri uri) {

    }
}
