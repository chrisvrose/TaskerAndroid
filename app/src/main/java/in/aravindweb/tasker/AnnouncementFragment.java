package in.aravindweb.tasker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a list of Items.
 */
public class AnnouncementFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private String courseName = "", courseId = "";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnnouncementFragment() {

    }

    public AnnouncementFragment(String courseName, String courseId) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AnnouncementFragment newInstance(int columnCount) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewparent = inflater.inflate(R.layout.fragment_announcement/*fragment_item_list*/, container, false);
        Context context = viewparent.getContext();
        View view = viewparent.findViewById(R.id.list);
        // Set the adapter


        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyAnnouncementRecyclerViewAdapter(
                //laceholderContent.ITEMS
                getContext(), courseId
        ));
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "-");
        boolean isTeacher = sharedPref.getBoolean("isTeacher",false);

        Button b = viewparent.findViewById(R.id.button2);
        TextView tv = viewparent.findViewById(R.id.editTextTextMultiLine);
        if(isTeacher) {
//            view.findViewById()





            b.setOnClickListener(v -> {
                // post request and then reset

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content", tv.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AndroidNetworking.post("https://tasker.aravindweb.in/api/rooms/" + courseId + "/announcements").addHeaders("X-Auth-Token", token)
                        .addJSONObjectBody(jsonObject)
                        .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        recyclerView.setAdapter(new MyAnnouncementRecyclerViewAdapter(
                                //laceholderContent.ITEMS
                                getContext(), courseId
                        ));

                        tv.setText("");
                    }

                    @Override
                    public void onError(ANError anError) {
                        recyclerView.setAdapter(new MyAnnouncementRecyclerViewAdapter(
                                //laceholderContent.ITEMS
                                getContext(), courseId
                        ));
                        tv.setText("");

                        Toast.makeText(context, anError.getErrorCode() + " Error", Toast.LENGTH_SHORT).show();
                    }
                });

            });
        }else{
            b.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
        }
        return viewparent;
    }
}