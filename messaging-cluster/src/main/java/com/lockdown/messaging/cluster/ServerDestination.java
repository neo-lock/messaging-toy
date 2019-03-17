package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.utils.IPUtils;

import java.util.Objects;

public class ServerDestination implements Destination {


    private String address;
    private int port;

    public ServerDestination() {
    }

    public ServerDestination(String address, int port) {
        this.address = convertLocal(address);
        this.port = port;
    }



    @Override
    public String identifier() {
        return toString();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = convertLocal(address);
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
                "address='" + address + '\'' +
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

    private String convertLocal(String address){
        if(address.equals("127.0.0.1")||address.equals("localhost")){
            return IPUtils.getLocalIP();
        }
        return address;
    }


    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }
}
