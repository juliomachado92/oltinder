package julio.puc.br.oltinder.Controler;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

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
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

import julio.puc.br.oltinder.Fragment.MyProduct;
import julio.puc.br.oltinder.Fragment.ProductsFragment;
import julio.puc.br.oltinder.Fragment.SellerStartFragment;
import julio.puc.br.oltinder.Model.Constants;
import julio.puc.br.oltinder.Model.Products;
import julio.puc.br.oltinder.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,
        ProductsFragment.OnFragmentInteractionListener,
        SellerStartFragment.OnFragmentInteractionListener,
        MyProduct.OnFragmentInteractionListener {

    public static String TAG = "OLTINDER";
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    public static List<Products>myProducts;


    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Backendless.initApp(this, Constants.APP_ID,Constants.SECRET_KEY, Constants.APP_VERSION);

        sharedPreferences = getSharedPreferences(getString(R.string.prev_title),CONTEXT_RESTRICTED);
        userType = sharedPreferences.getInt(getString(R.string.prev_user_type),-1);

        //default
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_main));

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

        //ProductsFragment fragment = new ProductsFragment();
//        SellerStartFragment fragment2 = new SellerStartFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment2).commit();

        if(userType == 2 ){
            Log.i(TAG, "TIPO 2 VENDEDOR");
            SellerStartFragment fragment = new SellerStartFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();

        }else{
            //chamar Fragmento padrão de usuario
            Log.i(TAG, "TIPO 1 USUARIO");
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

       if (id == R.id.nav_MyProducts) {
            //MyProduct fragment = new MyProduct();
            //getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        } else if(id == R.id.nav_login){
            //fazer Logout
            Backendless.UserService.logout(new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void aVoid) {
                    Log.i(TAG,"Logout");
                    Intent it  =  new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(it);
                    finish();
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {

                }
            });


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}
