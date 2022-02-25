package ChatSystem.Modele.Reseau;

import ChatSystem.Controlleur.MainController;
import ChatSystem.Modele.GestionnaireConv;
import ChatSystem.Modele.Utilisateur;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class RecepteurUtilisateur extends Thread {

    public static final int PORT = 9001;
    public static final int TAILLE_BUFFER = 1024;

    private DatagramSocket sock;
    private DatagramPacket packet;
    private byte[] buffer;

    private GestionnaireConv gestionnaireConv;

    // debug
    private int port;

    public RecepteurUtilisateur(int port, GestionnaireConv gestionnaireConv) {
        this.port = port;
        this.gestionnaireConv = gestionnaireConv;

        try {
            this.sock = new DatagramSocket(this.port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.packet = null;
        this.buffer = new byte[TAILLE_BUFFER];
    }

    public Utilisateur recevoir() {
        this.packet = new DatagramPacket(this.buffer, TAILLE_BUFFER);
        try {
            this.sock.receive(this.packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!this.packet.getAddress().equals(this.gestionnaireConv.getUtilisateurPrive().getAddresseIP()) &&
        !this.packet.getAddress().isLoopbackAddress()) {
            System.out.println("user received");
            return new Utilisateur(this.packet.getAddress(), new String(this.packet.getData(), 0, this.packet.getLength()));
        }
        return null;
    }

    @Override
    public void run() {
        while(true) {
            // TODO verifier periodiquement si il y a deconnexion
            this.gestionnaireConv.detectUser(this.recevoir());
        }
    }
}
