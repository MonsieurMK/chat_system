package ChatSystem.Modele.Reseau;

import ChatSystem.Controlleur.MainController;
import ChatSystem.Modele.Conversation;
import ChatSystem.Modele.GestionnaireConv;
import ChatSystem.Modele.Utilisateur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnecteurConv extends Thread {

    public static final int PORT = 9000;

    // debug
    private int serverPort;
    private int clientPort;
    private GestionnaireConv gestionnaireConv;

    public ConnecteurConv(int serverPort, int clientPort, GestionnaireConv gestionnaireConv) {
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.gestionnaireConv = gestionnaireConv;
    }

    public void ouvrirConversation(Conversation conversation) {
    }

    public void fermerConversation(Conversation conversation) {
    }

    public Conversation connecter(Utilisateur utilisateur) {
        try {
            Socket socket = new Socket(utilisateur.getAddresseIP(), this.clientPort);
            System.out.println("Connected to server for new connection");
            Conversation conversation = new Conversation(utilisateur.getPseudonyme(), utilisateur, socket, this.gestionnaireConv);
            //GestionnaireConv.ajouterConversation(conversation);
            conversation.start();
            return conversation;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // listening for connections
    @Override
    public void run() {
        System.out.println("Server listening for new connections ");
        try {
            ServerSocket servSocket = new ServerSocket(this.serverPort);
            while (true) {
                Socket socket = servSocket.accept();
                // if here, new conversation query
                // find username from ip
                System.out.println("New connection received");
                this.gestionnaireConv.ajouterConversation(socket);
                //GestionnaireConv.ajouterConversation(conversation);
                //conversation.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
