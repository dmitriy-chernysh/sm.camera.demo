package com.mobiledevpro.smcamera;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Rx Bus
 * <p>
 * Created by Dmitriy V. Chernysh on 29.05.18.
 * <p>
 * https://instagr.am/mobiledevpro
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */
public class RxEventBus {
    private static RxEventBus sInstance;

    private PublishSubject<Object> subject = PublishSubject.create();

    public static RxEventBus getInstance() {
        if (sInstance == null) sInstance = new RxEventBus();
        return sInstance;
    }

    public void setEvent(Object object) {
        subject.onNext(object);
    }

    public Observable<Object> getEvents() {
        return subject;
    }
}
