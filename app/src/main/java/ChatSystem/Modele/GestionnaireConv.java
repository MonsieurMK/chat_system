package ChatSystem.Modele;

import ChatSystem.Controlleur.MainController;
import ChatSystem.Modele.Reseau.ConnecteurConv;
import ChatSystem.Modele.Reseau.EnvoyeurUtilisateur;
import ChatSystem.Modele.Reseau.MessageReseauType;
import ChatSystem.Modele.Reseau.RecepteurUtilisateur;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Gestionnaire de l'ensemble des conversations et des utilisateurs du système,
 * classe principale du système
 */
public class GestionnaireConv {
    /**
     * Ensemble des conversations du système
     */
    private final List<Conversation> conversations = new ArrayList<>();

    /**
     * Utilisateurs par adresses IP
     */
    private final HashMap<InetAddress, Utilisateur> addressUtilisateurHashMap = new HashMap<>();

    /**
     * Utilisateur courant du système
     */
    private UtilisateurPrive utilisateurPrive;
    /**
     * Connecteur de conversations
     */
    private final ConnecteurConv connecteurConv;

    /**
     * Controleur principal
     */
    private final MainController mainController;

    private final EnvoyeurUtilisateur envoyeurUtilisateur;

    /**
     * Crée un gestionnaire de conversations
     * @param privateUsername pseudonyme de l'utilisateur courant du système
     * @param mainController controleur principal
     * @param tcpServerPort port d'écoute TCP
     * @param tcpClientPort port d'envoi TCP
     * @param udpServerPort port d'écoute UDP
     * @param udpClientPort port d'envoi UDP
     */
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

        RecepteurUtilisateur recepteurUtilisateur = new RecepteurUtilisateur(udpServerPort, this);
        this.envoyeurUtilisateur = new EnvoyeurUtilisateur(udpClientPort, this.utilisateurPrive.getPseudonyme());
        recepteurUtilisateur.start();
        this.envoyeurUtilisateur.start();
    }

    public void changerPseudo(Utilisateur utilisateur, String pseudonyme) {
    }

    /**
     * Ouvre une conversation depuis son client
     * @param utilisateur utilisateur membre de la conversation
     * @return la conversation ouverte
     */
    public Conversation ouvrirConversation(Utilisateur utilisateur) {
        Conversation conversation = this.connecteurConv.connecter(utilisateur);
        if (!this.conversations.contains(conversation)) {
            this.conversations.add(conversation);
        }
        return conversation;
    }

    /**
     * Ferme une conversation
     * @param conversation conversation à fermer
     */
    public void fermerConversationDistante(Conversation conversation) {
        this.conversations.remove(conversation);
        this.mainController.removeConversation(conversation);
    }

    public void fermerConversation(Conversation conversation) {
        int index = this.conversations.indexOf(conversation);
        this.conversations.get(index).closeConversation();
        this.conversations.remove(index);
    }

    /**
     * Retourne l'utilisateur courant du système
     * @return utilisateur courant du système
     */
    public UtilisateurPrive getUtilisateurPrive() {
        return utilisateurPrive;
    }

    /**
     * Ajoute une conversation ouverte depuis un autre hôte
     * @param socket socket associé à la conversation ouverte
     */
    public void ajouterConversation(Socket socket) {
        Utilisateur utilisateur = this.mainController.getUtilisateurFromAddress(socket.getInetAddress());
        Conversation conversation = new Conversation(utilisateur.getPseudonyme(), utilisateur, socket, this);
        if (!this.conversations.contains(conversation)) {
            conversation.start();
            conversations.add(conversation);
            this.mainController.openReceivedConversation(conversation);
        }
    }

    /**
     * Envoie un message dans une conversation
     * @param message message à envoyer
     * @param conversation conversation où l'on souhaite envoyer le message
     */
    public void envoyerMessage(String message, Conversation conversation) {
        // debug test text message only
        // change String to Message
        conversations.get(conversations.indexOf(conversation)).envoyerMessage(new Texte(LocalDateTime.now(), message, this.utilisateurPrive));
    }

    /**
     * Gère la réception d'un message reçu
     * @param conversation conversation correspondant au message
     * @param message message reçu
     */
    public void receptionMessage(Conversation conversation, Message message) {
        this.mainController.receiveMessage(conversation, message);
    }

    /**
     * Détecte un utilisateur connecté
     * @param utilisateur utilisateur détécté
     */
    public void detectUser(Utilisateur utilisateur) {
        if (utilisateur != null &&
                !this.addressUtilisateurHashMap.containsKey(utilisateur.getAddresseIP())) {
            this.addressUtilisateurHashMap.put(utilisateur.getAddresseIP(), utilisateur);
            this.mainController.detectUser(utilisateur);
        }
    }

    public void disconnectUser(Utilisateur utilisateur) {
        if (utilisateur != null &&
                this.addressUtilisateurHashMap.containsKey(utilisateur.getAddresseIP())) {
            this.addressUtilisateurHashMap.remove(utilisateur.getAddresseIP());
            this.mainController.disconnectUser(utilisateur);
        }
    }

    /**
     * Vide la liste des utilisateurs connectés
     * @deprecated
     */
    @Deprecated
    public void refreshUserList() {
        this.addressUtilisateurHashMap.clear();
        this.mainController.refreshUserList();
    }

    public void envoyerDeconnexion() {
        this.envoyeurUtilisateur.envoyer(this.utilisateurPrive.getPseudonyme(), MessageReseauType.DISCONNECT);
    }
}
