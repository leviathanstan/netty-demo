package com.rdc.zrj.nettydemo.nat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class ServerApplication {

    private static String client1 = "clientA";
    private static String client2 = "clientB";

    public static void main(String[] args) throws Exception{
        try (DatagramSocket server = new DatagramSocket(new InetSocketAddress("0.0.0.0", 9999))){
            Map<String, String> map = new HashMap<>(8);
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while(true) {
                server.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                map.put(message, packet.getAddress() + ":" + packet.getPort());
                // 触发打洞命令
                if (map.size() == 2) {
                    String[] strs = map.get(client1).split(":");
                    sendIP(map.get(client1), new InetSocketAddress(strs[0], Integer.valueOf(strs[1])), server);
                    strs = map.get(client2).split(":");
                    sendIP(map.get(client2), new InetSocketAddress(strs[0], Integer.valueOf(strs[1])), server);
                }

            }
        }
    }

    private static void sendIP(String message, InetSocketAddress address, DatagramSocket server)
    {
        byte[] sendBuf = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address);
        try {
            server.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
