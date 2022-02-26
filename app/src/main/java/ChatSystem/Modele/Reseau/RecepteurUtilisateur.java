package ChatSystem.Modele.Reseau;

import ChatSystem.Controlleur.MainController;
import ChatSystem.Modele.GestionnaireConv;
import ChatSystem.Modele.Utilisateur;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class RecepteurUtilisateur extends Thread {

    public static final int PORT = 9001;
    public static final int TAILLE_BUFFER = 1024;
    public static final int PERIODE_ATTENTE = 2000;

    private DatagramSocket sock;
    private DatagramPacket packet;
    @SuppressWarnings("FieldMayBeFinal")
    private byte[] buffer;
    
    @SuppressWarnings("FieldMayBeFinal")
    private List<InetAddress> localAddresses;

    private final GestionnaireConv gestionnaireConv;

    public RecepteurUtilisateur(int port, GestionnaireConv gestionnaireConv) {
        // debug
        this.gestionnaireConv = gestionnaireConv;

        try {
            this.sock = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.packet = null;
        this.buffer = new byte[TAILLE_BUFFER];
        
        this.localAddresses = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                for (InterfaceAddress iAddress :
                        ni.getInterfaceAddresses()) {
                    this.localAddresses.add(iAddress.getAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public Utilisateur recevoir() {
        this.packet = new DatagramPacket(this.buffer, TAILLE_BUFFER);
        try {
            this.sock.receive(this.packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!this.localAddresses.contains(this.packet.getAddress())) {
            System.out.println("user received");
            return new Utilisateur(this.packet.getAddress(), new String(this.packet.getData(), 0, this.packet.getLength()));
        }
        return null;
    }
    
    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(PERIODE_ATTENTE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.gestionnaireConv.refreshUserList();
            this.gestionnaireConv.detectUser(this.recevoir());
        }
    }
}
