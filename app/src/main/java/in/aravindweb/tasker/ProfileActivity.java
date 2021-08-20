package in.aravindweb.tasker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Profile file
 */
public class ProfileActivity extends AppCompatActivity {
    EditText fname;
    EditText lname;
    TextView email;
    EditText phno;

    Button submitButton;
    Button deleteAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        fname = findViewById(R.id.editTextTextPersonName);
        lname = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.editTextTextEmailAddress);
        phno = findViewById(R.id.editTextPhone);
        // default values



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("email");
            //The key argument here must match that used in the other activity

            email.setText(value);
        }else{
            email.setText("foo");
        }
        email.setEnabled(false);



        submitButton = findViewById(R.id.submitbutton);
        deleteAccount = findViewById(R.id.deleteaccount);

        submitButton.setOnClickListener(v->{
            String fnameText = fname.getText().toString();
            String lnameText = lname.getText().toString();
            String phoneText = phno.getText().toString();

            Toast.makeText(ProfileActivity.this,"no",Toast.LENGTH_LONG).show();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", fnameText+" "+lnameText);
                jsonObject.put("phone", phoneText);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.tokenLocation), Context.MODE_PRIVATE);

            String token = sharedPref.getString("token","-");
            // PATCH /api/users ->
            // {}
            AndroidNetworking.patch("https://tasker.aravindweb.in/api/users")
                    .addHeaders("X-Auth-Token",token)
                    .addJSONObjectBody(jsonObject)
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(ProfileActivity.this,"Successful",Toast.LENGTH_LONG).show();
                    Log.d("user.details",response.toString());
                    ProfileActivity.this.finish();
                }

                @Override
                public void onError(ANError anError) {

                    Toast.makeText(ProfileActivity.this,"Edit Error",Toast.LENGTH_LONG).show();
                    Log.e("user.edit",String.valueOf(anError.getErrorCode()));
                }
            });
        });

    }


}