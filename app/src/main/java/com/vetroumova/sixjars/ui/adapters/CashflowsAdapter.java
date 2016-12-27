package com.vetroumova.sixjars.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.database.RealmManager;
import com.vetroumova.sixjars.model.Cashflow;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.subjects.PublishSubject;

//import com.bumptech.glide.Glide;

public class CashflowsAdapter extends RealmRecyclerViewAdapter<Cashflow> {

    final Context context;
    private final PublishSubject<Cashflow> cashflowInAdapterPublishSubject = PublishSubject.create();
    private Realm realm;
    private LayoutInflater inflater;
    private DecimalFormatSymbols s = new DecimalFormatSymbols();
    private DecimalFormat f = new DecimalFormat("##,##0.00", s);


    public CashflowsAdapter(Context context) {

        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CashCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cash_recycler, parent, false);
        return new CashCardViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmManager.getInstance().getRealm();
        // get the article
        final Cashflow cashflow = getItem(position);
        // cast the generic view holder to our specific one
        final CashCardViewHolder holder = (CashCardViewHolder) viewHolder;

        // set the title and the snippet
        holder.textCashID.setText(String.valueOf(cashflow.getId()));
        holder.textCashDate.setText(String.valueOf(cashflow.getDate()));
        holder.textCashSum.setText(context.getString(R.string.item_balance_text,
                f.format(cashflow.getSum())));
        holder.textCashJar.setText(cashflow.getJar().getJar_id());
        holder.textCashPercentage.setText(context.getString(R.string.item_percentage_text,
                cashflow.getCurrpercent()));

        //remove single match from realm
        //TODO make a swipe delete
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                RealmResults<Cashflow> results = realm.where(Cashflow.class).findAll();

                // Get the book title to show it in toast message
                Cashflow cashflowItem = results.get(position);
                String title = context.getString(R.string.removed_cashflow_text)
                        + (cashflowItem.getDate() + " " + cashflowItem.getSum());

                // All changes to data must happen in a transaction
                realm.beginTransaction();

                // remove single match
                results.remove(position);
                realm.commitTransaction();
                notifyDataSetChanged();

                Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //update single match from realm
        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cashflowInAdapterPublishSubject.onNext(cashflow);
            }
        });
    }

    public Observable<Cashflow> getPositionCashClicks() {
        return cashflowInAdapterPublishSubject.asObservable();
    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CashCardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView textCashID;
        public TextView textCashDate;
        public TextView textCashSum;
        public TextView textCashJar;
        public TextView textCashPercentage;

        public CashCardViewHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.itemCardCashflow);
            textCashID = (TextView) itemView.findViewById(R.id.itemCashIdText);
            textCashDate = (TextView) itemView.findViewById(R.id.itemCashDateText);
            textCashSum = (TextView) itemView.findViewById(R.id.itemCashSumText);
            textCashJar = (TextView) itemView.findViewById(R.id.itemCashJarText);
            textCashPercentage = (TextView) itemView.findViewById(R.id.itemCashPercentageText);
        }
    }
}
