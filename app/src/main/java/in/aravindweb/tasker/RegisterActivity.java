package in.aravindweb.tasker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Registration activity
 */
public class RegisterActivity extends AppCompatActivity {


    private EditText memberEmail;
    private EditText memberPassword;
    private EditText memberName;
    private EditText memberPhone;
    private Switch memberIsTeacher;
    private Button registerButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.progressDialog = new ProgressDialog(this);

        findViewById(R.id.redirectLoginText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });

        memberEmail = (EditText) findViewById(R.id.registerEmail);
        memberPassword = (EditText) findViewById(R.id.registerPassword);
        memberName = (EditText) findViewById(R.id.registerName);
        memberPhone = (EditText) findViewById(R.id.registerPhone);
        memberIsTeacher = (Switch) findViewById(R.id.registerIsTeacher);
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String email = memberEmail.getText().toString();
                final String password = memberPassword.getText().toString();
                final String name = memberName.getText().toString();
                final String phone = memberPhone.getText().toString();
                final boolean isTeacher = memberIsTeacher.isChecked();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Please enter your full name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);
                    jsonObject.put("name", name);
                    jsonObject.put("phone", phone);
                    jsonObject.put("isTeacher", isTeacher);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AndroidNetworking.post("https://tasker.aravindweb.in/api/users")
                        .addJSONObjectBody(jsonObject)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String token = response.getString("token");
                                    Log.d("res.login.done",token);


                                    SharedPreferences sharedPref = RegisterActivity.this.getSharedPreferences(getString(R.string.tokenLocation), Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("token",token);
                                    editor.putBoolean("isTeacher", isTeacher);
                                    editor.apply();

                                    launchMainActivity();

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
        });
    }

    private void launchLoginActivity(){

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    void forError(){
        Toast.makeText(this, "Registration Error", Toast.LENGTH_SHORT).show();
    }

}
