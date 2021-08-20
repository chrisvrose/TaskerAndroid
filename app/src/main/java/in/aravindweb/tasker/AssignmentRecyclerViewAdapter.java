package in.aravindweb.tasker;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.aravindweb.tasker.data.AssignmentData.AssignmentItem;

import in.aravindweb.tasker.databinding.FragmentAssignmentlinkBinding;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AssignmentItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AssignmentRecyclerViewAdapter extends RecyclerView.Adapter<AssignmentRecyclerViewAdapter.ViewHolder> {

    private final List<AssignmentItem> mValues;

    String authToken;
    String roomid;
    boolean isTeacher;

    public AssignmentRecyclerViewAdapter(List<AssignmentItem> items) {
        mValues = items;
    }

    public void setVal(List<AssignmentItem> vals){mValues.clear();mValues.addAll(vals);}

    public void makeData(){
        Log.d("roomid",roomid+"");
        AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms/"+roomid+"/resources").addHeaders("X-Auth-Token",authToken).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray ar) {
                try {
                    int l  = ar.length();
                    LinkedList<AssignmentItem> x = new LinkedList<>();
                    for(int i=0;i<l;i++){
                        JSONObject j = ar.getJSONObject(i);
                        String id = j.getString("_id");
                        String createdAt = j.getString("createdAt");
                        String deadline = j.getString("deadline");
                        String resource = j.getString("resource");
                        String description = j.getString("description");
                        AssignmentItem item = new AssignmentItem(id,createdAt,deadline,resource,description);
                        x.add(item);
                    }
                    setVal(x);
                    AssignmentRecyclerViewAdapter.this.notifyDataSetChanged();
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

    public AssignmentRecyclerViewAdapter(Context c, String roomid){
        SharedPreferences sharedPref = c.getSharedPreferences(c.getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        authToken = sharedPref.getString("token","-");
        isTeacher = sharedPref.getBoolean("isTeacher",false);
        this.roomid=roomid;
        Log.d("create.students","created student list");
        makeData();

        mValues = new ArrayList<>();//items;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentAssignmentlinkBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDesc.setText(mValues.get(position).description);
        holder.mCreatedTime.setText("Created: "+mValues.get(position).createdAt);
        holder.mDeadLineTime.setText("Deadline: "+mValues.get(position).deadline);


        holder.mDownloadButton.setOnClickListener((v)->{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mValues.get(position).resource));
            holder.mDownloadButton.getContext().startActivity(browserIntent);
        });
        holder.mSubsButton.setOnClickListener((v)->{
            Intent Cake = new Intent(holder.mSubsButton.getContext(), SubmissionsActivity.class);
            Cake.putExtra("roomId",roomid);
            Cake.putExtra("resId",holder.mItem.id);
            holder.mDownloadButton.getContext().startActivity(Cake);
        });
        if(isTeacher){
        holder.mDeadLineTime.setOnClickListener(v->{
            final View dialogView = View.inflate(holder.mDeadLineTime.getContext(), R.layout.layout_dtpicker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(holder.mDeadLineTime.getContext()).create();

            dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                    TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute());
                    DateTimeFormatter formatter =
                            DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC));

                    Log.d("Time.got", formatter.format(calendar.toInstant())+"");

                    JSONObject j = new JSONObject();
                    try {
                        j.put("deadline",formatter.format(calendar.toInstant()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AndroidNetworking.post("https://tasker.aravindweb.in/api/upload/"+roomid+"/"+ holder.mItem.id+"/deadline").addHeaders("X-Auth-Token",authToken)
                            .addJSONObjectBody(j)
                            .build().getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(holder.mDeadLineTime.getContext(),"Changed deadline!",Toast.LENGTH_SHORT).show();
                            makeData();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(holder.mDeadLineTime.getContext(),"Cannot change deadline!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.dismiss();
                }});
            alertDialog.setView(dialogView);
            alertDialog.show();

        });
            holder.mDeleteButton.setOnClickListener(v->{
                AndroidNetworking.delete("https://tasker.aravindweb.in/api/upload/"+roomid+"/resources/"+holder.mItem.id).addHeaders("X-Auth-Token",authToken)
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
        }else{
            holder.mDeleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mDesc;
        public final TextView mCreatedTime;
        public final TextView mDeadLineTime;
        public final Button mSubsButton;
        public final Button mDownloadButton;
        public final Button mDeleteButton;

        public AssignmentItem mItem;

        public ViewHolder(FragmentAssignmentlinkBinding binding) {
            super(binding.getRoot());
            mDesc = binding.assignmentDescription;
            mCreatedTime = binding.assignmentCreatedTime;
            mDownloadButton= binding.downloadButton;
            mDeadLineTime = binding.assignmentDeadlineTime;
            mSubsButton = binding.viewsub;
            mDeleteButton = binding.deleteButton;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCreatedTime.getText() + "'";
        }
    }
}