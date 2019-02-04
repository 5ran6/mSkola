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
import mountedwings.org.mskola_mgt.data.NumberAchievements;

/**
 * Simple adapter class, used for show all numbers in list
 */
public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder> {

    ArrayList<NumberAchievements> numbers;
    private OnItemClickListener mOnItemClickListener;

    public AchievementsAdapter(List<NumberAchievements> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievements, parent, false);
        return new ViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NumberAchievements obj, int position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(numbers.get(position));

        holder.cardView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, numbers.get(position), position);
                setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, NumberAchievements obj, int position) {
                        Log.i("mSkola", numbers.get(position).getachievement());
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
        private TextView achievement;
        private TextView title;
        private ImageView passport;
        private CardView cardView;

        private ViewHolder(View v) {
            super(v);
            achievement = v.findViewById(R.id.subtitle);
            title = v.findViewById(R.id.title);
            passport = v.findViewById(R.id.image);
            cardView = v.findViewById(R.id.parent_layout);
        }

        private void bindData(NumberAchievements number) {
            title.setText(number.gettitle().toUpperCase());
            achievement.setText(number.getachievement());

            Bitmap bitmap = BitmapFactory.decodeByteArray(number.getImageFile(), 0, number.getImageFile().length);
            passport.setImageBitmap(bitmap);

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