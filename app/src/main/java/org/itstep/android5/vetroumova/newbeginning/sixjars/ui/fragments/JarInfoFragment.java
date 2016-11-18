package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

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
import android.widget.Toast;

import org.itstep.android5.vetroumova.newbeginning.sixjars.BottleDrawableManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.app.Prefs;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Cashflow;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters.CashflowsInJarAdapter;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters.RealmCashflowsAdapter;
import org.itstep.android5.vetroumova.newbeginning.sixjars.utils.DebugLogger;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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


/*    private OnFragmentInteractionListener mListener;*/

    public JarInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JarInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JarInfoFragment newInstance(String param1, String param2, String jarID) {
        JarInfoFragment fragment = new JarInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        //jarImage.setImageResource(BottleDrawableManager.setDrawableJar(Prefs.with(getContext()), jarIDString));
        /*jarImage.setImageResource(BottleDrawableManager
                .setAnimationJar(Prefs.with(getContext()), jarIDString));

        AnimationDrawable animation = (AnimationDrawable) jarImage.getDrawable();

        //Управлять объектом AnimationDrawable можно через методы start() и stop().
        animation.start();
*/
        /*CountDownTimer countDownTimer = new CountDownTimer(3500, 500) {
            int i = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                // do something after 1s
                if (i < jars.size()) {
                    jars.get(i).start();
                    i++;
                    Log.d(TAG,"Tick " + i + " animation");
                }
            }
            @Override
            public void onFinish() {
                // do something end times 5s
                Log.d(TAG,"Finish animation");
            }
        }.start();*/

        jarID.setText(jar.getJar_id());
        int nameResourceNumber = NAMES[(int) jar.getJar_float_id()];
        jarName.setText(getString(R.string.item_name_in_fragment_text, getString(nameResourceNumber)));
        //Set the text
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
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

        /*//TODO get back Prefs
        if (!Prefs.with(getContext()).getPreLoad()) {
            setRealmData();
        }*/

        realm = RealmManager.with(this).getRealm();
        /*realmListener = new RealmChangeListener() {
            @Override
            public void onChange(Object element) {
                refreshData();
            }
        };
        realm.addChangeListener(realmListener);*/
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically

        //setRealmCashAdapter(RealmManager.with(this).getCashflowInJar(jarIDString));
        refreshData();

        /*Snackbar.make(contentLayout, "Press card item for edit, long press to remove item",
                Snackbar.LENGTH_SHORT).show();*/

        Subscription cashClickSubscription = cashflowsInJarAdapter.getPositionCashClicks()
                .subscribe(cash -> {
                            DebugLogger.log("recycler cash info: " + cash.getId() + ", "
                                    + cash.getDate() + ", " + cash.getSum());
                            Log.d("VOlga", "recycler cash info: " + cash.getId() + ", "
                                    + cash.getDate() + ", " + cash.getSum());
                            //Toast.makeText(getContext(), "recycler cash info: " + cash.getId() + ", "
                            //+ cash.getDate() + ", " + cash.getSum(),
                            //Toast.LENGTH_SHORT).show();

                            cashInRecyclerPublishSubject.onNext(cash);
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        Subscription cashDeleteSubscription = cashflowsInJarAdapter.getPositionCashDeletes()
                .subscribe(deletedCashID -> {
                            DebugLogger.log("deleted : " + deletedCashID);
                            Log.d("VOlga", "deleted : " + deletedCashID);
                            Toast.makeText(getContext(), "deleted : " + deletedCashID,
                                    Toast.LENGTH_SHORT).show();

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

        //noCashTextView.setVisibility(View.INVISIBLE);
        //TODO progressbardialog fragment
        /*ProgressDialog pDialog  = ProgressDialog.show(getContext(), null,
                null, true);
        pDialog.setCancelable(true);
        pDialog.setOnCancelListener(dialog -> {
            //do something with long work to cancel;
        });*/
        jar = RealmManager.with(this).getJar(jarIDString);
        //realm = RealmManager.with(this).getRealm();
        RealmResults<Cashflow> cashflowRealmResults =
                RealmManager.with(this).getCashflowInJar(jarIDString);
        /*cashflowRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Cashflow>>() {
            @Override
            public void onChange(RealmResults<Cashflow> element) {
                if (element.isLoaded() && element.isValid()) {
                    setRealmCashAdapter(cashflowRealmResults);
                    //jar = RealmManager.with(this).getJar(jarIDString);
                    //getFragmentManager().popBackStackImmediate();
                    //pDialog.dismiss();
                }
            }
        });*/

        setRealmCashAdapter(cashflowRealmResults);
        Log.d("VOlga", "reSum : " + jar.getTotalCash());
        //Toast.makeText(getContext(), "reSum : " + jar.getTotalCash(),
        //Toast.LENGTH_SHORT).show();
        //Set the text
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        //s.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("##,##0.00", s);
        jarBalance.setText(getString(R.string.item_balance_text, f.format(jar.getTotalCash())));

        //todo check in lower versions
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            jarImage.setImageResource(BottleDrawableManager.setAnimationJar
                    (Prefs.with(getContext()), jarIDString));
            AnimationDrawable animation = (AnimationDrawable) jarImage.getDrawable();
            //Управлять объектом AnimationDrawable можно через методы start() и stop().
            animation.start();
        } else {
            jarImage.setImageResource(BottleDrawableManager.setDrawableJar
                    (Prefs.with(getContext()), jarIDString));
        }*/
        jarImage.setImageResource(BottleDrawableManager.setAnimationJar
                (Prefs.with(getContext()), jarIDString));
        AnimationDrawable animation = (AnimationDrawable) jarImage.getDrawable();
        //Управлять объектом AnimationDrawable можно через методы start() и stop().
        animation.start();

    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //realmRecycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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

        /*for (Cashflow cashflow:cashflows) {
            Log.d("VOlga","Cash date " + cashflow.getDate() + ", sum " + cashflow.getSum());
            Toast.makeText(getContext(),"Cash date " + cashflow.getDate() + ", sum "
                    + cashflow.getSum(),Toast.LENGTH_SHORT).show();
        }*/

        noCashTextView.setVisibility(cashflows.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        //noCashTextView.setVisibility(View.INVISIBLE);
    }

    public void setJarID(String jarID) {
        this.jarIDString = jarID;
    }


    public Observable<Cashflow> getCashflowItem() {
        //todo check
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
        /*if (spendCashEdit.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.enter_sum_text, Toast.LENGTH_SHORT).show();
        } else {
            // add to one jar or to all
            spendSum = Float.parseFloat(spendCashEdit.getText().toString());
            //adding in DB
            boolean resultAdd = RealmManager.with(this).addCashToJar(jarIDString, -spendSum, percent,getString(R.string.new_income_text));
            Log.d("VOlga", "add to " + jarID + " new Cashflow "
                    + spendSum + " - " + resultAdd);
            Toast.makeText(getContext(), getString(resultAdd ? R.string.added_sum_text
                    : R.string.not_added_sum_text), Toast.LENGTH_SHORT).show();
            spendCashEdit.setText("");
            jarBalance.setText(getString(R.string.cash_balance_text, jar.getTotalCash()));
            jarImage.setImageResource(jar.getTotalCash() > 0 ? R.drawable.jar_with_water : R.drawable.jar);
            setRealmCashAdapter(RealmManager.with(this).getCashflowInJar(jarIDString));
            //TODO normal id
            long virtual = 0;
            cashDeletedPublishSubject.onNext(virtual);
        }*/

        //TODO CHECK JAR
        spendCashPublishSubject.onNext(jar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(ARG_JAR_ID, jarIDString);
    }

}
