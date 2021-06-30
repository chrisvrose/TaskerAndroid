package in.aravindweb.tasker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import in.aravindweb.tasker.ui.course.CourseFragment;
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
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        final LinearLayout lineaScrollLayout = (LinearLayout)v.findViewById(R.id.scroll_view_linear_layout);
//        ((Button)v.findViewById(R.id.button)).setOnClickListener(x->{
//            Toast.makeText(getContext(), "NO", Toast.LENGTH_SHORT).show();
//        });


        SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token","-");

        Integer [] list = new Integer[]{R.drawable.img_code,R.drawable.img_backtoschool,R.drawable.img_bookclub,R.drawable.img_breakfast,
        R.drawable.img_graduation,R.drawable.img_honors,R.drawable.img_reachout};
        l= new ArrayList<Integer>();
//        int
        l.addAll(Arrays.asList(list));
//        l.add(R.drawable.img_code);l.add(R.drawable.img_backtoschool);l.add(R.drawable.img_bookclub);
//        l.add(R.drawable.img_breakfast);
//        l.addAll(R.drawable.img_code)

        AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms").addHeaders("X-Auth-Token",token).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                int l = response.length();
                for(int i=0;i<l;i++){
                    try {
                        JSONObject j = response.getJSONObject(i);
                        Log.d("res.someClass",j.toString());
                        String className = j.getString("className");
                        drawCourse(lineaScrollLayout,className);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });
        // /api/rooms
        return v;
    }




        private void drawCourse(LinearLayout scroll, String className){



            ImageView imageView = new ImageView(getActivity());
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imageViewLayoutParams);
//            imageView.setImageResource(course.getImageId());

            imageView.setImageResource(l.get((int)(Math.random()*l.size())));
//            Glide.with(getContext()).load("https://tasker.aravindweb.in/images/classroom_dark.jpg").into(imageView);

            LinearLayout linearLayout = new LinearLayout(getActivity());
            LinearLayout.LayoutParams linearLayoutLayoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
            courseTeacher.setText(/*teacher.getDisplayName()*/"");


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
//            cardView.setForeground(getSelectedItemDrawable());
            cardView.setRadius(misc.DPtoPX(8, getActivity()));
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CourseFragment.class);
//                    intent.putExtra("course", course);
                    startActivity(intent);
                }
            });


            cardView.addView(imageView, imageViewLayoutParams);
            cardView.addView(linearLayout, linearLayoutLayoutParams);

            scroll.addView(cardView, cardViewLayoutParams);

        }
    private Drawable getSelectedItemDrawable() {
        int[] attrs = new int[] { R.attr.selectableItemBackground };
        TypedArray ta = getActivity().obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }
}