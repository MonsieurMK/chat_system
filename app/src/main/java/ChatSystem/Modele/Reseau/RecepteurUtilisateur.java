package ChatSystem.Modele.Reseau;

import ChatSystem.Controlleur.MainController;
import ChatSystem.Modele.GestionnaireConv;
import ChatSystem.Modele.Utilisateur;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Classe opérant les réceptions de messages UDP pour détecter les autres utilisateurs sur le système
 */
public class RecepteurUtilisateur extends Thread {

    /**
     * Port par défaut
     */
    public static final int PORT = 9001;
    /**
     * Taille du buffer de réception des messages
     */
    public static final int TAILLE_BUFFER = 1024;
    /**
     * Période d'écoute des connexions et du rafraîchissement
     */
    public static final int PERIODE_ATTENTE = 2000;

    /**
     * Socket UDP
     */
    private DatagramSocket sock;
    /**
     * Paquet UDP
     */
    private DatagramPacket packet;
    /**
     * Buffer des messages
     */
    @SuppressWarnings("FieldMayBeFinal")
    private byte[] buffer;

    /**
     * Addresses IP connues
     */
    @SuppressWarnings("FieldMayBeFinal")
    private List<InetAddress> localAddresses;

    /**
     * Gestionnaire de conversations
     */
    private final GestionnaireConv gestionnaireConv;

    /**
     * Crée le récepteur
     * @param port port utilisé pour recevoir les messages, si le port spécifié est 0 le port par défaut sera utilisé (9000)
     * @param gestionnaireConv gestionnaire de conversations
     */
    public RecepteurUtilisateur(int port, GestionnaireConv gestionnaireConv) {
        // debug
        this.gestionnaireConv = gestionnaireConv;

        try {
            if (port == 0) {
                port = PORT;
            }
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

    /**
     * Écoute périodique
     */
    @SuppressWarnings({"InfiniteLoopStatement"})
    @Override
    public void run() {
        while(true) {
            // reception
            this.packet = new DatagramPacket(this.buffer, TAILLE_BUFFER);
            try {
                this.sock.receive(this.packet);
                if (!this.localAddresses.contains(this.packet.getAddress())) {
                    ObjectInputStream oiStream = new ObjectInputStream(new ByteArrayInputStream(this.packet.getData()));
                    MessageReseau msgRzo = (MessageReseau) oiStream.readObject();
                    oiStream.close();
                    Utilisateur utilisateur = new Utilisateur(this.packet.getAddress(), msgRzo.getUsername());
                    if (msgRzo.getType() == MessageReseauType.CONNECT) {
                        // connexion
                        this.gestionnaireConv.detectUser(utilisateur);
                    } else {
                        // deconnexion
                        this.gestionnaireConv.disconnectUser(utilisateur);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            //this.gestionnaireConv.detectUser(this.recevoir());
        }
    }
}
