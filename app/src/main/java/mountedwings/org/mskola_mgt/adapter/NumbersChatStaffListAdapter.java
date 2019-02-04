package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberChatStaffList;
import mountedwings.org.mskola_mgt.utils.Tools;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class NumbersChatStaffListAdapter extends RecyclerView.Adapter<NumbersChatStaffListAdapter.ViewHolder> {
    private Context ctx;
    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;
    public List<String> items = new ArrayList<>();

    private ArrayList<NumberChatStaffList> numbers;
    public ArrayList<String> selected = new ArrayList();
    private OnClickListener onClickListener = null;

    public NumbersChatStaffListAdapter(Context context, List<NumberChatStaffList> numbers) {
        this.ctx = context;
        this.numbers = new ArrayList<>(numbers);
        selected_items = new SparseBooleanArray();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_staff_list, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

        holder.lyt_parent.setOnClickListener(v -> {
            if (onClickListener == null) return;
            onClickListener.onItemClick(v, numbers.get(position), holder.getAdapterPosition());


        });
        holder.lyt_parent.setOnLongClickListener(v -> {
            if (onClickListener == null) return false;
            onClickListener.onItemLongClick(v, numbers.get(position), position);
            return true;
        });

        toggleCheckedIcon(holder, holder.getAdapterPosition());
        displayImage(holder, numbers.get(position));
    }


    private void displayImage(NumbersChatStaffListAdapter.ViewHolder holder, NumberChatStaffList inbox) {
        if (inbox.image != null) {
            Tools.displayImageRound(ctx, holder.image, inbox.image);
            holder.image.setColorFilter(null);
            holder.image_letter.setVisibility(View.GONE);
        } else {
            holder.image.setImageResource(R.drawable.shape_circle);
            holder.image.setColorFilter(inbox.color);
            holder.image_letter.setVisibility(View.VISIBLE);
        }
    }

    private void toggleCheckedIcon(NumbersChatStaffListAdapter.ViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.lyt_image.setVisibility(View.GONE);
            holder.lyt_checked.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.lyt_checked.setVisibility(View.GONE);
            holder.lyt_image.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public NumberChatStaffList getItem(int position) {
        return numbers.get(position);
    }

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            //if it was there before, remove it
            items.remove(numbers.get(pos).getEmail());
            selected_items.delete(pos);
        } else {
            //else put
            items.add(numbers.get(pos).getEmail());
            selected_items.put(pos, true);
        }
        Log.i("mSkola", items.toString());
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selected_items.clear();
        items.clear();
        notifyDataSetChanged();
    }

    public List<String> selectAll() {
        items = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            items.add(numbers.get(i).getEmail());
            selected_items.put(i, true);
            notifyItemChanged(i);
        }
        notifyDataSetChanged();
        return items;
    }

    public List<String> getSelectedItems() {
        Log.i("mSkola", items.toString());
        return items;
    }

    public void removeData(int position) {
        numbers.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView select;
        public ImageView image;
        public TextView image_letter;
        public RelativeLayout lyt_checked, lyt_image;
        public CardView lyt_parent;

        private ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            select = v.findViewById(R.id.select);
            image_letter = v.findViewById(R.id.image_letter);

            image = v.findViewById(R.id.image);

            lyt_checked = v.findViewById(R.id.lyt_checked);
            lyt_image = v.findViewById(R.id.lyt_image);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }

        private void bindData(NumberChatStaffList number) {

            name.setText(number.getRecipient());
            Bitmap bitmap = BitmapFactory.decodeByteArray(number.getImageFile(), 0, number.getImageFile().length);
            image.setImageBitmap(bitmap);

            lyt_parent.setActivated(selected_items.get(getAdapterPosition(), false));
            if (selected_items.get(getAdapterPosition())) {
                select.setText(R.string.selected);
                select.setTextColor(ctx.getResources().getColor(R.color.green_900));
            } else {
                select.setText(R.string.select);
            }


        }
    }

    public interface OnClickListener {
        void onItemClick(View view, NumberChatStaffList obj, int pos);

        void onItemLongClick(View view, NumberChatStaffList obj, int pos);
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