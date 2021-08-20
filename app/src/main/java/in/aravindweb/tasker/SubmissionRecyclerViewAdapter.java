package in.aravindweb.tasker;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.aravindweb.tasker.data.SubmissionData.SubmissionItem;

import in.aravindweb.tasker.databinding.FragmentSubmissionBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SubmissionItem}.
 *
 */
public class SubmissionRecyclerViewAdapter extends RecyclerView.Adapter<SubmissionRecyclerViewAdapter.ViewHolder> {

    private final List<SubmissionItem> mValues;
    String roomid,resid,authToken;boolean isTeacher;

    public SubmissionRecyclerViewAdapter(Context c, String roomid,String resid){
        SharedPreferences sharedPref = c.getSharedPreferences(c.getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        authToken = sharedPref.getString("token","-");
        isTeacher = sharedPref.getBoolean("isTeacher",false);
        this.roomid=roomid;
        this.resid = resid;
        Log.d("create.students","created student list");
        makeData();

        mValues = new ArrayList<>();//items;
    }

    public void setVal(List<SubmissionItem> vals){mValues.clear();mValues.addAll(vals);}

    public void makeData(){
        Log.d("roomid",roomid+"");
        // /api/rooms/roomid/resid/submissions
        AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms/"+roomid+"/"+resid+"/submissions").addHeaders("X-Auth-Token",authToken).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray ar) {
                try {
                    int l  = ar.length();
                    LinkedList<SubmissionItem> x = new LinkedList<>();
                    for(int i=0;i<l;i++){
                        JSONObject j = ar.getJSONObject(i);
                        String id = j.getString("_id");
                        String createdAt = j.getString("createdAt");
                        String studentId = j.getString("student_id");
                        String resourceId = j.getString("resource_id");
                        String submission = j.getString("submission");
                        String desc = j.getString("description");

                        SubmissionItem item = new SubmissionItem(id,createdAt,studentId,resourceId,submission,desc);
                        x.add(item);
                    }
                    setVal(x);
                    SubmissionRecyclerViewAdapter.this.notifyDataSetChanged();
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

        return new ViewHolder(FragmentSubmissionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).description);
        holder.mContentView.setText(mValues.get(position).createdAt);
        holder.mDownloadButton.setOnClickListener(v->{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mValues.get(position).submission));
            holder.mDownloadButton.getContext().startActivity(browserIntent);
        });
        holder.mDeleteButton.setOnClickListener(v->{
            AndroidNetworking.delete("https://tasker.aravindweb.in/api/upload/"+roomid+"/submissions/"+holder.mItem.id).addHeaders("X-Auth-Token",authToken)
                    .build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(holder.mDeleteButton.getContext(),"Deleted!",Toast.LENGTH_SHORT).show();
                    Log.d("pain",response);
                    makeData();
                }

                @Override
                public void onError(ANError anError) {
                    Toast.makeText(holder.mDeleteButton.getContext(),"Could not delete!",Toast.LENGTH_SHORT).show();
                    anError.printStackTrace();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button mDownloadButton;
        public final Button mDeleteButton;

        public SubmissionItem mItem;

        public ViewHolder(FragmentSubmissionBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mDownloadButton = binding.button6;
            mDeleteButton = binding.button7;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}