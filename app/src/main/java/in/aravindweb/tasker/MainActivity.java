package in.aravindweb.tasker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logout(View v){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.tokenLocation),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("token");
        editor.commit();
        Log.d("main.logout.committed","done");


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}