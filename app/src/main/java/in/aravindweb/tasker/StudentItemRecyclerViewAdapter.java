package in.aravindweb.tasker;

import static android.view.View.GONE;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.aravindweb.tasker.data.AnnouncementData;
import in.aravindweb.tasker.data.StudentData.StudentItem;

import in.aravindweb.tasker.databinding.FragmentStudentBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link StudentItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StudentItemRecyclerViewAdapter extends RecyclerView.Adapter<StudentItemRecyclerViewAdapter.ViewHolder> {

    private final List<StudentItem> mValues;

    String authToken;
    String roomid;
    boolean isTeacher;

//    public StudentItemRecyclerViewAdapter(List<StudentItem> items) {
//        mValues = items;
//    }
    public StudentItemRecyclerViewAdapter(Context c, String roomid){
        SharedPreferences sharedPref = c.getSharedPreferences(c.getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        authToken = sharedPref.getString("token","-");
        isTeacher = sharedPref.getBoolean("isTeacher",false);
        this.roomid=roomid;
        Log.d("create.students","created student list");
        makeData();

        mValues = new ArrayList<>();//items;
    }



    public void setVal(List<StudentItem> vals){mValues.clear();mValues.addAll(vals);}
    public void makeData(){
        Log.d("roomid",roomid+"");
        AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms/"+roomid+"/users").addHeaders("X-Auth-Token",authToken).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("students");
                    int l  = ar.length();
                    LinkedList<StudentItem> x = new LinkedList<>();
                    for(int i=0;i<l;i++){
                        JSONObject j = ar.getJSONObject(i);
                        String id = j.getString("_id");
                        String email = j.getString("email");
                        String name = j.getString("name");
                        StudentItem item = new StudentItem(id,email,name);
                        x.add(item);
                    }
                    setVal(x);
                    StudentItemRecyclerViewAdapter.this.notifyDataSetChanged();
                    Log.d("output","output ready");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.d("get.student.error",anError.getErrorCode()+"");
                anError.printStackTrace();
            }
        });
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentStudentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText(mValues.get(position).email);


        if(isTeacher)
            holder.mDeleteButton.setOnClickListener(v->{
                String url = "https://tasker.aravindweb.in/api/rooms/"+roomid+"/students/"+mValues.get(position).id;
                AndroidNetworking.delete(url)
                        .addHeaders("X-Auth-Token",authToken).build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        makeData();
                    }

                    @Override
                    public void onError(ANError anError) {
                        makeData();

                        Log.d("dataerror",":deleteerrror"+anError.getErrorCode()+":"+url);
                    }
                });

            });
        else holder.mDeleteButton.setVisibility(GONE);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button mDeleteButton;
        public StudentItem mItem;

        public ViewHolder(FragmentStudentBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mDeleteButton = binding.button4;

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}