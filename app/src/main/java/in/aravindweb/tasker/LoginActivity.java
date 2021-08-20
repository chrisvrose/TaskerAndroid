package in.aravindweb.tasker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Logging in
 */
public class LoginActivity extends AppCompatActivity {
    Button loginbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbutton = findViewById(R.id.loginButton);
        loginbutton.setOnClickListener(v->{
            signWithCredentials();
        });
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void register(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void signWithCredentials(){

        String email = ((EditText)findViewById(R.id.loginEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.loginPassword)).getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // TODO do our login here
        // launchMainActivity();
        AndroidNetworking.post("https://tasker.aravindweb.in/api/auth/login")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");
                            Log.d("res.login.done",token);

                            AndroidNetworking.get("https://tasker.aravindweb.in/api/auth/")
                                    .addHeaders("X-Auth-Token",token)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject j) {

                                            SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(getString(R.string.tokenLocation),Context.MODE_PRIVATE);

                                            try {
                                                SharedPreferences.Editor editor = sharedPref.edit();

                                                editor.putString("token",token);

                                                editor.putBoolean("isTeacher",j.getBoolean("isTeacher"));
                                                Log.d("isTeacher",String.valueOf( j.getBoolean("isTeacher")));
                                                editor.apply();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            launchMainActivity();
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            Log.d("res.login.parse.error",response.toString());
                                            forError();

                                            anError.printStackTrace();
                                        }
                                    });




                        } catch (JSONException e) {
                            Log.d("res.login.parse.error",response.toString());
                            forError();

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("res.login.error",""+anError.getErrorCode());
                        forError();
                    }
                });

    }

    /**
     * Show error message
     */
    void forError(){
        Toast.makeText(this, "Authentication Error", Toast.LENGTH_SHORT).show();
    }

}