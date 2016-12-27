package com.vetroumova.sixjars.ui.fragments;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.database.RealmManager;
import com.vetroumova.sixjars.model.Cashflow;
import com.vetroumova.sixjars.model.Jar;
import com.vetroumova.sixjars.ui.adapters.CashflowsInJarAdapter;
import com.vetroumova.sixjars.ui.adapters.RealmCashflowsAdapter;
import com.vetroumova.sixjars.utils.BottleDrawableManager;
import com.vetroumova.sixjars.utils.DebugLogger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;


public class JarInfoFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_JAR_ID = "ID";
    private static final int[] IDS = {R.string.db_jar_id_NEC, R.string.db_jar_id_PLAY, R.string.db_jar_id_GIVE,
            R.string.db_jar_id_EDU, R.string.db_jar_id_LTSS, R.string.db_jar_id_FFA};
    private static final int[] NAMES = {R.string.db_jar_name_NEC, R.string.db_jar_name_PLAY, R.string.db_jar_name_GIVE,
            R.string.db_jar_name_EDU, R.string.db_jar_name_LTSS, R.string.db_jar_name_FFA};
    private static final int[] DESCRIPTIONS = {R.string.db_jar_info_NEC, R.string.db_jar_info_PLAY, R.string.db_jar_info_GIVE,
            R.string.db_jar_info_EDU, R.string.db_jar_info_LTSS, R.string.db_jar_info_FFA};
    String jarIDString;
    ImageView jarImage;
    TextView jarID;
    TextView jarName;
    TextView jarBalance;
    TextView jarPercentage;
    TextView jarDescription;
    EditText spendCashEdit;
    Button spendCashSave;

    private Jar jar;
    private int percent;
    private float spendSum = 0; // to Bundle
    private Realm realm;
    private RealmChangeListener realmListener;

    private CashflowsInJarAdapter cashflowsInJarAdapter;
    private LayoutInflater inflater;
    private RecyclerView realmRecycler;
    private TextView noCashTextView;

    private PublishSubject<Cashflow> cashInRecyclerPublishSubject = PublishSubject.create();
    private PublishSubject<Long> cashDeletedPublishSubject = PublishSubject.create();
    private PublishSubject<Jar> spendCashPublishSubject = PublishSubject.create();
    private CompositeSubscription recyclerCashSubscriptions;

    public JarInfoFragment() {
        // Required empty public constructor
    }

    public static JarInfoFragment newInstance(String jarID) {
        JarInfoFragment fragment = new JarInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JAR_ID, jarID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jarIDString = getArguments().getString(ARG_JAR_ID);
        }
        if (savedInstanceState != null) {
            jarIDString = savedInstanceState.getString(ARG_JAR_ID);
        }

        recyclerCashSubscriptions = new CompositeSubscription();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jar_info, container, false);

        jarImage = (ImageView) view.findViewById(R.id.itemFragmentImage);
        jarID = (TextView) view.findViewById(R.id.itemFragmentIdText);
        jarName = (TextView) view.findViewById(R.id.itemFragmentNameText);
        jarBalance = (TextView) view.findViewById(R.id.itemFragmentBalanceText);
        jarPercentage = (TextView) view.findViewById(R.id.itemFragmentPercentageText);
        jarDescription = (TextView) view.findViewById(R.id.itemFragmentDescriptionText);
        noCashTextView = (TextView) view.findViewById(R.id.noCashTextView);

        spendCashEdit = (EditText) view.findViewById(R.id.itemCashSpendEdit);
        spendCashSave = (Button) view.findViewById(R.id.itemCashSpendButton);

        // get all Object with ID
        jar = RealmManager.with(this).getJar(jarIDString);

        jarID.setText(jar.getJar_id());
        int nameResourceNumber = NAMES[(int) jar.getJar_float_id()];
        jarName.setText(getString(R.string.item_name_in_fragment_text, getString(nameResourceNumber)));
        //Set the text
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        DecimalFormat f = new DecimalFormat("##,##0.00", s);
        jarBalance.setText(getString(R.string.item_balance_text, f.format(jar.getTotalCash())));
        percent = Prefs.with(getContext()).getPercentJar(jar.getJar_id());
        jarPercentage.setText(getString(R.string.item_percentage_text, percent));
        int descResourceNumber = DESCRIPTIONS[(int) jar.getJar_float_id()];
        jarDescription.setText(getString(descResourceNumber));

        spendCashEdit.setText("");
        spendCashSave.setOnClickListener(this);


        realmRecycler = (RecyclerView) view.findViewById(R.id.cashRecycler);
        setupRecycler();

        realm = RealmManager.with(this).getRealm();
        refreshData();
        Subscription cashClickSubscription = cashflowsInJarAdapter.getPositionCashClicks()
                .subscribe(cash -> {
                            DebugLogger.log("recycler cash info: " + cash.getId() + ", "
                                    + cash.getDate() + ", " + cash.getSum());
                            Log.d("VOlga", "recycler cash info: " + cash.getId() + ", "
                                    + cash.getDate() + ", " + cash.getSum());
                            cashInRecyclerPublishSubject.onNext(cash);
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        Subscription cashDeleteSubscription = cashflowsInJarAdapter.getPositionCashDeletes()
                .subscribe(deletedCashID -> {
                            DebugLogger.log("deleted : " + deletedCashID);
                            Log.d("VOlga", "deleted : " + deletedCashID);
                            /*Toast.makeText(getContext(), "deleted : " + deletedCashID,
                                    Toast.LENGTH_SHORT).show();*/
                            refreshData();
                            cashDeletedPublishSubject.onNext(deletedCashID);
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        recyclerCashSubscriptions.add(cashClickSubscription);
        recyclerCashSubscriptions.add(cashDeleteSubscription);
        return view;
    }

    public void refreshData() {
        jar = RealmManager.with(this).getJar(jarIDString);
        RealmResults<Cashflow> cashflowRealmResults =
                RealmManager.with(this).getCashflowInJar(jarIDString);
        setRealmCashAdapter(cashflowRealmResults);
        Log.d("VOlga", "reSum : " + jar.getTotalCash());
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        DecimalFormat f = new DecimalFormat("##,##0.00", s);
        jarBalance.setText(getString(R.string.item_balance_text, f.format(jar.getTotalCash())));
        jarImage.setImageResource(BottleDrawableManager.setAnimationJar
                (Prefs.with(getContext()), jarIDString));
        AnimationDrawable animation = (AnimationDrawable) jarImage.getDrawable();
        //Управлять объектом AnimationDrawable можно через методы start() и stop().
        animation.start();

    }

    private void setupRecycler() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        realmRecycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        cashflowsInJarAdapter = new CashflowsInJarAdapter(getContext());
        realmRecycler.setAdapter(cashflowsInJarAdapter);
    }


    public void setRealmCashAdapter(RealmResults<Cashflow> cashflows) {
        RealmCashflowsAdapter realmAdapter = new RealmCashflowsAdapter(getContext(), cashflows, true);
        // Set the data and tell the RecyclerView to draw
        cashflowsInJarAdapter.setRealmAdapter(realmAdapter);
        cashflowsInJarAdapter.notifyDataSetChanged();
        noCashTextView.setVisibility(cashflows.isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }

    public void setJarID(String jarID) {
        this.jarIDString = jarID;
    }


    public Observable<Cashflow> getCashflowItem() {
        if (cashInRecyclerPublishSubject.asObservable() == null) {
            cashInRecyclerPublishSubject.onNext(new Cashflow());
        }
        return cashInRecyclerPublishSubject.asObservable();
    }

    public Observable<Long> refreshRecyclerArterDeleteItem() {
        return cashDeletedPublishSubject.asObservable();
    }

    public Observable<Jar> spendCashInJar() {
        return spendCashPublishSubject.asObservable();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //TODO CHECK
        recyclerCashSubscriptions.clear();
    }

    @Override
    public void onClick(View v) {
        spendCashPublishSubject.onNext(jar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_JAR_ID, jarIDString);
    }

}
