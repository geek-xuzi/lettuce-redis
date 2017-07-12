package com.xuen.lettuceredis.bean;

import com.codahale.metrics.Metric;
import com.xuen.metrics.MetricKey;
import com.xuen.metrics.Metrics;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import metrics_influxdb.InfluxdbHttp;
import metrics_influxdb.InfluxdbReporter;


/**
 * @author zheng.xu
 * @since 2017-05-11
 */
public class Server {

    private String host;
    private int port;

    public static Server create(String host, int port) {
        Server server = new Server();
        server.setHost(host)
                .setPort(port);
        return server;

    }

    public String getHost() {
        return host;
    }

    public Server setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Server setPort(int port) {
        this.port = port;
        return this;
    }

    public static void main(String [] args){
        Metrics.timer("sada").tag("a","a").get().update(200, TimeUnit.MILLISECONDS);
        Metrics.timer("sada").tag("a","b").get().update(200, TimeUnit.MILLISECONDS);
        Metrics.timer("sada").tag("a","c").get().update(200, TimeUnit.MILLISECONDS);
        Map<MetricKey, Metric> metrics = Metrics.INSTANCE.getHolder().getMetrics();
        for (Map.Entry<MetricKey, Metric> metric: metrics.entrySet()){
            Metric value = metric.getValue();
        }
        System.out.println(metrics);

    }

}
