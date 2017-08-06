package com.xuen.metrics;

import com.codahale.metrics.Metric;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zheng.xu
 * @since 2017-07-11
 */
public interface MetricsProcessor {

    void report(MetricsHolder metricsHolder, PrintWriter writer, String name);
}



