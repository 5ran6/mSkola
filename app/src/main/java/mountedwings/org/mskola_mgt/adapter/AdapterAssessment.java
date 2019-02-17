/*
 * Copyright 2019 Mountedwings Cybersystems LTD. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mountedwings.org.mskola_mgt.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberAssessment;

public class AdapterAssessment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] regNumbs;
    private String[] names;
    private String TAG = "mSkola";


    private String first_persons_score = "";
    private String school_id;
    private String class_name;
    private String arm;
    private String assessment;
    private String subject;
    private ArrayList<NumberAssessment> numbers = new ArrayList<>();

    private List<NumberAssessment> items;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public AdapterAssessment(Context context, List<NumberAssessment> items) {
        this.items = items;
        ctx = context;
        setHasStableIds(true);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment_record, parent, false);
        vh = new OriginalViewHolder(v);

        ((OriginalViewHolder) vh).register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            NumberAssessment p = items.get(position);


            view.index.setText(p.getIndex());
            view.name.setText(p.getName());
            view.score.setText(p.getScore());

            view.register.setOnClickListener(view1 -> {
                new submitScore().execute(school_id, class_name, arm, assessment, subject, String.valueOf(position), view.score.getText().toString().trim());
            });

            view.skip.setOnClickListener(view2 -> {
                // TODO: collapse and move on

            });

            view.parent.setOnClickListener(view3 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view3, items.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setRegNos(String[] regNo) {
        this.regNumbs = regNo;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberAssessment obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView index;
        public TextView name;
        public EditText score;
        public Button register, skip;
        public LinearLayout parent, section;


        public OriginalViewHolder(View v) {
            super(v);
            index = v.findViewById(R.id.index);
            name = v.findViewById(R.id.tv_label_title);
            register = v.findViewById(R.id.bt_continue_title);
            score = v.findViewById(R.id.et_title);
            skip = v.findViewById(R.id.bt_skip);
            parent = v.findViewById(R.id.main_content);
            section = v.findViewById(R.id.lyt_title);
        }
    }

    private class loading extends AsyncTask<String, Integer, String> {
        int position = 0;

        @Override
        protected String doInBackground(String... strings) {
            position = Integer.valueOf(strings[5]);
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentscore");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4] + "<>" + regNumbs[position]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            Log.d(TAG, scores);
            //   Toast.makeText(getApplicationContext(), scores, Toast.LENGTH_SHORT).show();
            if (!scores.isEmpty()) {
                //  TODO: set editText score with scores
                NumberAssessment p = items.get(position);
                p.setScore(scores);
                notifyDataSetChanged();
            } else {
//                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class submitScore extends AsyncTask<String, Integer, String> {
        int counter = 0;

        @Override
        protected String doInBackground(String... strings) {
            counter = Integer.valueOf(strings[5]);
            storageFile storageObj = new storageFile();
            storageObj.setOperation("savestudentscore");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4] + "<>" + regNumbs[counter] + "<>" + strings[6]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            Log.d(TAG, scores);
            if (!scores.isEmpty()) {
                // TODO: collapse and continue
                counter++;
                if (counter < getItemCount()) {
                    //collapse and continue
                    new loading().execute(school_id, class_name, arm, assessment, subject, String.valueOf(counter));
                }

            } else {
//                Tools.toast("Check your internet connection and try again", ctx, R.color.red_800);
                Toast.makeText(ctx, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //     new loading().execute(school_id, class_name, arm, assessment, subject, String.valueOf(index));
}