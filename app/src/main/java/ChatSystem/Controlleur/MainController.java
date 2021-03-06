package ChatSystem.Controlleur;

import ChatSystem.Modele.*;
import ChatSystem.Modele.Reseau.ConnecteurConv;
import ChatSystem.Modele.Reseau.EnvoyeurUtilisateur;
import ChatSystem.Modele.Reseau.RecepteurUtilisateur;
import ChatSystem.Vue.MainFrame;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Controlleur principal de l'application
 */
public class MainController {

    /**
     * Interface graphique
     */
    private final MainFrame mainFrame;
    /**
     * Gestionnaire de conversations
     */
    private final GestionnaireConv gestionnaireConv;
    /**
     * Utilisateurs par adresse IP
     */
    private final HashMap<InetAddress, Utilisateur> addressUtilisateurHashMap;
    /**
     * Conversations ouvertes
     */
    private final List<Conversation> conversationsActives;

    /**
     * Conversation courante
     */
    private Conversation currentConversation;

    /**
     * Crée un controlleur principal
     * @param title titre de la fenêtre de l'interface graphique
     * @param tcpServerPort port serveur TCP
     * @param tcpClientPort port client TCP
     * @param udpServerPort port serveur UDP
     * @param udpClientPort port client UDP
     * @param privateUsername pseudonyme de l'utilisateur courant
     */
    public MainController(String title, int tcpServerPort, int tcpClientPort, int udpServerPort, int udpClientPort,
                          String privateUsername, boolean isFullscreen) {
        this.mainFrame = new MainFrame(title, isFullscreen, this);
        this.gestionnaireConv = new GestionnaireConv(privateUsername, this, tcpServerPort, tcpClientPort, udpServerPort, udpClientPort);
        this.addressUtilisateurHashMap = new HashMap<>();
        this.conversationsActives = new ArrayList<>();

        this.currentConversation = null;
    }

    /*
    called from model
     */

    /**
     * Affiche un utilisateur détecté
     * @param utilisateur utilisateur détecté
     */
    public void detectUser(Utilisateur utilisateur) {
        if (!this.addressUtilisateurHashMap.containsKey(utilisateur.getAddresseIP())) {
            this.addressUtilisateurHashMap.put(utilisateur.getAddresseIP(), utilisateur);
            this.mainFrame.addUserToList(utilisateur);
        }
    }

    /**
     * Vide la liste des utilisateurs connectés
     */
    public void refreshUserList() {
        this.addressUtilisateurHashMap.clear();
        this.mainFrame.refreshUserList();
    }

    /**
     * Ouvre une demande de conversation reçue
     * @param conversation conversation à ouvrir
     */
    public void openReceivedConversation(Conversation conversation) {
        if(!this.conversationsActives.contains(conversation)) {
            this.mainFrame.addConversationToList(conversation);
            this.conversationsActives.add(conversation);

            this.currentConversation = conversation;
            this.mainFrame.setSendMessageState(true);
            this.mainFrame.displayConversation(conversation);
        }
    }

    /**
     * Affiche un message reçu
     * @param conversation conversation correspondante au message
     * @param message message reçu
     */
    public void receiveMessage(Conversation conversation, Message message) {
        this.currentConversation = this.conversationsActives.get(this.conversationsActives.indexOf(conversation));
        this.mainFrame.receiveMessage(message);
    }

    /**
     * Retourne un utilisateur à partir de son addresse IP
     * @param address addresse IP de l'utilisateur
     * @return utilisateur correspondant à l'adresse IP
     * @throws IllegalArgumentException lorsque l'adresse IP ne correspond à aucun utilisateur
     */
    public Utilisateur getUtilisateurFromAddress(InetAddress address)
    throws IllegalArgumentException {
        if (!this.addressUtilisateurHashMap.containsKey(address)) {
            throw new IllegalArgumentException("utilisateur iconnu");
        }
        return this.addressUtilisateurHashMap.get(address);
    }

    /**
     * Retire une conversation de l'interface
     * @param conversation conversation à retirer
     */
    public void removeConversation(Conversation conversation) {
        this.conversationsActives.remove(conversation);
        this.mainFrame.removeConversation(conversation);
        if (this.currentConversation.equals(conversation)) {
            this.mainFrame.clearDisplayedMessages();
        }
    }
    /*
    called from model
     */

    /*
    called from view
     */

    /**
     * Ouvre une nouvelle conversation
     * @param utilisateur utilisateur de la conversation
     */
    public void openConversation(Utilisateur utilisateur) {
        Conversation conversation = this.gestionnaireConv.ouvrirConversation(utilisateur);
        if (!this.conversationsActives.contains(conversation)) {
            this.mainFrame.addConversationToList(conversation);
            this.conversationsActives.add(conversation);

            this.currentConversation = conversation;
            this.mainFrame.setSendMessageState(true);
            this.mainFrame.displayConversation(conversation);
        }
    }

    /**
     * Envoie un message à un utilisateur
     * @param message message à envoyer
     */
    public void sendMessage(Message message) {
        // only for text
        this.currentConversation.envoyerMessage(message);
    }

    /**
     * Retourne l'utilisateur courant du système
     * @return utilisateur courant du système
     */
    public UtilisateurPrive getUtilisateurPrive() {
        return this.gestionnaireConv.getUtilisateurPrive();
    }
    /*
    called from view
     */

}
