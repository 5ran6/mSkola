package mountedwings.org.mskola_mgt.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.Number;
import mountedwings.org.mskola_mgt.teacher.AttendanceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.ViewHolder> {

    Boolean isSelectedAll = false, isASelectedAll1 = false;
    ArrayList<Number> numbers;
    AttendanceFragment attendanceFragment = new AttendanceFragment();

    public NumbersAdapter(List<Number> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindData(numbers.get(position));
        //in some cases, it will prevent unwanted situations
        holder.checkbox1.setOnCheckedChangeListener(null);
        //in some cases, it will prevent unwanted situations
        holder.checkbox2.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkbox1.setChecked(numbers.get(position).isSelected());
        //if true, your checkbox will be selected, else unselected
        holder.checkbox2.setChecked(numbers.get(position).isSelected1());
//        for (int i = 0 ;  i<numbers.size();i++){
//            Log.d("mSkola", String.valueOf(numbers.get(i).isSelected()));
//        }

//        if (!isSelectedAll) holder.checkbox1.setChecked(false);
//        else holder.checkbox1.setChecked(true);
//
//        if (!isASelectedAll1) holder.checkbox2.setChecked(false);
//        else holder.checkbox2.setChecked(true);


        holder.checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                numbers.get(holder.getAdapterPosition()).setSelected(isChecked);
                Log.d("mSkola", String.valueOf(numbers.get(holder.getAdapterPosition()).isSelected()));
            }
        });
        holder.checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                numbers.get(holder.getAdapterPosition()).setSelected1(isChecked);
                Log.d("mSkola", numbers.get(holder.getAdapterPosition()).toString());
            }
        });
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
        private TextView ONEs;
        private TextView textName;
        private CheckBox checkbox1, checkbox2, allMorning, allAfternoon;

        public ViewHolder(View v) {
            super(v);
            ONEs = (TextView) v.findViewById(R.id.ONEs);
            textName = (TextView) v.findViewById(R.id.textONEs);
            checkbox1 = (CheckBox) v.findViewById(R.id.checkbox1);
            checkbox2 = (CheckBox) v.findViewById(R.id.checkbox2);
        }

        public void bindData(Number number) {
            ONEs.setText(number.getONEs());
            textName.setText(number.getTextONEs());
        }
    }

    public void selectAll() {
        //     isSelectedAll = true;
//        if (!isSelectedAll) holder.checkbox1.setChecked(false);
//        else holder.checkbox1.setChecked(true);
//
//        if (!isASelectedAll1) holder.checkbox2.setChecked(false);
//        else holder.checkbox2.setChecked(true);

        notifyDataSetChanged();
    }

    public void unSelectAll() {
        //   isSelectedAll = false;
        notifyDataSetChanged();
    }

    public void selectAll1() {
        //   isASelectedAll1 = true;

        notifyDataSetChanged();
    }

    public void unSelectAll1() {
        // isASelectedAll1 = false;
        notifyDataSetChanged();
    }
}