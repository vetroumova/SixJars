package com.vetroumova.sixjars.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vetroumova.sixjars.R;
import com.vetroumova.sixjars.app.Prefs;
import com.vetroumova.sixjars.database.RealmManager;
import com.vetroumova.sixjars.model.Jar;
import com.vetroumova.sixjars.ui.adapters.JarsAdapter;
import com.vetroumova.sixjars.ui.adapters.RealmJarsAdapter;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class RecyclerFragment extends Fragment {
    private JarsAdapter realmJarsAdapter;
    private Realm realm;
    private LayoutInflater inflater;
    private RecyclerView realmRecycler;
    private PublishSubject<Jar> jarInRecyclerPublishSubject = PublishSubject.create();
    private CompositeSubscription recyclerSubscriptions;

    public RecyclerFragment() {
        // Required empty public constructor
    }

    public static RecyclerFragment newInstance() {
        RecyclerFragment fragment = new RecyclerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get realm instance
        this.realm = RealmManager.with(this).getRealm();
        recyclerSubscriptions = new CompositeSubscription();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler,
                container, false);
        realmRecycler = (RecyclerView) view.findViewById(R.id.mainRecycler);
        setupRecycler();
        if (!Prefs.with(getContext()).getPreLoad()) {
            setRealmData();
        }
        setRealmJarsAdapter(RealmManager.with(this).getJars());
        Subscription jarInAdapterSubscription = realmJarsAdapter.getPositionClicks()
                .subscribe(jar -> {
                            Log.d("VOlga", "recycler JAR info: " + jar.getJar_id());
                            jarInRecyclerPublishSubject.onNext(jar);
                        },
                        error -> Log.d("VOlga", error.getMessage())
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
        RealmJarsAdapter realmAdapter = new RealmJarsAdapter(getContext(), jars, true);
        // Set the data and tell the RecyclerView to draw
        realmJarsAdapter.setRealmAdapter(realmAdapter);
        realmJarsAdapter.notifyDataSetChanged();
    }

    private void setRealmData() {
        RealmManager.initialiseJars(getContext());
        Prefs.with(getContext()).setPreLoad(true);
    }

    public Observable<Jar> getJar() {
        return jarInRecyclerPublishSubject.asObservable();
    }

    public void refreshRecycler() {
        realmJarsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recyclerSubscriptions.clear();
    }
}
