package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberPsychomotor;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class NumbersPsychomotorAdapter extends RecyclerView.Adapter<NumbersPsychomotorAdapter.ViewHolder> {

    ArrayList<NumberPsychomotor> numbers;
    private OnItemClickListener mOnItemClickListener;

    public NumbersPsychomotorAdapter(List<NumberPsychomotor> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_psychomotor, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberPsychomotor obj, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

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
        private TextView skill;
        private EditText value;

        private ViewHolder(View v) {
            super(v);
            skill = v.findViewById(R.id.psycho_skills);
            value = v.findViewById(R.id.psycho_value);
        }

        private void bindData(NumberPsychomotor number) {
            skill.setText(number.getskill());
            value.setText(number.getvalue());
        }
    }


}