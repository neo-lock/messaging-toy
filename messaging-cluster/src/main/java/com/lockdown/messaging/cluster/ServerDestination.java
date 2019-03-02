package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.framwork.Destination;

import java.util.Objects;

public class ServerDestination implements Destination {


    private String ipAddress;
    private int port;

    public ServerDestination() {
    }

    public ServerDestination(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public String identifier() {
        return toString();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerDestination{" +
                "ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerDestination that = (ServerDestination) o;
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, port);
    }
}
