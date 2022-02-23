package JavaProjectTemplate.Modele;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireConv {
    public static List<Conversation> conversations = new ArrayList<>();

    public List<Utilisateur> utilisateurs = new ArrayList<>();

    public UtilisateurPrive utilisateurPrive;

    public void changerPseudo(Utilisateur utilisateur, String pseudonyme) {
    }

    public void ouvrirConversation() {
    }

    public void envoyerMessage(Message message, Conversation conversation) {
    }

    public void fermerConversation(Conversation conversation) {
    }

    public static void ajouterConversation(Conversation conversation) {
        conversations.add(conversation);
    }

    // change String to Message
    public static void envoyerMessage(String message, Conversation conversation) {
        // debug test text message only
        conversations.get(conversations.indexOf(conversation)).envoyerMessage(new Texte(null, message));
        System.out.println("Message sent");
    }

}
