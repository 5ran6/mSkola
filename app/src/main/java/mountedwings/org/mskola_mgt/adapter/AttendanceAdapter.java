package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.Number;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private ArrayList<Number> numbers;

    public AttendanceAdapter(List<Number> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));
        //in some cases, it will prevent unwanted situations
        holder.checkbox1.setOnCheckedChangeListener(null);
        //in some cases, it will prevent unwanted situations
        holder.checkbox2.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkbox1.setChecked(numbers.get(position).isSelected());
        //if true, your checkbox will be selected, else unselected
        holder.checkbox2.setChecked(numbers.get(position).isSelected1());


        holder.checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                numbers.get(holder.getAdapterPosition()).setSelected(isChecked);
                if (!numbers.get(position).isSelected()) {
                    for (int i = 0; i < numbers.size(); i++)
                        numbers.get(i).setAllSelectedM(false);
                }

                Log.d("mSkola", String.valueOf(Boolean.valueOf(numbers.get(holder.getAdapterPosition()).isSelected())));
            }
        });
        holder.checkbox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            numbers.get(holder.getAdapterPosition()).setSelected1(isChecked);
            if (!numbers.get(position).isSelected()) {
                for (int i = 0; i < numbers.size(); i++)
                    numbers.get(i).setAllSelectedA(false);
            }
            Log.d("mSkola", String.valueOf(Boolean.valueOf(numbers.get(holder.getAdapterPosition()).toString())));
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
        private CheckBox checkbox1, checkbox2;

        private ViewHolder(View v) {
            super(v);
            ONEs = v.findViewById(R.id.ONEs);
            textName = v.findViewById(R.id.textONEs);
            checkbox1 = v.findViewById(R.id.checkbox1);
            checkbox2 = v.findViewById(R.id.checkbox2);
        }

        public void bindData(Number number) {
            ONEs.setText(number.getONEs());
            textName.setText(number.getTextONEs());
        }
    }

    public void selectAll() {
        notifyDataSetChanged();
    }

    public void unSelectAll() {
        notifyDataSetChanged();
    }

    public void selectAll1() {
        notifyDataSetChanged();
    }

    public void unSelectAll1() {
        notifyDataSetChanged();
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