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

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberClassStudents;

public class AdapterListClassStudents extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int focusedItem = -1;

    private List<NumberClassStudents> items;

    private OnItemClickListener mOnItemClickListener;

    public AdapterListClassStudents(List<NumberClassStudents> items) {
        this.items = items;

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_students, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            holder.itemView.setSelected(focusedItem == position);
            //      Log.d("mSkola", "position = " + position + " focusedItem = " + focusedItem);
            holder.itemView.setBackgroundColor(focusedItem == position ? Color.GRAY : Color.TRANSPARENT);


            NumberClassStudents p = items.get(position);
            view.name.setText(p.getName());
            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, items.get(position), position);

                    // Updating old as well as new positions
                    notifyItemChanged(focusedItem);
                    focusedItem = position;
                    //   Log.d("mSkola", " focusedItem/adapterPosition = " + focusedItem + "/" + position);

                    notifyItemChanged(focusedItem);


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

    public interface OnItemClickListener {
        void onItemClick(View view, NumberClassStudents obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.classmate_name);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // Do another stuff for your onClick
        }
    }
}