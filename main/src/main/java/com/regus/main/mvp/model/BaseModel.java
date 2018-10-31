package com.regus.main.mvp.model;


import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 */

public class BaseModel {
    protected <T> Subscription toSubscribe(Observable<T> o, Subscriber<T> s) {
        Subscription subscription = o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);

        return subscription;
    }

}
