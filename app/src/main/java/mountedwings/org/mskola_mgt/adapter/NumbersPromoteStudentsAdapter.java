package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberPromoteStudents;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class NumbersPromoteStudentsAdapter extends RecyclerView.Adapter<NumbersPromoteStudentsAdapter.ViewHolder> {

    ArrayList<NumberPromoteStudents> numbers;
    private OnItemClickListener mOnItemClickListener;
    public ArrayList<String> selected = new ArrayList();

    public NumbersPromoteStudentsAdapter(List<NumberPromoteStudents> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promote_students, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberPromoteStudents obj, int position);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

        holder.cardView.setOnClickListener(v -> {
            //TODO: intent to do something (like concat strings separated by ';')
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, numbers.get(position), position);
                setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, NumberPromoteStudents obj, int position) {
                        if (numbers.get(position).getSelected()) {
                            selected.remove(numbers.get(position).getregNo());
                            numbers.get(position).setSelected(false);

                            //UI_update
                            notifyDataSetChanged();
                        } else {
                            selected.add(numbers.get(position).getregNo());
                            numbers.get(position).setSelected(true);

                            //UI_update
                            notifyDataSetChanged();
                        }
                    }
                });
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
        private TextView name;
        private TextView reg_number;
        private TextView class_arm;
        private TextView select;
        private TextView current_session;
        private ImageView checker;
        private CardView cardView;

        private ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            reg_number = v.findViewById(R.id.regNo);
            class_arm = v.findViewById(R.id.class_arm);
            select = v.findViewById(R.id.select);
            current_session = v.findViewById(R.id.session);
            checker = v.findViewById(R.id.selection);
            cardView = v.findViewById(R.id.parent_layout);
        }

        private void bindData(NumberPromoteStudents number) {

            name.setText(number.getName());
            reg_number.setText(number.getregNo());
            class_arm.setText(number.getclass_arm());
            current_session.setText(number.getsession());
            if (number.getSelected()) {
                checker.setImageResource(R.drawable.ic_done);
//                select.setTextColor(R.color.yellow_400);
                select.setText(R.string.selected);
            } else {
                checker.setImageResource(R.drawable.ic_person);
                select.setText(R.string.select);
            }
        }
    }


}