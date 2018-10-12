package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.Number;
import mountedwings.org.mskola_mgt.data.NumberAssHist;
import mountedwings.org.mskola_mgt.teacher.Assignment_history_detail;
import mountedwings.org.mskola_mgt.teacher.AttendanceFragment;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class NumbersAssHistAdapter extends RecyclerView.Adapter<NumbersAssHistAdapter.ViewHolder> {

    ArrayList<NumberAssHist> numbers;
    private OnItemClickListener mOnItemClickListener;

    public NumbersAssHistAdapter(List<NumberAssHist> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignments, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberAssHist obj, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: intent to open a class with passed extras
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, numbers.get(position), position);

                }

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
        private TextView date;
        private TextView subject;
        private TextView classArm;
        private TextView staff_id;
        private CardView cardView;

        private ViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.date);
            subject = v.findViewById(R.id.subject);
            classArm = v.findViewById(R.id.current_class);
            staff_id = v.findViewById(R.id.teacher_name);
            cardView = v.findViewById(R.id.parent_layout);
        }

        private void bindData(NumberAssHist number) {
            date.setText(number.getDate());
            subject.setText(number.getSubject());
            classArm.setText(number.getClassArm());
            staff_id.setText(String.format("By: %s", number.getStaff()));
        }
    }


}