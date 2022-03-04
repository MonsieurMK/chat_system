package chat_system.modele.Reseau;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Enumeration;

/**
 * Classe opérant les envois de messages UDP pour indiquer sa présence sur le système
 */
public class EnvoyeurUtilisateur extends Thread {

    /**
     * Port par défaut
     */
    public static final int PORT = 9001;
    /**
     * Taille du buffer d'envoi des messages
     */
    public static final int TAILLE_BUFFER = 1024;

    /**
     * Période d'envoi des messages (en ms)
     */
    public static final int PERIODE_ENVOI = 2000;

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
    private byte[] buffer;

    /**
     * Port utilisé
     */
    private final int port;
    /**
     * Message à envoyer
     */
    private final String msg;

    /**
     * Crée l'envoyeur
     * @param port port utilisé pour envoyer les messages, si le port spécifié est 0 le port par défaut sera utilisé (9000)
     * @param msg message à envoyer (typiquement le pseudonyme de l'utilisateur courant)
     */
    public EnvoyeurUtilisateur(int port, String msg) {
        if (port == 0) {
            this.port = PORT;
        } else {
            this.port = port;
        }
        this.msg = msg;
        try {
            this.sock = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.packet = null;
        this.buffer = new byte[TAILLE_BUFFER];
    }

    /**
     * Envoi d'un message en broadcast pour signaler sa présence
     * @param msg message à envoyer
     */
    public void envoyer(String msg, MessageReseauType type) {
        try {
            // creation du message
            MessageReseau msgRzo = new MessageReseau(type, msg);
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutputStream ooStream = new ObjectOutputStream(bStream);
            ooStream.writeObject(msgRzo);
            ooStream.close();
            this.buffer = bStream.toByteArray();

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                for (InterfaceAddress interfaceAddress :
                        networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }

                    this.packet = new DatagramPacket(this.buffer, this.buffer.length, broadcast, this.port);
                    this.sock.send(this.packet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoi périodique du message
     */
    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    @Override
    public void run() {
        while (true) {
            this.envoyer(this.msg, MessageReseauType.CONNECT);
            try {
                Thread.sleep(PERIODE_ENVOI);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
