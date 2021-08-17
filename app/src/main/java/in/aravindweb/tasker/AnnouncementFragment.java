package in.aravindweb.tasker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.aravindweb.tasker.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class AnnouncementFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String courseName="",courseId="";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnnouncementFragment() {

    }

    public AnnouncementFragment(String courseName,String courseId){
        this.courseId=courseId;
        this.courseName=courseName;
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

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
//        container.getContext();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyAnnouncementRecyclerViewAdapter(
                    //laceholderContent.ITEMS
                    getContext(),courseId
            ));
        }
        return view;
    }
}