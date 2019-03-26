package com.lockdown.messaging.cluster.utils;

import com.lockdown.messaging.cluster.exception.MessagingHostException;
import io.netty.channel.ChannelHandlerContext;

import java.net.*;
import java.util.Enumeration;
import java.util.regex.Pattern;


public class IPUtils {


    public static boolean isLocalPort(ChannelHandlerContext ctx, Pattern nodeWhiteList) {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return nodeWhiteList.matcher(String.valueOf(localAddress.getPort())).matches() ||
                nodeWhiteList.matcher(String.valueOf(remoteAddress.getPort())).matches();
    }


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
