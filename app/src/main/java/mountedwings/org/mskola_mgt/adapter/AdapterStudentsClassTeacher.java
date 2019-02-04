package mountedwings.org.mskola_mgt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberClassTeachers;

public class AdapterStudentsClassTeacher extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NumberClassTeachers> items;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, NumberClassTeachers obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterStudentsClassTeacher(Context context, List<NumberClassTeachers> items) {
        this.items = items;
        ctx = context;
        setHasStableIds(true);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView contact;
        public CardView lyt_parent;
        public ImageView logo;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            contact = v.findViewById(R.id.contact);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            logo = v.findViewById(R.id.image);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_teacher, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            NumberClassTeachers p = items.get(position);

            Bitmap bitmap = BitmapFactory.decodeByteArray(p.getPassport(), 0, p.getPassport().length);
            view.logo.setImageBitmap(bitmap);

            view.name.setText(p.getName());
            view.contact.setText(String.format("%s - %s", p.getEmail(), p.getPhone()));
            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, items.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
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