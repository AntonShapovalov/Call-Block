package ru.org.adons.cblock.ui.main;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
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
import ru.org.adons.cblock.utils.StringUtils;

/**
 * Recycler adapter for Block List View
 */
class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {

    private final ArrayList<BlockListItem> items = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new BlockListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BlockListItem item = items.get(position);
        //
        String phone = StringUtils.formatPhoneNumber(item.getPhoneNumber());
        holder.textPhone.setText(phone);
        //
        String desc = !TextUtils.isEmpty(item.getName()) ? item.getName() + ", " : "";
        desc += DateUtils.getRelativeTimeSpanString(item.getDate());
        holder.textDesc.setText(desc);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void setItems(List<BlockListItem> newItems) {
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
