package com.xuen.metrics;

import com.codahale.metrics.Clock;

import java.util.concurrent.TimeUnit;

/**
 */
public class ResettableTimerAdapter implements Timer {
    private final ResettableTimer record;

    public ResettableTimerAdapter(ResettableTimer resettableTimer) {
        this.record = resettableTimer;
    }

    @Override
    public void update(long duration, TimeUnit unit) {
        record.update(duration, unit);
    }

    @Override
    public Context time() {
        return new ResettableTimerContext(record, Clock.defaultClock());
    }

    private class ResettableTimerContext implements Context {

        private final ResettableTimer timer;
        private final Clock clock;
        private final long startTime;

        private ResettableTimerContext(ResettableTimer timer, Clock clock) {
            this.timer = timer;
            this.clock = clock;
            this.startTime = System.currentTimeMillis();
        }

        public long stop() {
            final long elapsed = System.currentTimeMillis() - startTime;
            timer.update(elapsed, TimeUnit.MILLISECONDS);
            return elapsed;
        }


        public void close() {
            stop();
        }
    }
}
