package com.xuen.metrics;


import static com.xuen.metrics.ItemValue.percentilesIndex;
import static com.xuen.metrics.ItemValue.value;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.google.common.primitives.Ints;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * Timer 计时的实现原理是使用数组存储一定数量的值, 然后对数组值聚合求结果
 * 每次计算完以后会清空数组值, 所以一个 timer 在同一个时间内不能做两次聚合
 * 为了达到 timer 复用的目的, 需要对 timer 的值数组做结果缓存
 * </pre>
 */
public class ResettableTimer implements Metric, Delta {
    static final int DEFAULT_TIMER_SIZE = 8000;
    public static final double P75 = 75.0;
    public static final double P98 = 98.0;
    public static final double P50 = 50.0;
    public static final double P99 = 99.0;
    public static final double P99_5 = 99.5;
    private static final double[] DEFAULT_PER = new double[] { P75, P98 };
    private final Meter meter;
    private final DeltaCounter counter;
    private final StatsBuffer timer;
    private volatile TimerSnapshot snapshot;

    public ResettableTimer() {
        this(Clock.defaultClock(), DEFAULT_PER, DEFAULT_TIMER_SIZE);
    }

    public ResettableTimer(double[] percentiles, int timersize) {
        this(Clock.defaultClock(), percentiles, timersize);
    }

    public ResettableTimer(Clock clock, double[] percentiles, int timersize) {
        this.meter = new Meter(clock);
        this.timer = new StatsBuffer(timersize, percentiles);
        this.counter = new DeltaCounter(false);
    }

    public void update(long el, TimeUnit timeUnit) {
        meter.mark();
        counter.inc();
        long time = timeUnit.toMillis(el);
        try {
            timer.record(Ints.checkedCast(time));
        } catch (IllegalArgumentException ignored) {

        }
    }

    public double getFifteenMinuteRate() {
        return meter.getFifteenMinuteRate();
    }

    public double getFiveMinuteRate() {
        return meter.getFiveMinuteRate();
    }

    public double getCount() {
        return counter.getCount();
    }

    public double getMeanRate() {
        return meter.getMeanRate();
    }

    public double getOneMinuteRate() {
        return meter.getOneMinuteRate();
    }

    Meter getMeter() {
        return meter;
    }

    @Override
    public void tick() {
        counter.tick();
    }

    public TimerSnapshot getSnapshot() {
        return snapshot;
    }

    void refreshTimeSnapshot() {
        TimerSnapshot snapshot = new TimerSnapshot();
        timer.computeStats();
        double[] percentileValues = timer.getPercentileValues();
        double[] percentiles = timer.getPercentiles();
        snapshot.setMean(value(timer.getMean()));
        snapshot.setStd(value(timer.getStdDev()));
        snapshot.setP75(value(percentileValues[percentilesIndex(ResettableTimer.P75, percentiles)]));
        snapshot.setP98(value(percentileValues[percentilesIndex(ResettableTimer.P98, percentiles)]));
        timer.reset();

        this.snapshot = snapshot;
    }

    static class TimerSnapshot {
        private float mean;
        private float std;
        private float p75;
        private float p98;

        public float getMean() {
            return mean;
        }

        public TimerSnapshot setMean(float mean) {
            this.mean = mean;
            return this;
        }

        public float getStd() {
            return std;
        }

        public TimerSnapshot setStd(float std) {
            this.std = std;
            return this;
        }

        public float getP75() {
            return p75;
        }

        public TimerSnapshot setP75(float p75) {
            this.p75 = p75;
            return this;
        }

        public float getP98() {
            return p98;
        }

        public TimerSnapshot setP98(float p98) {
            this.p98 = p98;
            return this;
        }
    }
}
