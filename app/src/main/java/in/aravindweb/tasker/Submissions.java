package in.aravindweb.tasker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import in.aravindweb.tasker.data.SubmissionData;

public class Submissions extends AppCompatActivity {
    String roomId;
    String resId;
    String authToken;
    RecyclerView rv;
    boolean isTeacher;
    SubmissionRecyclerViewAdapter rva;
    FloatingActionButton fb;
    private Uri filePickedUriuri = null;

    private static int PICKFILE_RESULT_CODE = 25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent x = getIntent();
        roomId = x.getStringExtra("roomId");
        resId = x.getStringExtra("resId");
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        authToken = sharedPref.getString("token","-");
        isTeacher = sharedPref.getBoolean("isTeacher",false);
        fb = findViewById(R.id.floatingActionButton2);

        if(isTeacher) {
            // not teacjher
            fb.setVisibility(View.GONE);
        }else{
            fb.setOnClickListener(v-> {
                AlertDialog.Builder builder = new AlertDialog.Builder(Submissions.this);
                builder.setTitle("Upload Material");
                Button fb = new Button(Submissions.this);
                fb.setText("Pick file");
//                registerForActivityResult()
                ;
                fb.setOnClickListener(lv -> {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                });

                final EditText input = new EditText(Submissions.this);
                input.setHint("Enter Description");
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                LinearLayout dialogLayout = new LinearLayout(Submissions.this);
                dialogLayout.addView(fb);
                dialogLayout.addView(input);

                builder.setView(dialogLayout);
                builder.setPositiveButton("UPLOAD", (dv, which) -> {
                    if (filePickedUriuri == null) {
                        Toast.makeText(Submissions.this, "Please pick a file", Toast.LENGTH_SHORT).show();
                    } else {
                        // this is the worst, create a temp file, copy the IS into that file then upload it
                        try {
//                            String path = FileUtils.getPath(getContext(),filePickedUriuri);
//                            File file = new File(filePickedUriuri.toString());//FileUtils.getFile(getContext(), filePickedUriuri);
                            InputStream s = Submissions.this.getContentResolver().openInputStream(filePickedUriuri);
                            File tempFile = File.createTempFile("olkliksdfhglosdiertlkushdfgsj", "jav");
                            OutputStream ss = new FileOutputStream(tempFile, false);

                            int n;
                            byte[] buffer = new byte[1024];
                            while ((n = s.read(buffer)) > -1) {
                                ss.write(buffer, 0, n);   // Don't allow any extra bytes to creep in, final write
                            }
                            ss.close();
                            AndroidNetworking
                                    .upload("https://tasker.aravindweb.in/api/upload/"+roomId)
                                    .addMultipartFile("file", tempFile)

                                    .addHeaders("description", input.getText().toString())
                                    .addHeaders("resource_id",resId)
                                    .addHeaders("X-Auth-Token", authToken)
                                    .build().getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(Submissions.this, "Done", Toast.LENGTH_SHORT).show();
                                    tempFile.deleteOnExit();
                                    rva.makeData();
                                }

                                @Override
                                public void onError(ANError anError) {
                                    anError.printStackTrace();
                                    Toast.makeText(Submissions.this, "Error" + anError.getErrorCode(), Toast.LENGTH_SHORT).show();
                                    tempFile.deleteOnExit();
                                    rva.makeData();

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//            AndroidNetworking.upload("").addMultipartFile()
//                        Log.d("upload.Picked", data.getData().toString());
                    }
                });

                builder.setNegativeButton("Cancel", (dv, which) -> {

                });

                builder.show();
            });
        }

        rv = findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rva=new SubmissionRecyclerViewAdapter(this,roomId,resId);

        rv.setAdapter(rva);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE) {
            filePickedUriuri = data.getData();

        }
    }
}