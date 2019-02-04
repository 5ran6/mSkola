package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberResultItem;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class ParentResultListHeader extends RecyclerView.Adapter<ParentResultListHeader.ViewHolder> {

    private ArrayList<NumberResultItem> numbers;

    public ParentResultListHeader(List<NumberResultItem> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_result_items, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberResultItem obj, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

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
        private TextView score;
        private TextView ca;

        private ViewHolder(View v) {
            super(v);
            score = v.findViewById(R.id.score);
            ca = v.findViewById(R.id.ca);
        }

        private void bindData(NumberResultItem number) {
            score.setText(number.getValue());
            ca.setText(number.getTitle());
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