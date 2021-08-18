package in.aravindweb.tasker;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
//import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//import com.example.cengonline.DatabaseCallback;
//import com.example.cengonline.DatabaseUtility;
import in.aravindweb.tasker.AnnouncementFragment;
import in.aravindweb.tasker.AssignmentFragment;
import in.aravindweb.tasker.MaterialFragment;
import in.aravindweb.tasker.R;
import in.aravindweb.tasker.StudentFragment;

//import in.aravindweb.tasker.ui.dialog.EditClassDialog;
//import in.aravindweb.tasker.ui.dialog.NewAnnouncementDialog;
//import in.aravindweb.tasker.ui.dialog.NewAssignmentDialog;
//import in.aravindweb.tasker.ui.dialog.NewPostDialog;
import com.androidnetworking.AndroidNetworking;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CourseActivity extends AppCompatActivity implements View.OnClickListener,  BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView b;
    Toolbar courseName;
    Fragment f1 ;
    Fragment f2;
    Fragment f3;
    Fragment f4;
    final FragmentManager fm = getSupportFragmentManager();
    Fragment fa ;// active fragment
    String courseNameStr;String courseIdStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        courseNameStr = extras.getString("courseName");
        courseIdStr = extras.getString("courseId");
        Log.d("pain","it hurts");
        f1 = new StudentFragment(courseNameStr,courseIdStr);
        f2 = new AnnouncementFragment(courseNameStr,courseIdStr);
        f3 = new AssignmentFragment(courseNameStr,courseIdStr);
        f4 = new MaterialFragment(courseNameStr,courseIdStr);
        fa=f1;

//        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_course);
        b = findViewById(R.id.bottom_navigation);
        b.setOnNavigationItemSelectedListener(this);
        courseName = findViewById(R.id.toolbar3/*course_fragment_course_name*/);
        courseName.setTitle(courseNameStr);



        fm.beginTransaction()
                .add(R.id.fragment_container,f1,"students")
                .add(R.id.fragment_container,f2,"announcement")
                .add(R.id.fragment_container,f3,"assignment")
                .add(R.id.fragment_container,f4,"material")
                .hide(f2).hide(f3).hide(f4)
                .commit();
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
                fm.beginTransaction().hide(fa).show(f1).commit();fa=f1;
                return true;
                case R.id.navigation_announcements :
                //toolbar.setTitle(getResources().getString(R.string.title_category));
                //loadFragment(new homeFragment());
                    fm.beginTransaction().hide(fa).show(f2).commit();fa=f2;
                return true;
            case R.id.navigation_assignments :
                //toolbar.setTitle(getResources().getString(R.string.title_notifications));
                fm.beginTransaction().hide(fa).show(f3).commit();fa=f3;
                //loadFragment(new homeFragment());
                return true;
            case R.id.navigation_materials :
                fm.beginTransaction().hide(fa).show(f4).commit();fa=f4;
                //toolbar.setTitle(getResources().getString(R.string.title_profile));
                //loadFragment(new homeFragment());
                return true;
        }

        return false;
    }

}
