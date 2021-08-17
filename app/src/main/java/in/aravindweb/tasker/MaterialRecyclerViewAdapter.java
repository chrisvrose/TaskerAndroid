package in.aravindweb.tasker;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import in.aravindweb.tasker.data.MaterialData.MaterialItem;
import in.aravindweb.tasker.data.StudentData;

import in.aravindweb.tasker.databinding.FragmentMaterialBinding;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MaterialItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MaterialRecyclerViewAdapter extends RecyclerView.Adapter<MaterialRecyclerViewAdapter.ViewHolder> {

    private final List<MaterialItem> mValues;
    String authToken;
    String roomid;
    boolean isTeacher;

//    public MaterialRecyclerViewAdapter(List<MaterialItem> items) {
//        mValues = items;
//    }
    public MaterialRecyclerViewAdapter(Context c, String roomid){
        SharedPreferences sharedPref = c.getSharedPreferences(c.getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        authToken = sharedPref.getString("token","-");
        isTeacher = sharedPref.getBoolean("isTeacher",false);
        this.roomid=roomid;
        Log.d("create.students","created student list");
        makeData();

        mValues = new ArrayList<>();//items;
    }
    public void setVal(List<MaterialItem> vals){mValues.clear();mValues.addAll(vals);}
    public void makeData(){
        Log.d("roomid",roomid+"");
        AndroidNetworking.get("https://tasker.aravindweb.in/api/rooms/"+roomid+"/materials").addHeaders("X-Auth-Token",authToken).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray ar) {
                try {
                    int l  = ar.length();
                    LinkedList<MaterialItem> x = new LinkedList<>();
                    for(int i=0;i<l;i++){
                        JSONObject j = ar.getJSONObject(i);
                        String id = j.getString("_id");
                        String material = j.getString("material");
                        String name = j.getString("name");
                        MaterialItem item = new MaterialItem(id,material,name);
                        x.add(item);
                    }
                    setVal(x);
                    MaterialRecyclerViewAdapter.this.notifyDataSetChanged();
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

        return new ViewHolder(FragmentMaterialBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public MaterialItem mItem;

        public ViewHolder(FragmentMaterialBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}