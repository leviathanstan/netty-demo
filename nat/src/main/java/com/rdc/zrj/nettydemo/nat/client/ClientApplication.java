package com.rdc.zrj.nettydemo.nat.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

@SuppressWarnings("all")
public class ClientApplication {

    private static String SERVER_ADDRESS = "120.79.30.14";
    private static int SERVER_PORT = 9999;

    /**
     * 实验失败，推测为多重net导致。过于复杂，不再研究
     */
    public static void main(String[] args) throws Exception{
        String name = args[0];
        SocketAddress target = new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT);
        byte[] buf = new byte[1024];
        DatagramPacket receivePackage = new DatagramPacket(buf, buf.length);
        try (DatagramSocket socket = new DatagramSocket()){
            byte[] sendBuf = name.getBytes();
            socket.send(new DatagramPacket(sendBuf, sendBuf.length, target));
            // 接受server发来的其它client地址
            socket.receive(receivePackage);
            String addressStr = new String(receivePackage.getData(), 0, receivePackage.getLength());
            String[] addressStrs = addressStr.split(":");
            // 向其它client发送一个数据包，此数据包会被拒绝，但nat设备会记录下来
            sendBuf = "Hello world, client".getBytes();
            SocketAddress address = new InetSocketAddress(addressStrs[0], Integer.parseInt(addressStrs[1]));
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address);
            socket.send(sendPacket);
            new Thread(() -> {
                try {
                    // 等待一段时间，等双方都打了洞后，发送一条消息测试
                    Thread.sleep(2000);
                    byte[] bufs = "Hello world, client".getBytes();
                    DatagramPacket sp = new DatagramPacket(bufs, bufs.length, address);
                    socket.send(sp);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    //
                }
            }).start();
            // 阻塞在这里，如果打洞成功，会收到消息
            socket.receive(receivePackage);
            String message = new String(receivePackage.getData(), 0, receivePackage.getLength());
            System.out.println(">>>>>>>>>>>> i got it" + message);
        }
    }
}
