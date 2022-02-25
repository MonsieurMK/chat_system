package ChatSystem.Modele;

import ChatSystem.Controlleur.MainController;
import ChatSystem.Modele.Reseau.ConnecteurConv;
import ChatSystem.Modele.Reseau.EnvoyeurUtilisateur;
import ChatSystem.Modele.Reseau.RecepteurUtilisateur;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GestionnaireConv {
    private static List<Conversation> conversations = new ArrayList<>();

    //private List<Utilisateur> utilisateurs = new ArrayList<>();
    private HashMap<InetAddress, Utilisateur> addressUtilisateurHashMap = new HashMap<>();

    private UtilisateurPrive utilisateurPrive;
    private ConnecteurConv connecteurConv;
    private RecepteurUtilisateur recepteurUtilisateur;
    private EnvoyeurUtilisateur envoyeurUtilisateur;

    private MainController mainController;

    public GestionnaireConv(String privateUsername, MainController mainController,
                            int tcpServerPort, int tcpClientPort,
                            int udpServerPort, int udpClientPort) {
        // debug
        try {
            this.utilisateurPrive = new UtilisateurPrive(privateUsername);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.mainController = mainController;
        this.connecteurConv = new ConnecteurConv(tcpServerPort, tcpClientPort, this);
        this.connecteurConv.start();

        this.recepteurUtilisateur = new RecepteurUtilisateur(udpServerPort, this);
        this.envoyeurUtilisateur = new EnvoyeurUtilisateur(udpClientPort, this.utilisateurPrive.getAddresseIP(), this.utilisateurPrive.getPseudonyme());
        this.recepteurUtilisateur.start();
        this.envoyeurUtilisateur.start();
    }

    public void changerPseudo(Utilisateur utilisateur, String pseudonyme) {
    }

    public Conversation ouvrirConversation(Utilisateur utilisateur) {
        Conversation conversation = this.connecteurConv.connecter(utilisateur);
        this.addressUtilisateurHashMap.put(utilisateur.getAddresseIP(), utilisateur);
        this.conversations.add(conversation);
        return conversation;
    }

    public void fermerConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        this.mainController.removeConversation (conversation);
    }

    public UtilisateurPrive getUtilisateurPrive() {
        return utilisateurPrive;
    }

    public void ajouterConversation(Socket socket) {
        Utilisateur utilisateur = this.mainController.getUtilisateurFromAddress(socket.getInetAddress());
        this.addressUtilisateurHashMap.put(socket.getInetAddress(), utilisateur);
        Conversation conversation = new Conversation(utilisateur.getPseudonyme(), utilisateur, socket, this);
        conversation.start();
        conversations.add(conversation);
        this.mainController.openReceivedConversation(conversation);
    }

    // change String to Message
    public void envoyerMessage(String message, Conversation conversation) {
        // debug test text message only
        conversations.get(conversations.indexOf(conversation)).envoyerMessage(new Texte(LocalDateTime.now(), message, this.utilisateurPrive));
        System.out.println("Message sent");
    }

    public void receptionMessage(Conversation conversation, Message message) {
        this.mainController.receiveMessage(conversation, message);
    }

    public void detectUser(Utilisateur utilisateur) {
        if (utilisateur != null &&
                !this.addressUtilisateurHashMap.containsKey(utilisateur.getAddresseIP())) {
            this.addressUtilisateurHashMap.put(utilisateur.getAddresseIP(), utilisateur);
            this.mainController.detectUser(utilisateur);
        }
    }

}
