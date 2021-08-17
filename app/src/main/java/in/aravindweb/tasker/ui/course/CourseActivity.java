package in.aravindweb.tasker.ui.course;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//import com.example.cengonline.DatabaseCallback;
//import com.example.cengonline.DatabaseUtility;
import in.aravindweb.tasker.AnnouncementFragment;
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
import com.androidnetworking.AndroidNetworking;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CourseActivity extends AppCompatActivity implements View.OnClickListener,  BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView b;
    TextView courseName;
    Fragment f1 ;
    final FragmentManager fm = getSupportFragmentManager();
    Fragment fa ;// active fragment
    String courseNameStr;String courseIdStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        courseNameStr = extras.getString("courseName");
        courseIdStr = extras.getString("courseId");

        f1 = new AnnouncementFragment(courseNameStr,courseIdStr);
        fa=f1;

//        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_course);
        b = findViewById(R.id.bottom_navigation);
        b.setOnNavigationItemSelectedListener(this);
        courseName = findViewById(R.id.course_fragment_course_name);
        courseName.setText(courseNameStr);
        fm.beginTransaction().add(R.id.fragment_container,fa,"announcement").commit();
        // set to announcementPage

    }
    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("In course fragment", String.valueOf(item.getItemId()));
        switch(item.getItemId()) {
            case R.id.navigation_stream :
                //show the fragment and hide the fragment currently on top
                fm.beginTransaction().hide(fa).commit();
                return true;
                case R.id.navigation_announcements :
                //toolbar.setTitle(getResources().getString(R.string.title_category));
                //loadFragment(new homeFragment());
                    fm.beginTransaction().hide(fa).show(f1).commit();fa=f1;
                return true;
            case R.id.navigation_assignments :
                //toolbar.setTitle(getResources().getString(R.string.title_notifications));
                //loadFragment(new homeFragment());
                return true;
            case R.id.navigation_materials :
                //toolbar.setTitle(getResources().getString(R.string.title_profile));
                //loadFragment(new homeFragment());
                return true;
        }

        return false;
    }

}
