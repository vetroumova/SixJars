package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters;

import android.content.Context;

import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Cashflow;

import io.realm.RealmResults;

public class RealmCashflowsAdapter extends RealmModelAdapter<Cashflow> {

    public RealmCashflowsAdapter(Context context, RealmResults<Cashflow> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}