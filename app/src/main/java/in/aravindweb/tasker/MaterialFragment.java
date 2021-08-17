package in.aravindweb.tasker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.aravindweb.tasker.data.MaterialData;

/**
 * A fragment representing a list of Items.
 */
public class MaterialFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String courseName = "", courseId = "";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MaterialFragment() {
    }

    public MaterialFragment(String cn,String ci){
        courseId=ci;
        courseName=cn;
    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MaterialFragment newInstance(int columnCount) {
        MaterialFragment fragment = new MaterialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewparent = inflater.inflate(R.layout.fragment_material_listing/*fragment_item_list*/, container, false);
        Context context = viewparent.getContext();
        View view = viewparent.findViewById(R.id.list);
        // Set the adapter


        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MaterialRecyclerViewAdapter(
                //laceholderContent.ITEMS
                getContext(), courseId
        ));
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "-");
        boolean isTeacher = sharedPref.getBoolean("isTeacher",false);

        Button b = viewparent.findViewById(R.id.button2);
        TextView tv = viewparent.findViewById(R.id.editTextTextMultiLine);
        if(isTeacher) {
            // teacher
            tv.setHint("Student Email");

            b.setOnClickListener(v -> {
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray x = new JSONArray();
                    x.put(tv.getText().toString());
                    jsonObject.put("studentEmails", x);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // teacher wanting to add student
                AndroidNetworking.post("https://tasker.aravindweb.in/api/rooms/" + courseId + "/students/invite").addHeaders("X-Auth-Token", token)
                        .addJSONObjectBody(jsonObject)
                        .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        recyclerView.setAdapter(new StudentItemRecyclerViewAdapter(
                                getContext(), courseId
                        ));

                        tv.setText("");
                        Toast.makeText(context, "Invite Code Sent!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError anError) {
                        recyclerView.setAdapter(new StudentItemRecyclerViewAdapter(
                                getContext(), courseId
                        ));
                        tv.setText("");

                        Toast.makeText(context, anError.getErrorCode() + " Error", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }else{
            // student
            b.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);

        }
        return viewparent;
    }
}