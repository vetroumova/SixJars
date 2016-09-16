package org.itstep.android5.vetroumova.newbeginning.sixjars.ui.adapters;

import android.content.Context;

import org.itstep.android5.vetroumova.newbeginning.sixjars.model.Jar;

import io.realm.RealmResults;

public class RealmJarsAdapter extends RealmModelAdapter<Jar> {

    public RealmJarsAdapter(Context context, RealmResults<Jar> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}