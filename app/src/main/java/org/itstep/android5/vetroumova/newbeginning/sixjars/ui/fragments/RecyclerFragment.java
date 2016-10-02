package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.itstep.android5.vetroumova.newbeginning.sixjars.R;
import org.itstep.android5.vetroumova.newbeginning.sixjars.app.Prefs;
import org.itstep.android5.vetroumova.newbeginning.sixjars.database.RealmManager;
import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters.JarsAdapter;
import org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters.RealmJarsAdapter;
import org.itstep.android5.vetroumova.newbeginning.sixjars.utils.DebugLogger;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecyclerFragment.OnRecyclerItemTouchListener} interface
 * to handle interaction events.
 * Use the {@link RecyclerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //REALM
    private JarsAdapter realmJarsAdapter;
    private Realm realm;
    private LayoutInflater inflater;
    private RecyclerView realmRecycler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnRecyclerItemTouchListener mListener;

    private PublishSubject<Jar> jarInRecyclerPublishSubject = PublishSubject.create();
    private CompositeSubscription recyclerSubscriptions;


    public RecyclerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecyclerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecyclerFragment newInstance(String param1, String param2) {
        RecyclerFragment fragment = new RecyclerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //get realm instance
        this.realm = RealmManager.with(this).getRealm();

        recyclerSubscriptions = new CompositeSubscription();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler,
                container, false);

        realmRecycler = (RecyclerView) view.findViewById(R.id.mainRecycler);

        setupRecycler();

        //TODO get back Prefs
        if (!Prefs.with(getContext()).getPreLoad()) {
            setRealmData();
        }
        //setRealmData();

        // refresh the realm instance
        //RealmManager.with(this).refresh();

        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmJarsAdapter(RealmManager.with(this).getJars());

        /*Snackbar.make(contentLayout, "Press card item for edit, long press to remove item",
                Snackbar.LENGTH_SHORT).show();*/

        Subscription jarInAdapterSubscription = realmJarsAdapter.getPositionClicks()
                .subscribe(jar -> {
                            DebugLogger.log("recycler JAR info: " + jar.getJar_id());
                            Toast.makeText(getContext(), "recycler JAR info: " + jar.getJar_id(),
                                    Toast.LENGTH_SHORT).show();

                            jarInRecyclerPublishSubject.onNext(jar);
                        },
                        error -> DebugLogger.log(error.getMessage())
                );

        recyclerSubscriptions.add(jarInAdapterSubscription);

        return view;
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        realmRecycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        realmRecycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        //realmJarsAdapter = new JarsAdapter(this);
        realmJarsAdapter = new JarsAdapter(getContext());
        realmRecycler.setAdapter(realmJarsAdapter);
    }

    public void setRealmJarsAdapter(RealmResults<Jar> jars) {

        //RealmJarsAdapter realmAdapter = new RealmJarsAdapter(this.getApplicationContext(), jars, true);
        RealmJarsAdapter realmAdapter = new RealmJarsAdapter(getContext(), jars, true);
        // Set the data and tell the RecyclerView to draw
        realmJarsAdapter.setRealmAdapter(realmAdapter);
        realmJarsAdapter.notifyDataSetChanged();
    }


    private void setRealmData() {

        //RealmManager.initialiseJars(getApplicationContext());
        RealmManager.initialiseJars(getContext());

        //TODO CHECK - WAS with(this) in activity
        Prefs.with(getContext()).setPreLoad(true);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onRecyclerItemTouch(view);
        }
    }

    public Observable<Jar> getJar() {
        return jarInRecyclerPublishSubject.asObservable();
    }

    /*public JarsAdapter getRealmJarsAdapter() {
        return realmJarsAdapter;
    }*/

    public void refreshRecycler() {
        realmJarsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //TODO CHECK
        //realm.close();
        recyclerSubscriptions.clear();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecyclerItemTouchListener {
        // TODO: Update argument type and name
        void onRecyclerItemTouch(View view);
    }

}