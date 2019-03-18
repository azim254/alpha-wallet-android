package io.stormbird.wallet.ui.widget.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URISyntaxException;
import java.util.List;

import io.stormbird.wallet.R;
import io.stormbird.wallet.entity.DApp;
import io.stormbird.wallet.ui.widget.OnDappClickListener;
import io.stormbird.wallet.ui.widget.OnDappEditedListener;
import io.stormbird.wallet.ui.widget.OnDappRemovedListener;
import io.stormbird.wallet.util.DappBrowserUtils;
import io.stormbird.wallet.util.Utils;

public class MyDappsListAdapter extends RecyclerView.Adapter<MyDappsListAdapter.ViewHolder> {
    private List<DApp> data;
    private OnDappClickListener listener;
    private OnDappRemovedListener onDappRemovedListener;
    private OnDappEditedListener onDappEditedListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, url, remove, edit;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            remove = itemView.findViewById(R.id.remove);
            edit = itemView.findViewById(R.id.edit);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            url = itemView.findViewById(R.id.url);
        }
    }

    public MyDappsListAdapter(List<DApp> data,
                              OnDappClickListener listener,
                              OnDappRemovedListener onDappRemovedListener,
                              OnDappEditedListener onDappEditedListener) {
        this.data = data;
        this.listener = listener;
        this.onDappRemovedListener = onDappRemovedListener;
        this.onDappEditedListener = onDappEditedListener;
    }

    public void setDapps(List<DApp> dapps) {
        this.data = dapps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyDappsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_my_dapp_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDappsListAdapter.ViewHolder viewHolder, int i) {
        DApp dApp = data.get(i);
        String visibleUrl = null;
        try {
            visibleUrl = Utils.getDomainName(dApp.getUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        viewHolder.name.setText(dApp.getName());
        viewHolder.url.setText(visibleUrl);

        String favicon;
        if (visibleUrl != null) {
            favicon = DappBrowserUtils.getIconUrl(visibleUrl);
            Glide.with(viewHolder.icon.getContext())
                    .load(favicon)
                    .apply(new RequestOptions().circleCrop())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_logo))
                    .into(viewHolder.icon);

            viewHolder.icon.setOnClickListener(v -> {
                listener.onDappClick(dApp);
            });
        }

        viewHolder.remove.setOnClickListener(v -> onDappRemovedListener.onDappRemoved(dApp));

        viewHolder.edit.setOnClickListener(v -> onDappEditedListener.onDappEdited(dApp));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
