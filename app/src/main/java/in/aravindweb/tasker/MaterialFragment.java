package in.aravindweb.tasker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import in.aravindweb.util.FileUtils;

/**
 * A fragment representing a list of Items.
 */
public class MaterialFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String courseName = "", courseId = "";

    private Uri filePickedUriuri = null;
    MaterialRecyclerViewAdapter mva;
    private static int PICKFILE_RESULT_CODE = 25;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MaterialFragment() {
    }

    public MaterialFragment(String cn, String ci) {
        courseId = ci;
        courseName = cn;
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
        recyclerView.setAdapter(mva = new MaterialRecyclerViewAdapter(
                getContext(), courseId
        ));
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.tokenLocation), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "-");
        boolean isTeacher = sharedPref.getBoolean("isTeacher", false);

        Button b = viewparent.findViewById(R.id.button2);
        if (isTeacher) {
            b.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Upload Material");
                Button fb = new Button(getContext());
                fb.setText("Pick file");
                fb.setOnClickListener(lv -> {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                });

                final EditText input = new EditText(getContext());
                input.setHint("Enter Description");
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                LinearLayout dialogLayout = new LinearLayout(getContext());
                dialogLayout.addView(fb);
                dialogLayout.addView(input);

                builder.setView(dialogLayout);
                builder.setPositiveButton("UPLOAD", (dv, which) -> {
                    if (filePickedUriuri == null) {
                        Toast.makeText(getContext(), "Please pick a file", Toast.LENGTH_SHORT).show();
                    } else {
                        // this is the worst, create a temp file, copy the IS into that file then upload it
                        try {
                            InputStream s = getContext().getContentResolver().openInputStream(filePickedUriuri);
                            File tempFile = File.createTempFile("olkliksdfhglosdiertlkushdfgsj", "jav");
                            OutputStream ss = new FileOutputStream(tempFile, false);

                            int n;
                            byte[] buffer = new byte[1024];
                            while ((n = s.read(buffer)) > -1) {
                                ss.write(buffer, 0, n);   // Don't allow any extra bytes to creep in, final write
                            }
                            ss.close();
                            AndroidNetworking
                                    .upload("https://tasker.aravindweb.in/api/upload/"+courseId+"/materials")
                                    .addMultipartFile("file", tempFile)

                                    .addHeaders("description", input.getText().toString())
                                    .addHeaders("X-Auth-Token", token)
                                    .build().getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                    tempFile.deleteOnExit();
                                    mva.makeData();
                                }

                                @Override
                                public void onError(ANError anError) {
                                    anError.printStackTrace();
                                    Toast.makeText(getContext(), "Error" + anError.getErrorCode(), Toast.LENGTH_SHORT).show();
                                    tempFile.deleteOnExit();

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                builder.setNegativeButton("Cancel", (dv, which) -> {

                });

                builder.show();

            });
        } else {
            b.setVisibility(View.GONE);
        }

        return viewparent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE) {
            filePickedUriuri = data.getData();

        }
    }
}