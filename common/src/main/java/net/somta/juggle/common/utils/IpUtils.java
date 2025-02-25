package net.somta.juggle.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {

    public static String getLocalIp() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
