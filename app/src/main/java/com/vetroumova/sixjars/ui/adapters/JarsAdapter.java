package com.vetroumova.sixjars.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.database.RealmManager;
import com.vetroumova.sixjars.model.Jar;
import com.vetroumova.sixjars.utils.BottleDrawableManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import io.realm.RealmResults;
import rx.Observable;
import rx.subjects.PublishSubject;

public class JarsAdapter extends RealmRecyclerViewAdapter<Jar> {

    private static final int[] NAMES = {R.string.db_jar_name_NEC, R.string.db_jar_name_PLAY, R.string.db_jar_name_GIVE,
            R.string.db_jar_name_EDU, R.string.db_jar_name_LTSS, R.string.db_jar_name_FFA};
    final Context context;
    private final PublishSubject<Jar> jarInAdapterPublishSubject = PublishSubject.create();
    private LayoutInflater inflater;
    private List<Integer> percentJars;

    public JarsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jar_recycler,
                parent, false);
        // for correct locale name in appwidget
        RealmResults<Jar> jars = RealmManager.getInstance().getJars();
        //for localisation
        for (int i = 0; i < jars.size(); i++) {
            String nameForWidget = context.getString(NAMES[i]);
            RealmManager.getInstance().changeJarName(jars.get(i), nameForWidget);
        }
        return new CardViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        percentJars = Prefs.with(context).getPercentage();
        // get the article
        final Jar jar = getItem(position);
        final String jarID = jar.getJar_id();
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;
        // set the title and the snippet
        holder.textID.setText(jarID);
        String nameForWidget = context.getString(NAMES[position]);
        RealmManager.getInstance().changeJarName(jar, nameForWidget); // for correct locale name in appwidget
        holder.textName.setText(nameForWidget);
        //Set the text
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        DecimalFormat f = new DecimalFormat("##,##0.00", s);
        String sum = (f.format(jar.getTotalCash()));
        String total = String.format(context.getString(R.string.item_balance_text), sum);
        holder.textTotal.setText(total);
        holder.textPercentage.setText(context.getString(R.string.item_percentage_short_text,
                percentJars.get(position)));
        holder.textPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, context.getString(R.string.percentage_toast_text),
                        Toast.LENGTH_SHORT).show();
            }
        });
        holder.imageJar.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(context),
                jar.getJar_id()));

        //update single match from realm
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarInAdapterPublishSubject.onNext(jar);
            }
        });
    }

    public Observable<Jar> getPositionClicks() {
        return jarInAdapterPublishSubject.asObservable();
    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView textID;
        public TextView textName;
        public TextView textTotal;
        public TextView textPercentage;
        public ImageView imageJar;

        public CardViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.itemFragmentCardJars);
            textID = (TextView) itemView.findViewById(R.id.itemIdText);
            textName = (TextView) itemView.findViewById(R.id.itemNameText);
            textTotal = (TextView) itemView.findViewById(R.id.itemBalanceText);
            textPercentage = (TextView) itemView.findViewById(R.id.itemPercentageText);
            //textDescription = (TextView) itemView.findViewById(R.id.itemDescriptionText);
            imageJar = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }
}
