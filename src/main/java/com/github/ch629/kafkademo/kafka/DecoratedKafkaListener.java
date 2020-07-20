package com.github.ch629.kafkademo.kafka;

import com.github.ch629.kafkademo.domain.core.kafka.ListenerStatus;
import org.springframework.kafka.listener.MessageListenerContainer;

public class DecoratedKafkaListener {
    private final MessageListenerContainer container;
    private ListenerStatus listenerStatus = ListenerStatus.RUNNING;

    public DecoratedKafkaListener(final MessageListenerContainer container) {
        this.container = container;
    }

    // NOTE: When pausing, it'll pause for the next poll, meaning this batch will still be processed
    public boolean pause(final boolean force) {
        if (!force && listenerStatus == ListenerStatus.FORCED_PAUSE) {
            return false;
        }

        container.pause();
        listenerStatus = force ? ListenerStatus.FORCED_PAUSE : ListenerStatus.AUTOMATED_PAUSE;
        return true;
    }

    public boolean resume(final boolean force) {
        if (force || listenerStatus == ListenerStatus.AUTOMATED_PAUSE) {
            container.resume();
            listenerStatus = ListenerStatus.RUNNING;
            return true;
        }
        return false;
    }

    public String getListenerId() {
        return container.getListenerId();
    }

    public String getGroupId() {
        return container.getGroupId();
    }

    public boolean isRunning() {
        return listenerStatus == ListenerStatus.RUNNING;
    }

    public boolean isContainerRunning() {
        return container.isRunning();
    }

    public ListenerStatus getStatus() {
        return listenerStatus;
    }
}
