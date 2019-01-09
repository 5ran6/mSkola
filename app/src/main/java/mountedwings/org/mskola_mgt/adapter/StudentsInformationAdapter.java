package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberStudentsInformation;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class StudentsInformationAdapter extends RecyclerView.Adapter<StudentsInformationAdapter.ViewHolder> {
    private Context ctx;
    public List<String> items = new ArrayList<>();

    private ArrayList<NumberStudentsInformation> numbers;

    public StudentsInformationAdapter(Context context, List<NumberStudentsInformation> numbers) {
        this.ctx = context;
        this.numbers = new ArrayList<>(numbers);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_student_information, parent, false);
        return new ViewHolder(v);
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView value;


        private ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            value = v.findViewById(R.id.value);
        }

        private void bindData(NumberStudentsInformation number) {

            title.setText(number.getField());
            value.setText(number.getValue());
        }
    }

    public interface OnClickListener {
        void onItemClick(View view, NumberStudentsInformation obj, int pos);
    }

}