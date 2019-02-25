package com.lockdown.messaging.cluster.utils;
import java.net.InetAddress;
import java.net.UnknownHostException;




public class IPUtils {


    public static String getLocalIP() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostAddress();
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(" address :"+IPUtils.getLocalIP());
    }

}
