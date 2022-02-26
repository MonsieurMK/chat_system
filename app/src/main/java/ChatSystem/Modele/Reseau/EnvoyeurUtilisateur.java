package ChatSystem.Modele.Reseau;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class EnvoyeurUtilisateur extends Thread {

    public static final int PORT = 9001;
    public static final int TAILLE_BUFFER = 1024;

    public static final int PERIODE_ENVOI = 2000; // en ms

    private DatagramSocket sock;
    private DatagramPacket packet;
    private byte[] buffer;

    // debug
    private final int port;
    private final String msg;

    public EnvoyeurUtilisateur(int port, InetAddress address, String msg) {
        this.port = port;
        this.msg = msg;
        try {
            this.sock = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.packet = null;
        this.buffer = new byte[TAILLE_BUFFER];
    }

    public void envoyer(String msg) {
        try {
            this.buffer = msg.getBytes(StandardCharsets.UTF_8);

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                for (InterfaceAddress interfaceAddress :
                        networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }

                    this.packet = new DatagramPacket(this.buffer, msg.length(), broadcast, this.port);

                    this.sock.send(this.packet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    @Override
    public void run() {
        while (true) {
            this.envoyer(this.msg);
            try {
                Thread.sleep(PERIODE_ENVOI);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
