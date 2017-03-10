package ru.org.adons.cblock.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.org.adons.cblock.R;
import ru.org.adons.cblock.model.BlockListItem;

import static ru.org.adons.cblock.ui.adapter.ListItemDecorator.formatPhoneNumber;
import static ru.org.adons.cblock.ui.adapter.ListItemDecorator.getDescription;

/**
 * Recycler adapter for Block list
 */
public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {

    private final ArrayList<BlockListItem> items = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new BlockListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BlockListItem item = items.get(position);
        holder.textPhone.setText(formatPhoneNumber(item.getPhoneNumber()));
        holder.textDesc.setText(getDescription(item.getName(), item.getDate()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<BlockListItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_phone_number) TextView textPhone;
        @BindView(R.id.text_view_desc) TextView textDesc;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
