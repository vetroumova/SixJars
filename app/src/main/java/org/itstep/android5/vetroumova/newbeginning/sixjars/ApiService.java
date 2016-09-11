package org.itstep.android5.vetroumova.newbeginning.sixjars;


import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by OLGA on 31.08.2016.
 */
public class ApiService {

    public static Observable<List<String>> paginatedThings(final Observable<Void> onNextObservable) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(final Subscriber<? super List<String>> subscriber) {

                onNextObservable.subscribe(new Observer<Void>() {
                    int latestPage = -1;

                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        latestPage++;
                        List<String> pageItems = new ArrayList<String>();
                        for (int i = 0; i < 10; i++) {
                            pageItems.add("page " + latestPage + " item " + i);
                        }
                        subscriber.onNext(pageItems);
                    }
                });
            }
        });
    }
}
