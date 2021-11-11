package com.fhtechnikum.einheit7ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder>{

    private List<BluetoothDevice> mItems;
    private Context mContext;
    private ListItemClickListener mListItemClickListener;

    public List<BluetoothDevice> getmItems() {
        return mItems;
    }



    public ListAdapter(List<BluetoothDevice> mItems) {
        this.mItems = mItems;
    }

    interface ListItemClickListener {
        void onListItemClick(BluetoothDevice item);
    }

    public void setOnListItemClickListener(ListItemClickListener listItemClickListener) {
        mListItemClickListener = listItemClickListener;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(position);
        Log.d("BINDLOG", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return (mItems == null) ? 0 : mItems.size();
    }

    public void swapData(List<BluetoothDevice> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView tvName;
        private TextView tvType;
        private TextView tvWeight;
        private LinearLayout llItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            tvWeight = itemView.findViewById(R.id.tv_weight);
            llItem = itemView.findViewById(R.id.ll_item);
            itemView.setOnClickListener(this);
        }

        void bind(int index){
            try{
                tvName.setText(mItems.get(index).getName());
            } catch (Exception e) {
                tvName.setText("unknown");
            }

            try{
                tvType.setText(mItems.get(index).getAddress());
            } catch (Exception e) {
                tvType.setText("unknown");
            }
        }

        @Override
        public void onClick(View view) {
            if (mListItemClickListener != null) {
                int clickedIndex = getAdapterPosition();
                BluetoothDevice clickedItem = mItems.get(clickedIndex);
                mListItemClickListener.onListItemClick(clickedItem);
            }
        }
    }
}
