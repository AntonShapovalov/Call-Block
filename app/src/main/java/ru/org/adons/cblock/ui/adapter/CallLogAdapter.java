package ru.org.adons.cblock.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.org.adons.cblock.R;
import ru.org.adons.cblock.model.CallLogItem;

import static ru.org.adons.cblock.ui.adapter.ListItemDecorator.formatPhoneNumber;
import static ru.org.adons.cblock.ui.adapter.ListItemDecorator.getDescription;

/**
 * Recycler adapter for Call Log list
 */
public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

    private final ArrayList<CallLogItem> items = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_call_log, parent, false);
        return new CallLogAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CallLogItem item = items.get(position);
        holder.textPhone.setText(formatPhoneNumber(item.phoneNumber()));
        holder.textDesc.setText(getDescription(item.name(), item.date()));
        if (item.isBlocked) {
            holder.isBlocked.setChecked(true);
            holder.isBlocked.setEnabled(false);
        } else {
            holder.isBlocked.setChecked(false);
            holder.isBlocked.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<CallLogItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkbox_is_blocked) CheckBox isBlocked;
        @BindView(R.id.text_view_phone_number) TextView textPhone;
        @BindView(R.id.text_view_desc) TextView textDesc;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
