package chat_system.controlleur;

import chat_system.modele.*;
import chat_system.vue.MainFrame;

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
        BDD.connect();
        BDD.createTables();
        this.mainFrame = new MainFrame(title, isFullscreen, this);
        this.gestionnaireConv = new GestionnaireConv(privateUsername, this, tcpServerPort, tcpClientPort, udpServerPort, udpClientPort);
        this.conversationsActives = new ArrayList<>();
        this.addressUtilisateurHashMap = new HashMap<>();

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

    public void disconnectUser(Utilisateur utilisateur) {
        if (this.addressUtilisateurHashMap.containsKey(utilisateur.getAddresseIP())) {
            this.addressUtilisateurHashMap.remove(utilisateur.getAddresseIP());
            this.mainFrame.removeUserFromList(utilisateur);
        }
    }

    /**
     * Vide la liste des utilisateurs connectés
     * @deprecated
     */
    @Deprecated
    public void refreshUserList() {
        this.addressUtilisateurHashMap.clear();
        this.mainFrame.refreshUserList();
    }

    /**
     * Ouvre une demande de conversation reçue
     * @param conversation conversation à ouvrir
     */
    public void openReceivedConversation(Conversation conversation) {
        startConversation(conversation);

        Utilisateur utilisateur = conversation.getUtilisateurs().get(0);
        if (BDD.getUtilisateur(utilisateur.getAddresseIP()) == null) {
            BDD.ajouterUtilisateur(utilisateur);
        }
        boolean found = BDD.chercherConversation(conversation);
        if (found) {
            System.out.println("conv dans la bdd");
            this.mainFrame.displayConversation(conversation);
        } else {
            System.out.println("nouvelle conv");
            BDD.ajouterConversation(conversation);
        }
    }

    private void startConversation(Conversation conversation) {
        if(!this.conversationsActives.contains(conversation)) {
            this.mainFrame.addConversationToList(conversation);
            this.mainFrame.setCloseConversationState(true);
            this.conversationsActives.add(conversation);

            this.currentConversation = conversation;
            this.mainFrame.setSendMessageState(true);
            this.mainFrame.displayConversation(conversation);
            this.mainFrame.setCurrentConversation(conversation);
        }
    }

    /**
     * Affiche un message reçu
     * @param conversation conversation correspondante au message
     * @param message message reçu
     */
    public void receiveMessage(Conversation conversation, Message message) {
        //this.currentConversation = this.conversationsActives.get(this.conversationsActives.indexOf(conversation));
        //this.currentConversation.ajouterMessage(message);
        //this.mainFrame.receiveMessage(message);
        if (this.currentConversation.equals(this.conversationsActives.get(this.conversationsActives.indexOf(conversation)))) {
            this.mainFrame.receiveMessage(message);
        }
        this.conversationsActives.get(this.conversationsActives.indexOf(conversation)).ajouterMessage(message);

        // BDD
        BDD.ajouterMessage(message);
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
        if (this.conversationsActives.contains(conversation)) {
            this.conversationsActives.remove(conversation);
            this.mainFrame.removeConversation(conversation);
            if (this.currentConversation.equals(conversation)) {
                this.mainFrame.clearDisplayedMessages();
                this.mainFrame.setCloseConversationState(false);
                this.mainFrame.setSendMessageState(false);
            }
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
        this.mainFrame.setCurrentConversation(conversation);
        startConversation(conversation);

        // BDD
        if (BDD.getUtilisateur(utilisateur.getAddresseIP()) == null) {
            BDD.ajouterUtilisateur(utilisateur);
        }
        boolean found = BDD.chercherConversation(conversation);
        if (found) {
            System.out.println("conv dans la bdd");
            this.mainFrame.displayConversation(conversation);
        } else {
            System.out.println("nouvelle conv");
            BDD.ajouterConversation(conversation);
        }
    }

    public void closeConversation(Conversation conversation) {
        if (this.conversationsActives.contains(conversation)) {
            this.mainFrame.removeConversationFromList(conversation);
            this.conversationsActives.remove(conversation);
            if (this.currentConversation.equals(conversation)) {
                this.currentConversation = null;
                this.mainFrame.clearDisplayedMessages();
                this.mainFrame.setCloseConversationState(false);
                this.mainFrame.setSendMessageState(false);
            }

            this.gestionnaireConv.fermerConversation(conversation);
        }
    }

    /**
     * Envoie un message à un utilisateur
     * @param message message à envoyer
     */
    public void sendMessage(Message message) {
        this.currentConversation.envoyerMessage(message);
        this.currentConversation.ajouterMessage(message);

        // BDD
        BDD.ajouterMessage(message);
    }

    /**
     * Retourne l'utilisateur courant du système
     * @return utilisateur courant du système
     */
    public UtilisateurPrive getUtilisateurPrive() {
        return GestionnaireConv.getUtilisateurPrive();
    }

    public void deconnexion() {
        this.gestionnaireConv.envoyerDeconnexion();
    }
    /*
    called from view
     */

}
