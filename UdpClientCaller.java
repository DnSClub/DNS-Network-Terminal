package com.lumpology.nfcterminal;
public class UdpClientCaller {

    public static void callUdpClientInBackground(String convertedData) {
        Runnable udpRunnable = new Runnable() {
            @Override
            public void run() {
                UdpClient.card_sender(convertedData);
            }
        };

        Thread udpThread = new Thread(udpRunnable);
        udpThread.start();
    }
}