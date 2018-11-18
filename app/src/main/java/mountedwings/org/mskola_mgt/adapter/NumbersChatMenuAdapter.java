package mountedwings.org.mskola_mgt.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberChat;

/**
 * Simple adapter class, used for show all messages in list
 */
public class NumbersChatMenuAdapter extends RecyclerView.Adapter<NumbersChatMenuAdapter.ViewHolder> {

    private ArrayList<NumberChat> numbers;
    private OnItemClickListener mOnItemClickListener;

    public NumbersChatMenuAdapter(List<NumberChat> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_menu, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberChat obj, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

        holder.cardView.setOnClickListener(v -> {
            //TODO: intent to open a class with passed extras
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, numbers.get(position), position);
                setOnItemClickListener((view, obj, position1) ->
                        Log.i("mSkola", numbers.get(position).getRecipient()));
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
        private TextView msg;
        private TextView recipient;
        private TextView date;
        private ImageView passport;
        private CardView cardView;

        private ViewHolder(View v) {
            super(v);
            msg = v.findViewById(R.id.msg);
            recipient = v.findViewById(R.id.recipient);
            passport = v.findViewById(R.id.image);
            cardView = v.findViewById(R.id.parent_layout);
            date = v.findViewById(R.id.date);

        }

        private void bindData(NumberChat number) {
            recipient.setText(number.getRecipient().toUpperCase());
            msg.setText(number.getmsg());
            date.setText(number.getdate());
            Bitmap bitmap = BitmapFactory.decodeByteArray(number.getImageFile(), 0, number.getImageFile().length);
            passport.setImageBitmap(bitmap);
        }
    }


}