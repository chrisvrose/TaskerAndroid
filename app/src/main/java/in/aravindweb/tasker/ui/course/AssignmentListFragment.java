package in.aravindweb.tasker.ui.course;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

//import com.example.cengonline.DatabaseCallback;
//import com.example.cengonline.DatabaseUtility;
import in.aravindweb.tasker.R;
//import com.example.cengonline.Utility;
//import com.example.cengonline.model.Course;
//import com.example.cengonline.model.MyTimestamp;
//import com.example.cengonline.model.User;
//import com.example.cengonline.post.Assignment;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.ListResult;
//import com.google.firebase.storage.StorageMetadata;
//import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class AssignmentListFragment extends AppCompatActivity implements View.OnClickListener {

    private static final int REFRESH_ITEM = 1000;
    private static final int DOWNLOAD_ALL = 1001;

    private Toolbar toolbar;
//    private User user;
//    private Course course;
//    private Assignment assignment;
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;
    private List<FileEntity> files;
    private int dismissLimit;
    private int dismissCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_assignment_list);

        this.toolbar = findViewById(R.id.assignment_list_toolbar);
        this.linearLayout = findViewById(R.id.assignment_list_linear_layout);
        this.progressDialog = new ProgressDialog(this);
        this.files = new ArrayList<FileEntity>();

        this.toolbar.setTitle("Submissions");

        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        if(getIntent() != null && getIntent().getSerializableExtra("user") != null && getIntent().getSerializableExtra("assignment") != null && getIntent().getSerializableExtra("course") != null){
//
//            this.user = (User)getIntent().getSerializableExtra("user");
//            this.course = (Course)getIntent().getSerializableExtra("course");
//            this.assignment = (Assignment) getIntent().getSerializableExtra("assignment");
//
//
//            progressDialog.setMessage("Fetching files...");
//            progressDialog.show();
//            getFiles();
//
//
//        }
//        else{
//            finish();
//        }
    }

    private void printFileNames(final FileEntity fileEntity){

    }

    private void getFiles(){

    }

    private void deleteFile(final FileEntity file, final CardView cardView){


//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(course.getKey() + "/" + assignment.getPostedAt().hashCode() + "/" + file.getFileName());
//        storageReference.delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        makeToastMessage(file.getFileName() + " has been deleted successfully!");
//                        linearLayout.removeView(cardView);
//                        progressDialog.dismiss();
//                    }
//                });

    }


    private void downloadFile(final FileEntity file){

//        try {
//            StorageReference ref = FirebaseStorage.getInstance().getReference().child(course.getKey() + "/" + assignment.getPostedAt().hashCode() + "/" + file.getFileName());
//            ref.getDownloadUrl()
//                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            downloadManager(file.getFileName(), DIRECTORY_DOWNLOADS, uri.toString());
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            makeToastMessage(e.getMessage());
//                            progressDialog.dismiss();
//                        }
//                    });
//        }
//        catch (Exception ex){
//            makeToastMessage(ex.getMessage());
//        }

    }

    private void downloadManager(String fileName, String destinationDirectory, String url){


//        DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(this, destinationDirectory, url);
//
//
//        downloadManager.enqueue(request);
//        progressDialog.dismiss();
    }


    @Override
    public void onClick(View v) {
        /*NewAnnouncementDialog newAD = new NewAnnouncementDialog(this, this.course);
        newAD.show();*/
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        setMenuItems(menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                onBackPressed();
                break;
            case REFRESH_ITEM:
                recreate();
                break;
            case DOWNLOAD_ALL:
                downloadAllFiles();
            break;
        }
        return true;
    }

    private void downloadAllFiles(){
        for(FileEntity fileEntity : this.files){
            progressDialog.setMessage("Downloading " + fileEntity.getFileName());
            progressDialog.show();
            downloadFile(fileEntity);
        }
    }


    private void setMenuItems(final Menu menu){

        menu.add(0, REFRESH_ITEM, 0, "Refresh");
        menu.add(0, DOWNLOAD_ALL, 1, "Download All");
    }

    private void makeToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private static class FileEntity{

        private String fileName;
        private String creationTime;

        public FileEntity(String fileName, String creationTime) {
            this.fileName = fileName;
            this.creationTime = creationTime;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }
    }
}
