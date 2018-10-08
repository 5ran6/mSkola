package mountedwings.org.mskola_mgt.teacher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersAdapter;
import mountedwings.org.mskola_mgt.adapter.NumbersAssHistAdapter;
import mountedwings.org.mskola_mgt.data.Number;
import mountedwings.org.mskola_mgt.data.NumberAssHist;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

/**
 * A placeholder fragment containing a simple view.
 */

public class AssignmentHistoryFragment extends Fragment {

    ArrayList<NumberAssHist> numbers = new ArrayList<>();


    private RecyclerView list;
    private FloatingActionButton fab_done;
    private TextView heading;

    String school_id, staff_id, TAG = "mSkola";

    ProgressBar loading;
    NumbersAssHistAdapter adapter;

    public AssignmentHistoryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ass_hist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab_done = view.findViewById(R.id.done);
        heading = view.findViewById(R.id.assignment_history_title);
        heading.setText(R.string.given_ass);
        loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setHasFixedSize(false);
    }

    public class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //  Boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getasshistory");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();
            Log.d(TAG, text);

            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.isEmpty() && !text.equals("0")) {
                loading.setVisibility(View.GONE);
                //worked. Split into reg and names
                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    NumberAssHist number = new NumberAssHist();
                    number.setDate(rows[i].split(";")[0]);
                    number.setSubject(rows[i].split(";")[1]);
                    number.setClassArm(rows[i].split(";")[2]);
                    number.setStaff(rows[i].split(";")[3]);
                    number.setAssignment(rows[i].split(";")[4]);

                    numbers.add(number);
                }
                //show recyclerView with inflated views
                adapter = new NumbersAssHistAdapter(numbers);
                list.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "You haven't given an assignment!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

/////////////////////////////////////////////////////////////////////////////////////////////////////  init
        school_id = getActivity().getIntent().getStringExtra("school_id");
        staff_id = getActivity().getIntent().getStringExtra("staff_id");
        //hide parentView
        loading.setVisibility(View.VISIBLE);

//        new first_loading().execute(school_id, staff_id);
        new first_loading().execute("cac180826043520", "admin");
///////////////////////////////////////////////////////////////////////////////////////////////////////
        fab_done.setOnClickListener(v -> getActivity().finish());
    }
}
