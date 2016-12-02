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
import java.text.SimpleDateFormat;

import io.realm.Realm;
import rx.Observable;
import rx.subjects.PublishSubject;

//import com.bumptech.glide.Glide;

public class CashflowsInJarAdapter extends RealmRecyclerViewAdapter<Cashflow> {

    final Context context;
    private final PublishSubject<Cashflow> cashflowInAdapterPublishSubject = PublishSubject.create();
    private final PublishSubject<Long> cashflowDeletePublishSubject = PublishSubject.create();
    //private List<Integer> percentJars;
    private Realm realm;
    private LayoutInflater inflater;


    public CashflowsInJarAdapter(Context context) {

        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CashCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cash_in_jar_recycler, parent, false);
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
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        String formatted = simpleDateFormat.format(cashflow.getDate());
        holder.textCashDate.setText(formatted);
        //Set the text
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("##,##0.00", s);
        String sum = (f.format(cashflow.getSum()));
        String total = String.format(context.getString(R.string.item_balance_text), sum);
        holder.textCashSum.setText(total);

        //remove single match from realm
        //TODO make a swipe delete
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                long cashID = cashflow.getId();

                if (!RealmManager.getInstance().deleteCashflow(cashID)) {
                    Toast.makeText(context, cashID + " NOT removed from Realm, not enough money",
                            Toast.LENGTH_SHORT).show();
                } else {
                    notifyDataSetChanged();
                    cashflowDeletePublishSubject.onNext(cashID);
                    Toast.makeText(context, cashID + " is removed from Realm",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        //update single match from realm
        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cashflowInAdapterPublishSubject.onNext(cashflow);

                /*inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editID = (EditText) content.findViewById(R.id.id_edit);
                final EditText editName = (EditText) content.findViewById(R.id.name_edit);
                final EditText editThumbnail = (EditText) content.findViewById(R.id.thumbnail_edit);

                editID.setText(jar.getJar_id());
                editName.setText(jar.getJar_name());
                //TODO
                editThumbnail.setText(jar.getJar_info());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Edit Jar")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                RealmResults<Jar> results = realm.where(Jar.class).findAll();

                                realm.beginTransaction();
                                results.get(position).setJar_id(editID.getText().toString());
                                results.get(position).setJar_name(editName.getText().toString());
                                results.get(position).setJar_info(editThumbnail.getText().toString());

                                realm.commitTransaction();

                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();*/
            }
        });
    }


    public Observable<Cashflow> getPositionCashClicks() {
        return cashflowInAdapterPublishSubject.asObservable();
    }

    public Observable<Long> getPositionCashDeletes() {
        return cashflowDeletePublishSubject.asObservable();
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
        public TextView textCashDate;
        public TextView textCashSum;

        public CashCardViewHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.itemCardCashflow);
            textCashDate = (TextView) itemView.findViewById(R.id.itemCashDateText);
            textCashSum = (TextView) itemView.findViewById(R.id.itemCashSumText);
        }
    }
}
