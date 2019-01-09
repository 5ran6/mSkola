package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import mountedwings.org.mskola_mgt.data.NumberChildrenList;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class ChildrenListAdapter extends RecyclerView.Adapter<ChildrenListAdapter.ViewHolder> {

    private ArrayList<NumberChildrenList> numbers;
    private OnItemClickListener mOnItemClickListener;

    public ChildrenListAdapter(List<NumberChildrenList> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_children_list, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberChildrenList obj, int position);
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
        private TextView name;
        private TextView class_name;
        private TextView school_name;

        private ImageView passport;
        private CardView cardView;

        private ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            class_name = v.findViewById(R.id.class_name);
            school_name = v.findViewById(R.id.school_name);

            passport = v.findViewById(R.id.passport);
            cardView = v.findViewById(R.id.parent_layout);
        }

        private void bindData(NumberChildrenList number) {
//          name.setText(number.get);
            name.setText(number.getName());
            class_name.setText(String.format("%s %s", number.getClass_name(), number.getArm()));
            school_name.setText(number.getSchoolName());

            Bitmap bitmap = BitmapFactory.decodeByteArray(number.getImageFile(), 0, number.getImageFile().length);
            passport.setImageBitmap(bitmap);

        }
    }


}