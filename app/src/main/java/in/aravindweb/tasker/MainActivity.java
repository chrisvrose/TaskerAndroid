package in.aravindweb.tasker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavController navController;
//    private TextView x=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        this.drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,R.id.nav_profile)
                .setDrawerLayout(drawerLayout)
                .build();

        navigationView.setNavigationItemSelectedListener(this);


        //set profile
        // onclick opens the profile page
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.navbarDisplayName)).setOnClickListener(this::openProfilePage);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.navbarEmailImage)).setOnClickListener(this::openProfilePage);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.navbarEmail)).setText("Bopooo");

        updateProfileDetails();
    }

    void openProfilePage(View v){
        Intent i = new Intent(this,ProfileActivity.class);


        i.putExtra("email",((TextView)(navigationView.getHeaderView(0)).findViewById(R.id.navbarEmail)).getText().toString());
        startActivityForResult(i,12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12)
            updateProfileDetails();
    }
    void updateProfileDetails(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.tokenLocation), Context.MODE_PRIVATE);

        String token = sharedPref.getString("token","-");

        AndroidNetworking.get("https://tasker.aravindweb.in/api/auth").addHeaders("X-Auth-Token",token).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject res) {
//                navigationView.getHeaderView(0
                Log.d("res.output",res.toString());
                try {
                    String name = res.getString("name");
                    String email = res.getString("email");
                    View v = (navigationView.getHeaderView(0));
                    ((TextView)v.findViewById(R.id.navbarEmail)).setText(email);
                    ((TextView)v.findViewById(R.id.navbarDisplayName)).setText(name);
                    ((TextView)v.findViewById(R.id.navbarEmailImage)).setText(name.substring(0,1));

                } catch (JSONException e) {
                    Log.d("res.output.output",res.toString());

                    e.printStackTrace();
                }
//                String isTeacher = res.getString("email");

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }
    public void logout(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.tokenLocation),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.commit();
        Log.d("main.logout.committed","done");


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.nav_logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}