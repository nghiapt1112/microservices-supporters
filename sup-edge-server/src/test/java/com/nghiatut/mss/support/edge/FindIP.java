package com.nghiatut.mss.support.edge;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

import static java.lang.System.out;

public class FindIP {


    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {

        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {

            out.printf("InetAddress: %s\n", inetAddress);
        }
        out.printf("\n");
    }

    @Test
    public void way1() {

        try {
            InetAddress addr = InetAddress.getLocalHost();

            // Get IP Address
            byte[] ipAddr = addr.getAddress();

            // Get hostname
            String hostname = addr.getHostName();
            System.out.println(hostname);
        } catch (UnknownHostException e) {
        }
    }

    @Test
    public void way2() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            displayInterfaceInformation(netint);
    }

    @Test
    public void way3() {

        String systemIPAddress = "";
        try {
            URL url_name = new URL("http://bot.whatismyipaddress.com");
            BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));
            systemIPAddress = sc.readLine().trim();
            if (!(systemIPAddress.length() > 0)) {
                try {
                    InetAddress localhost = InetAddress.getLocalHost();
                    System.out.println((localhost.getHostAddress()).trim());
                    systemIPAddress = (localhost.getHostAddress()).trim();
                } catch (Exception e1) {
                    systemIPAddress = "Cannot Execute Properly";
                }
            }
        } catch (Exception e2) {
            systemIPAddress = "Cannot Execute Properly";
        }
        System.out.println("\nYour IP Address: " + systemIPAddress + "\n");

    }

    @Test
    public void way4() {
        String currentHostIpAddress = "";
        if (currentHostIpAddress == null) {
            Enumeration<NetworkInterface> netInterfaces = null;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();

                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> address = ni.getInetAddresses();
                    while (address.hasMoreElements()) {
                        InetAddress addr = address.nextElement();
                        //                      log.debug("Inetaddress:" + addr.getHostAddress() + " loop? " + addr.isLoopbackAddress() + " local? "
                        //                            + addr.isSiteLocalAddress());
                        if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()
                                && !(addr.getHostAddress().indexOf(":") > -1)) {
                            currentHostIpAddress = addr.getHostAddress();
                        }
                    }
                }
                if (currentHostIpAddress == null) {
                    currentHostIpAddress = "127.0.0.1";
                }

            } catch (SocketException e) {
//                log.error("Somehow we have a socket error acquiring the host IP... Using loopback instead...");
                currentHostIpAddress = "127.0.0.1";
            }
        }
        System.out.println("Way4 IP found: " + currentHostIpAddress);
    }

    @Test
    public void way5() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());
    }


    @Test
    public void wirdzar() {
//        a1b1a2b2c2d2e1a1
        String input = "abaabbccddea";
        String target = "";
        String prev = "";
        int count = 1;
        for (int i = 0; i < input.length(); i++) {
            String curr = String.valueOf(input.charAt(i));
            if (curr != prev) {
                target = prev.concat(String.valueOf(count));
                prev = curr;
                count = 1;
            } else if (curr.equals(prev)) count++;

        }
        System.out.println(target);
    }
}
