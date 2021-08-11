package in.aravindweb.tasker.ui.course;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
//import com.example.cengonline.adt.SortedList;
//import com.example.cengonline.model.Course;
//import com.example.cengonline.model.CourseAnnouncements;
//import com.example.cengonline.model.CourseAssignments;
//import com.example.cengonline.model.CoursePosts;
//import com.example.cengonline.model.User;
//import com.example.cengonline.post.AbstractPost;
//import com.example.cengonline.post.Announcement;
//import com.example.cengonline.post.Assignment;
//import com.example.cengonline.post.Post;
//import in.aravindweb.tasker.ui.dialog.EditClassDialog;
//import in.aravindweb.tasker.ui.dialog.NewAnnouncementDialog;
//import in.aravindweb.tasker.ui.dialog.NewAssignmentDialog;
//import in.aravindweb.tasker.ui.dialog.NewPostDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class CourseFragment extends AppCompatActivity implements View.OnClickListener,  BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course);

    }
    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return false;
    }

}
