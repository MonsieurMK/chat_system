package ChatSystem.Modele.Reseau;

import ChatSystem.Controlleur.MainController;
import ChatSystem.Modele.Conversation;
import ChatSystem.Modele.GestionnaireConv;
import ChatSystem.Modele.Utilisateur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe opérant les connexions TCP entre utilisateurs afin d'échanger des messages
 */
public class ConnecteurConv extends Thread {

    /**
     * Port par défaut utilisé
     */
    public static final int PORT = 9000;

    // debug
    /**
     * Port d'écoute
     */
    private final int serverPort;
    /**
     * Port d'envoi
     */
    private final int clientPort;
    /**
     * Gestionnaire de la conversation
     */
    private final GestionnaireConv gestionnaireConv;

    /**
     * Crée le connecteur, serverPort et clientPort doivent avoir la même valeur pour fonctionner sur différents hôtes
     * @param serverPort port utilisé pour écouter les connexions entrantes, si le port spécifié est 0 le port par défaut sera utilisé (9000)
     * @param clientPort port utilisé pour envoyer des demandes de connexion, si le port spécifié est 0 le port par défaut sera utilisé (9000)
     * @param gestionnaireConv gestionnaire de conversation associé
     */
    public ConnecteurConv(int serverPort, int clientPort, GestionnaireConv gestionnaireConv) {
        if (serverPort == 0 && clientPort == 0) {
            this.serverPort = PORT;
            this.clientPort = PORT;
        } else {
            this.serverPort = serverPort;
            this.clientPort = clientPort;
        }
        this.gestionnaireConv = gestionnaireConv;
    }

    /**
     * Non implémenté
     * @param conversation n
     */
    public void ouvrirConversation(Conversation conversation) {
    }

    /**
     * Non implémenté
     * @param conversation n
     */
    public void fermerConversation(Conversation conversation) {
    }

    /**
     * Se connecte à un utilisateur distant
     * @param utilisateur utilisateur distant auquel on souhaite se connecter
     * @return Conversation qui résulte de la connexion
     */
    public Conversation connecter(Utilisateur utilisateur) {
        try {
            Socket socket = new Socket(utilisateur.getAddresseIP(), this.clientPort);
            Conversation conversation = new Conversation(utilisateur.getPseudonyme(), utilisateur, socket, this.gestionnaireConv);
            //GestionnaireConv.ajouterConversation(conversation);
            conversation.start();
            return conversation;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Écoute les demandes de connexions entrante
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            ServerSocket servSocket = new ServerSocket(this.serverPort);
            while (true) {
                Socket socket = servSocket.accept();
                this.gestionnaireConv.ajouterConversation(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
