package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberStudentsAssignment;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class StudentAssHistAdapter extends RecyclerView.Adapter<StudentAssHistAdapter.ViewHolder> {

    private ArrayList<NumberStudentsAssignment> numbers;
    private OnItemClickListener mOnItemClickListener;

    public StudentAssHistAdapter(List<NumberStudentsAssignment> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_assignments, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberStudentsAssignment obj, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

        holder.cardView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, numbers.get(position), position);

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
        private TextView code;

        private CardView cardView;

        private ViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.date);
            subject = v.findViewById(R.id.subject);
            code = v.findViewById(R.id.code);
            cardView = v.findViewById(R.id.parent_layout);
        }

        private void bindData(NumberStudentsAssignment number) {
            date.setText(number.getAssignment_date());
            subject.setText(number.getSubject());
            code.setText(String.format("Subject: %s", number.getCode()));
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