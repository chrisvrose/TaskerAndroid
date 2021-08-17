package in.aravindweb.tasker;

import static android.view.View.GONE;

import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import in.aravindweb.tasker.databinding.FragmentItemBinding;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.aravindweb.tasker.databinding.FragmentItemBinding;
import in.aravindweb.tasker.data.AnnouncementData.AnnouncementItem;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AnnouncementItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAnnouncementRecyclerViewAdapter extends RecyclerView.Adapter<MyAnnouncementRecyclerViewAdapter.ViewHolder> {

    private final List<AnnouncementItem> mValues;
    public void setVal(List<AnnouncementItem> vals){mValues.clear();mValues.addAll(vals);}

    String authToken;
    String roomid;
    boolean isTeacher;


    public void makeData(){
        AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms/"+roomid+"/announcements").addHeaders("X-Auth-Token",authToken).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int l = response.length();
                    LinkedList<AnnouncementItem> x = new LinkedList<>();
                    for(int i=0;i<l;i++) {
                        JSONObject j = response.getJSONObject(i);
                        String id = j.getString("_id"),
                                content=j.getString("content"),
                                date=j.getString("createdAt");
                        AnnouncementItem item = new AnnouncementItem(id,content,date);
                        x.add(item);
                    }
                    setVal(x);
                    MyAnnouncementRecyclerViewAdapter.this.notifyDataSetChanged();
                }catch(JSONException e){
                    Log.d("skree","JSOn array exception");
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
//                Toast.makeText(c,"No context found",Toast.LENGTH_LONG).show();
                Log.d("get.announcement.error",anError.toString());
                anError.printStackTrace();
            }
        });
    }
    public MyAnnouncementRecyclerViewAdapter(Context c,String roomid) {
        // android networking
        SharedPreferences sharedPref = c.getSharedPreferences(c.getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        authToken = sharedPref.getString("token","-");
        isTeacher = sharedPref.getBoolean("isTeacher",false);
        this.roomid=roomid;




        makeData();

        mValues = new ArrayList<>();//items;
    }
//    public MyAnnouncementRecyclerViewAdapter(List<>)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).content);
        holder.mContentView.setText(mValues.get(position).date);
        //mValues.get(position).id
        if(isTeacher)
        holder.mDeleteButton.setOnClickListener(v->{
            String url = "https://tasker.aravindweb.in/api/rooms/"+roomid+"/announcements/"+mValues.get(position).id;
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
        public AnnouncementItem mItem;

        public ViewHolder(FragmentItemBinding binding) {
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