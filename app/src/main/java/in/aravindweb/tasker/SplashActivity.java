package in.aravindweb.tasker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
//
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // make req to /api/auth/

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token","-");
        AndroidNetworking.get("https://tasker.aravindweb.in/api/auth/")
                .addHeaders("X-Auth-Token",token)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        launchMainActivity();
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("res",String.valueOf(anError.getErrorCode()));
                        Log.d("res.string",anError.getErrorBody());
                        launchLoginActivity();
                    }
                });



    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }




}