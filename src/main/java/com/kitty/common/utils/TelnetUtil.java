package com.kitty.common.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TelnetUtil
{
    public TelnetUtil() {
    }
    
    public static boolean telnet(String hostname, int port, int timeout) {
        Socket socket = new Socket();
        boolean isConnected = false;
        try {
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            isConnected = socket.isConnected();
        }
        catch (IOException e) {
            System.out.println("false");
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e2) {
                System.out.println("false");
            }
        }
        return isConnected;
    }
}
