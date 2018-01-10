package com.networknt.subscription.schema;

import com.networknt.subscription.schema.models.MessageAddedEvent;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;


/**
 * Created by Nicholas Azar on October 18, 2017.
 */
public class ChannelPublisher {

    final Flowable<MessageAddedEvent> publisher;
    ObservableEmitter<MessageAddedEvent> emitter;

    public ChannelPublisher() {
        Observable<MessageAddedEvent> messageAddedEventObservable = Observable.create(messageAddedEventObservableEmitter -> {
            this.emitter = messageAddedEventObservableEmitter;
        });

        ConnectableObservable<MessageAddedEvent> connectableObservable = messageAddedEventObservable.share().publish();
        connectableObservable.connect();
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void onMessageAdded(MessageAddedEvent messageAddedEvent) {
        this.emitter.onNext(messageAddedEvent);
    }

    public Flowable<MessageAddedEvent> getPublisher(int channelId) {
        return publisher;
    }
}
