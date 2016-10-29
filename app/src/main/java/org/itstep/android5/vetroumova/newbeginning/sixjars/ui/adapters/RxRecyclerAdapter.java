package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OLGA on 31.08.2016.
 */
public class RxRecyclerAdapter extends RecyclerView.Adapter<RxRecyclerAdapter.RxViewHolder> {

    private List<String> items = new ArrayList<>();

    public void addAll(List<String> moreItems) {
        //TODO list of objects with jars
        items.addAll(moreItems);
        notifyDataSetChanged();
    }

    @Override
    public RxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jar_recycler, parent, false);
        RxViewHolder vh = new RxViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RxViewHolder holder, int position) {
        String item = items.get(position);
        holder.jar.setImageResource(R.drawable.water_to_jar_empty);
        holder.name.setText(R.string.item_name_of_jar);
        holder.balance.setText(R.string.item_balance_text);
        //holder.description.setText(R.string.item_description_text);
        holder.percentage.setText(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class RxViewHolder extends RecyclerView.ViewHolder {

        ImageView jar;
        TextView name;
        TextView balance;
        TextView percentage;

        //TextView description;
        public RxViewHolder(View itemView) {
            super(itemView);
            jar = (ImageView) itemView.findViewById(R.id.itemImage);
            name = (TextView) itemView.findViewById(R.id.itemNameText);
            balance = (TextView) itemView.findViewById(R.id.itemBalanceText);
            percentage = (TextView) itemView.findViewById(R.id.itemPercentageText);
        }
    }
}
