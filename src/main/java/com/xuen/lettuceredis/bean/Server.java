package com.xuen.lettuceredis.bean;

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
}
