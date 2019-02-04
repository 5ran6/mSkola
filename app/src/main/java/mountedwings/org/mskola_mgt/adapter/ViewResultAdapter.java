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

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberViewResult;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class ViewResultAdapter extends RecyclerView.Adapter<ViewResultAdapter.ViewHolder> {

    private ArrayList<NumberViewResult> numbers;
    private OnItemClickListener mOnItemClickListener;

    public ViewResultAdapter(List<NumberViewResult> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_result, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberViewResult obj, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

        holder.cardView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, numbers.get(position), position);
                setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, NumberViewResult obj, int position) {
                        //          Log.i("mSkola", numbers.get(position).getName());
                    }
                });
            }

        });
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    @Override
    public int getItemCount() {
        return numbers.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name;
        private TextView total;
        private TextView average;
        private TextView no_subjects;
        private TextView position;
        private ImageView passport;
        private CardView cardView;

        private ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            total = v.findViewById(R.id.total);
            average = v.findViewById(R.id.average);
            no_subjects = v.findViewById(R.id.no_subjects);
            position = v.findViewById(R.id.position);
            passport = v.findViewById(R.id.passport);
            cardView = v.findViewById(R.id.parent_layout);
        }

        private void bindData(NumberViewResult number) {
//          name.setText(number.get);
            name.setText(number.getName());
            total.setText(String.format("Total: %s", number.gettotal()));
            average.setText(String.format("Average: %s", number.getaverage()));
            no_subjects.setText(String.format("No. Subjects: %s", number.getno_subjects()));
            if (number.getPosition().equals("0"))
                position.setText("");
            else
                position.setText(String.format("Pos.: %s", number.getPosition()));

            Bitmap bitmap = BitmapFactory.decodeByteArray(number.getImageFile(), 0, number.getImageFile().length);
            passport.setImageBitmap(bitmap);


        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}