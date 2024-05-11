
package com.kitty.common.utils;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TelnetUtil
{
    public static boolean telnet(final String hostname, final int port, final int timeout) {
        final Socket socket = new Socket();
        boolean isConnected = false;
        try {
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            isConnected = socket.isConnected();
        }
        catch (final IOException e) {
            System.out.println("false");
            try {
                socket.close();
            }
            catch (final IOException ex) {
                System.out.println("false");
            }
        }
        finally {
            try {
                socket.close();
            }
            catch (final IOException e2) {
                System.out.println("false");
            }
        }
        return isConnected;
    }
}
