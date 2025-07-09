package org.slavik.connector;

public class SftpClientProperties {
    private final int port;
    private final String host;
    private final String userName;
    private int timeout;

    public SftpClientProperties(String userName, int port, String host, int timeout) {
        this.port = port;
        this.host = host;
        this.userName = userName;
        this.timeout = timeout;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getUserName() {
        return userName;
    }

    public int getTimeout() {
        return timeout;
    }
}
