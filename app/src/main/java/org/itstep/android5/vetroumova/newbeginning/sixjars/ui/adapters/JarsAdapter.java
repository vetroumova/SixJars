package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.app.Prefs;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.subjects.PublishSubject;

//import com.bumptech.glide.Glide;

public class JarsAdapter extends RealmRecyclerViewAdapter<Jar> {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;
    private List<Integer> percentJars;

    private final PublishSubject<Jar> jarInAdapterPublishSubject = PublishSubject.create();



    public JarsAdapter(Context context) {

        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item__recycler, parent, false);
        return new CardViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmManager.getInstance().getRealm();
        //TODO CHECK
        percentJars = Prefs.with(context).getPercentage();

        // get the article
        final Jar jar = getItem(position);
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        // set the title and the snippet
        holder.textID.setText(jar.getJar_id());
        holder.textName.setText(jar.getJar_name());
        String total = String.format(context.getString(R.string.item_balance_text), jar.getTotalCash());
        holder.textTotal.setText(total);
        //TODO sets percentage from Prefs
        //holder.textPercentage.setText(context.getString(R.string.item_percentage_text, 0));
        holder.textPercentage.setText(context.getString(R.string.item_percentage_text, percentJars.get(position)));
        //holder.textDescription.setText(jar.getJar_info());

        /*// load the background image
        if (book.getImageUrl() != null) {
            Glide.with(context)
                    .load(book.getImageUrl().replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(holder.imageBackground);
        }*/
        int fullness = (int) jar.getTotalCash();
        if (fullness > 0) {
            holder.imageJar.setImageResource(R.drawable.jar_with_water);
        } else {
            holder.imageJar.setImageResource(R.drawable.jar);
        }

        //remove single match from realm
        //TODO make a swipe delete
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                RealmResults<Jar> results = realm.where(Jar.class).findAll();

                // Get the book title to show it in toast message
                Jar jarItem = results.get(position);
                String title = jarItem.getJar_id() + " " + jarItem.getJar_name();

                // All changes to data must happen in a transaction
                realm.beginTransaction();

                // remove single match
                results.remove(position);
                realm.commitTransaction();

                if (results.size() == 0) {
                    Prefs.with(context).setPreLoad(false);
                }

                notifyDataSetChanged();

                Toast.makeText(context, title + " is removed from Realm", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //update single match from realm
        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //int itemJarPosition = position;

                jarInAdapterPublishSubject.onNext(jar);

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
        //public TextView textDescription;
        public ImageView imageJar;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
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
