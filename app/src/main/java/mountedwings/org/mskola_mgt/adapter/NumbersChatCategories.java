package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberChatCategory;

/**
 * Simple adapter class, used for show all NumberChatCategorys in list
 */
public class NumbersChatCategories extends RecyclerView.Adapter<NumbersChatCategories.ViewHolder> {

    ArrayList<NumberChatCategory> NumberChatCategorys;

    public NumbersChatCategories(List<NumberChatCategory> NumberChatCategorys) {
        this.NumberChatCategorys = new ArrayList<>(NumberChatCategorys);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(NumberChatCategorys.get(position));
        //in some cases, it will prevent unwanted situations
        holder.checkbox1.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkbox1.setChecked(NumberChatCategorys.get(position).isSelected());
        //if true, your checkbox will be selected, else unselected
//        for (int i = 0 ;  i<NumberChatCategorys.size();i++){
//            Log.d("mSkola", String.valueOf(NumberChatCategorys.get(i).isSelected()));
//        }

//        if (!isSelectedAll) holder.checkbox1.setChecked(false);
//        else holder.checkbox1.setChecked(true);
//
//        if (!isASelectedAll1) holder.checkbox2.setChecked(false);
//        else holder.checkbox2.setChecked(true);


        holder.checkbox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NumberChatCategorys.get(holder.getAdapterPosition()).setSelected(isChecked);
            if (!NumberChatCategorys.get(position).isSelected()) {
                for (int i = 0; i < NumberChatCategorys.size(); i++)
                    NumberChatCategorys.get(i).setAllSelectedA(false);
            }

            Log.d("mSkola", String.valueOf(Boolean.valueOf(NumberChatCategorys.get(holder.getAdapterPosition()).isSelected())));
        });
    }

    @Override
    public int getItemCount() {
        return NumberChatCategorys.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView names;
        private CheckBox checkbox1;

        private ViewHolder(View v) {
            super(v);
            names = v.findViewById(R.id.names);
            checkbox1 = v.findViewById(R.id.checkbox1);
        }

        private void bindData(NumberChatCategory numberChatCategory) {
            names.setText(numberChatCategory.getnames());

            //set checked too

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