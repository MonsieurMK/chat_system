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

public class MainController {

    private final MainFrame mainFrame;
    private final GestionnaireConv gestionnaireConv;

    //private List<Utilisateur> utilisateursDetectes;
    private final HashMap<InetAddress, Utilisateur> addressUtilisateurHashMap;
    private final List<Conversation> conversationsActives;

    // temp
    private Conversation currentConversation;

    public MainController(String title, int tcpServerPort, int tcpClientPort, int udpServerPort, int udpClientPort,
                          String privateUsername) {
        this.mainFrame = new MainFrame(title, false, this);
        this.gestionnaireConv = new GestionnaireConv(privateUsername, this, tcpServerPort, tcpClientPort, udpServerPort, udpClientPort);
        /*this.recepteurUtilisateur = new RecepteurUtilisateur(udpServerPort, this);
        this.envoyeurUtilisateur = new EnvoyeurUtilisateur(udpClientPort, this.gestionnaireConv.getUtilisateurPrive().getAddresseIP(), this.gestionnaireConv.getUtilisateurPrive().getPseudonyme());
        this.recepteurUtilisateur.start();
        this.envoyeurUtilisateur.start();*/

        this.addressUtilisateurHashMap = new HashMap<>();
        this.conversationsActives = new ArrayList<>();

        this.currentConversation = null;
    }

    /*
    called from model
     */
    public void detectUser(Utilisateur utilisateur) {
        if (!this.addressUtilisateurHashMap.containsKey(utilisateur.getAddresseIP())) {
            this.addressUtilisateurHashMap.put(utilisateur.getAddresseIP(), utilisateur);
            this.mainFrame.addUserToList(utilisateur);
        }
    }

    public void refreshUserList() {
        this.addressUtilisateurHashMap.clear();
        this.mainFrame.refreshUserList();
    }

    public void openReceivedConversation(Conversation conversation) {
        if(!this.conversationsActives.contains(conversation)) {
            this.mainFrame.addConversationToList(conversation);
            this.conversationsActives.add(conversation);

            this.currentConversation = conversation;
            this.mainFrame.setSendMessageState(true);
            this.mainFrame.displayConversation(conversation);
        }
    }

    public void receiveMessage(Conversation conversation, Message message) {
        this.currentConversation = this.conversationsActives.get(this.conversationsActives.indexOf(conversation));
        this.mainFrame.receiveMessage(message);
    }

    public Utilisateur getUtilisateurFromAddress(InetAddress address)
    throws IllegalArgumentException {
        if (!this.addressUtilisateurHashMap.containsKey(address)) {
            throw new IllegalArgumentException("utilisateur iconnu");
        }
        return this.addressUtilisateurHashMap.get(address);
    }

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

    public void sendMessage(Message message) {
        // only for text
        this.currentConversation.envoyerMessage(message);
    }

    public UtilisateurPrive getUtilisateurPrive() {
        return this.gestionnaireConv.getUtilisateurPrive();
    }
    /*
    called from view
     */

}
