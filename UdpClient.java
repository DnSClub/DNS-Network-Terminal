package com.lumpology.nfcterminal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UdpClient {

    public static String/*Boolean*/ card_sender(String message) {
        try {
            //DatagramSocket socket = new DatagramSocket();

            //StringBuilder message = new StringBuilder("DNS_BANK_EXCHANGE_PROTOCOL:");
//            int args_len = args.;
//            for (int i = 0; i < args_len; i++) {
//                message.append("\n").append(args[i]);
//            }

            //71.17.162.206
            //204.83.169.199
            //byte[] serverIpAddress = new byte[]{(byte)71, (byte)17, (byte)162, (byte)206};

            //byte[] serverIpAddress = new byte[]{(byte)204, (byte)83, (byte)169, (byte)199};
            //InetAddress serverAddress = InetAddress.getByAddress(serverIpAddress);
            //int serverPort = 25542; // Use the same port as the server

            //byte[] data = message.getBytes();
            //DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

            //socket.send(packet);
            //System.out.println("Data Sent To DNS Servers: " + message);

            //socket.close();
            // Send data over UDP
            sendUDPMessage(message);
            return message+"\nSent Card Data To DNS Servers.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message+"\nFailed To Send Card Data To DNS Servers.";
    }

    public static void sendUDPMessage(final String message) {
        new Thread(() -> {
            try {
                // Specify the server's IP address and port
                InetAddress serverAddress = InetAddress.getByName("204.83.169.199");

                int serverPort = 25542;//development port remember to switch back before deployment

                // Create a DatagramSocket
                DatagramSocket socket = new DatagramSocket();

                // Convert the message to bytes
                byte[] data = message.getBytes();

                // Create a DatagramPacket to send to the server
                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

                // Send the packet
                socket.send(packet);

                // Close the socket
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}