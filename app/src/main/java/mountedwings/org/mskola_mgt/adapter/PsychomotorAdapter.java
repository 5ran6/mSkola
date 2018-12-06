package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Simple adapter class, used for show all psychomotor skills in list
 */
public class PsychomotorAdapter extends RecyclerView.Adapter<PsychomotorAdapter.ViewHolder> {

    ArrayList<NumberPsychomotor> numbers;
    String skill_values_array[];
    Boolean isEmpty;
    private OnItemClickListener mOnItemClickListener;

    public PsychomotorAdapter(List<NumberPsychomotor> numbers) {
        this.numbers = new ArrayList<>(numbers);
        this.skill_values_array = new String[getItemCount()];
        this.isEmpty = true;
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
        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (holder.value.getText().toString().isEmpty()) {
                    skill_values_array[holder.getAdapterPosition()] = "_";
                    isEmpty = true;
                } else {
                    skill_values_array[holder.getAdapterPosition()] = String.valueOf(holder.value.getText().toString());
                    isEmpty = false;
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (holder.value.getText().toString().isEmpty()) {
                    skill_values_array[holder.getAdapterPosition()] = "_";
                    isEmpty = true;
                } else {
                    skill_values_array[holder.getAdapterPosition()] = String.valueOf(holder.value.getText().toString());
                    isEmpty = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView skill;
        EditText value;

        public ViewHolder(View v) {
            super(v);
            skill = v.findViewById(R.id.psycho_skills);
            value = v.findViewById(R.id.psycho_value);
        }

        public void bindData(NumberPsychomotor number) {
            skill.setText(number.getskill());
            value.setText(number.getvalue());
        }
    }

    public String[] getSkill_values_array() {
        return skill_values_array;
    }

    public boolean isItEmpty() {
        return isEmpty;
    }

}