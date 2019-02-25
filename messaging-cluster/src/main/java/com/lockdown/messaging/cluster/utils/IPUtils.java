package com.lockdown.messaging.cluster.utils;

import com.lockdown.messaging.cluster.exception.MessagingHostException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


public class IPUtils {


    public static String getLocalIP() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.startsWith("windows")) {
            try {
                return getWindowsIp();
            } catch (UnknownHostException e) {
                throw new MessagingHostException(e);
            }
        } else if (os.startsWith("linux")) {
            try {
                return getLinuxLocalIp();
            } catch (SocketException e) {
                throw new MessagingHostException(e);
            }
        } else {
            throw new MessagingHostException("不支持当前操作系统 [" + os + "]");
        }

    }

    private static String getWindowsIp() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostAddress();
    }


    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                                System.out.println(ipaddress);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        System.out.println("IP:" + ip);
        return ip;
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(" address :" + IPUtils.getLocalIP());
    }

}
