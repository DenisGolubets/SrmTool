package com.golubets.monitor.environment.arduinoconnection;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by golubets on 27.01.2017.
 */
public class IpFinderTest {
    public static void main(String[] args) throws IOException {


        for (String s : getLocalIP()) {
            String[] tmp = s.split("\\.");
            byte[] ip = {(byte) Integer.parseInt(tmp[0]),
                    (byte) Integer.parseInt(tmp[1]),
                    (byte) Integer.parseInt(tmp[2]),
                    (byte) Integer.parseInt(tmp[3])};
            for (int i = 1; i < 255; i++) {
                ip[3] = (byte) i;
                InetAddress address = InetAddress.getByAddress(ip);
                //System.out.println(address);
            }
        }

        System.out.println(getAllArduinoInNetwork());


    }

    public static List<String> getAllArduinoInNetwork(){
        List<String> list = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("arp -a");
            BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (true) {
                String tmp = inn.readLine();
                if (tmp == null) {
                    break;
                }
                if (tmp.contains("de-de")) {
                    String[] arr = arpParser(tmp);
                   list.add("ip: " + arr[0] + ", MAC: " + arr[1]);
                }
            }
            inn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getLocalIP() {
        List<String> list = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> anInterface = NetworkInterface.getNetworkInterfaces();

            while (anInterface.hasMoreElements()) {
                NetworkInterface networkInterface = anInterface.nextElement();
                if (networkInterface.getHardwareAddress() != null) {

                    while (networkInterface.getInetAddresses().hasMoreElements()) {
                        InetAddress inetAddress = networkInterface.getInetAddresses().nextElement();
                        if (!inetAddress.toString().contains(":")) {
                            list.add(inetAddress.getHostAddress());
                        }
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String[] arpParser(String str) {
        String tmp = str.trim();
        String ip = tmp.substring(0, tmp.indexOf(" "));
        tmp = tmp.substring(ip.length()).trim();
        String mac = tmp.substring(0, tmp.indexOf(" "));

        return new String[]{ip, mac};
    }
}
