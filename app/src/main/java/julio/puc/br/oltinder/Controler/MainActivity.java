package julio.puc.br.oltinder.Controler;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

import julio.puc.br.oltinder.Fragment.ProductsFragment;
import julio.puc.br.oltinder.Fragment.SellerFragment;
import julio.puc.br.oltinder.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , ProductsFragment.OnFragmentInteractionListener{

    public String TAG = "OLTINDER";
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    private int userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(getString(R.string.prev_title), CONTEXT_RESTRICTED);
        userType = sharedPreferences.getInt(getString(R.string.prev_user_type),-1);

        //default
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_main));

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        fab.setVisibility(View.GONE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView txtNameUser = (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        TextView txtEmailUser = (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtUserEmail);

        txtNameUser.setText(sharedPreferences.getString(getString(R.string.prev_user_name),""));
        txtEmailUser.setText(sharedPreferences.getString(getString(R.string.prev_user_email),""));

        if(userType == 2 ){
            SellerFragment fragment = new SellerFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();
            
        }else{
            //chamar Fragmento padrão de usuario
            ProductsFragment fragment = new ProductsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_send) {

        } else if(id == R.id.nav_login){
            //chamar tela de login para vendedor
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
