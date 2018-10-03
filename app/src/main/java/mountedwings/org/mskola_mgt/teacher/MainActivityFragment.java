package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import mountedwings.org.mskola_mgt.adapter.NumbersAdapter;
import mountedwings.org.mskola_mgt.data.Number;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private View back_drop;
    private boolean rotate = false;
    private View lyt_hols;
    private View lyt_save;
    private CheckBox all_morning, all_afternoon;
    ArrayList<Number> numbers;
    private ArrayList names = new ArrayList();
    private ArrayList regNo = new ArrayList();
    private RecyclerView list;
    private FloatingActionButton fab_add, fab_holidays, fab_save;
    private String[] morning = null, afternoon = null;

    String date, school_id, class_name, arm, TAG = "mSkola";
    ProgressBar loading;
    NumbersAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab_add = (FloatingActionButton) view.findViewById(R.id.see_more);
        fab_holidays = (FloatingActionButton) view.findViewById(R.id.fab_hols);
        fab_save = (FloatingActionButton) view.findViewById(R.id.fab_save);
        back_drop = view.findViewById(R.id.back_drop);
        all_morning = view.findViewById(R.id.all_checkbox1);
        all_afternoon = view.findViewById(R.id.all_checkbox2);

        loading = (ProgressBar) view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        lyt_hols = view.findViewById(R.id.lyt_mic);
        lyt_save = view.findViewById(R.id.lyt_call);
        ViewAnimation.initShowOut(lyt_hols);
        ViewAnimation.initShowOut(lyt_save);
        back_drop.setVisibility(View.GONE);

        list = (RecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setHasFixedSize(false);
    }

    //DONE
    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_hols);
            ViewAnimation.showIn(lyt_save);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_hols);
            ViewAnimation.showOut(lyt_save);
            back_drop.setVisibility(View.GONE);
        }
    }


    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //  Boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("loadattendance");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3]);
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

                //get values
                morning = new String[text.split("<>").length];
                afternoon = new String[text.split("<>").length];

                numbers = new ArrayList<>();

                //if there was previous data
                if (text.contains("##")) {
                    morning = text.split("##")[1].split(";");
                    afternoon = text.split("##")[2].split(";");
                    text = text.split("##")[0];
                }

                //worked. Splited into reg and names
                String rows[] = text.split("<>");
                if (rows.length > 0) {
                    for (int i = 0; i < rows.length; i++) {
                        regNo.add(rows[i].split(";")[0]);
                        names.add(rows[i].split(";")[1]);

                        Number number = new Number();
                        number.setONEs((i + 1) + "");
                        number.setTextONEs(names.get(i).toString());
                        try {
//                            Log.d(TAG, morning[i].toString());
                            //checked morning
                            if (morning[i].trim().equals("1")) number.setSelected(true);
                            //checked afternoon
                            if (afternoon[i].trim().equals("1")) number.setSelected1(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        numbers.add(number);
                    }
                    //show recyclerView with inflated views
                    adapter = new NumbersAdapter(numbers);
                    list.setAdapter(adapter);
                } else {
                    // display an EMPTY error dialog and return to previous activity
                    Toast.makeText(getContext(), "Something wrong went wrong", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            } else {
                Toast.makeText(getContext(), "No record found for selected class/arm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

/////////////////////////////////////////////////////////////////////////////////////////////////////  init
        school_id = getActivity().getIntent().getStringExtra("school_id");
        class_name = getActivity().getIntent().getStringExtra("class_name");
        arm = getActivity().getIntent().getStringExtra("arm");
        date = getActivity().getIntent().getStringExtra("date");

//        Toast.makeText(getContext(), school_id, Toast.LENGTH_SHORT).show();
        loading.setVisibility(View.VISIBLE);

        //        new first_loading().execute(school_id, class_name, arm, date);
        new first_loading().execute("cac180826043520", "JSS1", "A", "2018-10-03");
///////////////////////////////////////////////////////////////////////////////////////////////////////

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });

        fab_holidays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getActivity().getApplicationContext(), "Voice clicked", Toast.LENGTH_SHORT).show();
            }
        });

        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //              Toast.makeText(getActivity().getActivity().getApplicationContext(), "Call clicked", Toast.LENGTH_SHORT).show();
            }
        });


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder stringBuilder1 = new StringBuilder();
                for (Number number : numbers) {
                    if (number.isSelected()) {
                        if (stringBuilder.length() > 0)
                            stringBuilder.append(", ");
                        stringBuilder.append(number.getONEs());
                    }
                    if (number.isSelected1()) {
                        if (stringBuilder1.length() > 0)
                            stringBuilder1.append(", ");
                        stringBuilder1.append(number.getONEs());
                    }
                }
//                Toast.makeText(getActivity(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        });

        all_morning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //tick all
                    adapter.selectAll();
                } else {
                    //uncheck all
                    adapter.unSelectAll();

                }
            }
        });
        all_afternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //tick all
                    adapter.selectAll1();
                } else {
                    //uncheck all
                    adapter.unSelectAll1();

                }
            }
        });
    }
}
