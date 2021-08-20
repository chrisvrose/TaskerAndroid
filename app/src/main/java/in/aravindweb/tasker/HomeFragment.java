package in.aravindweb.tasker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import in.aravindweb.util.misc;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    ArrayList<Integer> l;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        final LinearLayout linearScrollLayout = (LinearLayout) v.findViewById(R.id.scroll_view_linear_layout);


        SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "-");
        boolean isTeacher = sharedPref.getBoolean("isTeacher",false);
        FloatingActionButton fb =v.findViewById(R.id.floatingActionButton);

        if(isTeacher){
            // create classroom
            fb.setOnClickListener(myview->{
                // something has to pop up for email
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Create class");

                // Set up the input
                final EditText input = new EditText(getContext());
                input.setHint("Enter Class Name");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        // invite_id
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("className", m_Text);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AndroidNetworking.post("https://tasker.aravindweb.in/api/rooms").addHeaders("X-Auth-Token", token)
                                .addJSONObjectBody(jsonObject)
                                .build().getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                linearScrollLayout.removeAllViews();
                                getAllCourses(token,linearScrollLayout);
                                Toast.makeText(getContext(), "Invite code worked!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(ANError anError) {

                                Toast.makeText(getContext(), anError.getErrorCode() + " Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            });
        }else{
            fb.setOnClickListener(myview->{
//                something has to pop up for email
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Join class");

// Set up the input
                final EditText input = new EditText(getContext());
                input.setHint("Enter Invite Code");
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        // invite_id
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("invite_id", m_Text);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AndroidNetworking.post("https://tasker.aravindweb.in/api/rooms/students/join").addHeaders("X-Auth-Token", token)
                                .addJSONObjectBody(jsonObject)
                                .build().getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                linearScrollLayout.removeAllViews();
                                getAllCourses(token,linearScrollLayout);
                                Toast.makeText(getContext(), "Invite code worked!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(ANError anError) {

                                Toast.makeText(getContext(), anError.getErrorCode() + " Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            });
        }


        // image list mapping
        Integer[] list = new Integer[]{R.drawable.img_code, R.drawable.img_backtoschool, R.drawable.img_bookclub, R.drawable.img_breakfast,
                R.drawable.img_graduation, R.drawable.img_honors, R.drawable.img_reachout};
        l = new ArrayList<Integer>();
//        int
        l.addAll(Arrays.asList(list));

        // start drawing all the tables
        getAllCourses(token,linearScrollLayout);

        // /api/rooms
        return v;
    }

    private void getAllCourses(String token,LinearLayout linearLayout){
        AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms").addHeaders("X-Auth-Token", token).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                int l = response.length();
                for (int i = 0; i < l; i++) {
                    try {
                        JSONObject j = response.getJSONObject(i);
                        Log.d("res.someClass", j.toString());
                        String className = j.getString("className");
                        String classId = j.getString("_id");
                        drawCourse(linearLayout, className,classId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    private void drawCourse(LinearLayout scroll, String className,String classId) {


        ImageView imageView = new ImageView(getActivity());
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageViewLayoutParams);

        imageView.setImageResource(l.get((int) (Math.random() * l.size())));

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearLayoutLayoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView courseName = new TextView(getActivity());
        LinearLayout.LayoutParams courseNameLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        courseNameLayoutParams.leftMargin = misc.DPtoPX(20, getActivity());
        courseNameLayoutParams.topMargin = misc.DPtoPX(15, getActivity());
        courseName.setLayoutParams(courseNameLayoutParams);
        courseName.setLines(1);
        courseName.setEllipsize(TextUtils.TruncateAt.END);
        courseName.setTextAppearance(getActivity(), R.style.fontForCourseNameOnCard);
        courseName.setText(className);

        TextView courseSection = new TextView(getActivity());
        LinearLayout.LayoutParams courseSectionLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        courseSectionLayoutParams.leftMargin = misc.DPtoPX(20, getActivity());
        courseSection.setLayoutParams(courseSectionLayoutParams);
        courseSection.setTextAppearance(getActivity(), R.style.fontForCourseSectionOnCard);
        courseSection.setLines(1);
        courseSection.setEllipsize(TextUtils.TruncateAt.END);
        courseSection.setText("");

        TextView courseTeacher = new TextView(getActivity());
        LinearLayout.LayoutParams courseTeacherLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        courseTeacherLayoutParams.leftMargin = misc.DPtoPX(20, getActivity());
        courseTeacherLayoutParams.bottomMargin = misc.DPtoPX(15, getActivity());
        courseTeacherLayoutParams.topMargin = misc.DPtoPX(60, getActivity());
        courseTeacher.setLayoutParams(courseTeacherLayoutParams);
        courseTeacher.setLines(1);
        courseTeacher.setEllipsize(TextUtils.TruncateAt.END);
        courseTeacher.setTextAppearance(getActivity(), R.style.fontForCourseTeacherOnCard);
        courseTeacher.setText("");


        linearLayout.addView(courseName, courseNameLayoutParams);
        linearLayout.addView(courseSection, courseSectionLayoutParams);
        linearLayout.addView(courseTeacher, courseTeacherLayoutParams);

        CardView cardView = new CardView(getActivity());
        LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.topMargin = misc.DPtoPX(7, getActivity());
        cardViewLayoutParams.leftMargin = misc.DPtoPX(13, getActivity());
        cardViewLayoutParams.rightMargin = misc.DPtoPX(13, getActivity());
        cardView.setLayoutParams(cardViewLayoutParams);
        cardView.setClickable(true);

        cardView.setRadius(misc.DPtoPX(8, getActivity()));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("courseName", className);
                intent.putExtra("courseId",classId);

                startActivity(intent);
            }
        });


        cardView.addView(imageView, imageViewLayoutParams);
        cardView.addView(linearLayout, linearLayoutLayoutParams);

        scroll.addView(cardView, cardViewLayoutParams);

    }
}