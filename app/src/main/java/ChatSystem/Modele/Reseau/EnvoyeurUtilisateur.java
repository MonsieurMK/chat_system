package ChatSystem.Modele.Reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class EnvoyeurUtilisateur extends Thread {

    public static final int PORT = 9001;
    public static final int TAILLE_BUFFER = 1024;

    public static final int PERIODE_ENVOI = 2000; // en ms

    private DatagramSocket sock;
    private DatagramPacket packet;
    private byte[] buffer;

    private InetAddress address;

    // debug
    private int port;
    private String msg;

    public EnvoyeurUtilisateur(int port, InetAddress address, String msg) {
        this.port = port;
        this.address = address;
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
        this.buffer = msg.getBytes(StandardCharsets.UTF_8);
        this.packet = new DatagramPacket(this.buffer, msg.length(), this.address, this.port);
        try {
            this.sock.send(this.packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
