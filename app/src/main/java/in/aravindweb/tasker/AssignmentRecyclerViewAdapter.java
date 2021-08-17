package in.aravindweb.tasker;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import in.aravindweb.tasker.data.AssignmentData.AssignmentItem;

import in.aravindweb.tasker.databinding.FragmentAssignmentlinkBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AssignmentItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AssignmentRecyclerViewAdapter extends RecyclerView.Adapter<AssignmentRecyclerViewAdapter.ViewHolder> {

    private final List<AssignmentItem> mValues;

    public AssignmentRecyclerViewAdapter(List<AssignmentItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentAssignmentlinkBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

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
        public AssignmentItem mItem;

        public ViewHolder(FragmentAssignmentlinkBinding binding) {
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