package JavaProjectTemplate.Modele;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireConv {
    public List<Conversation> conversations = new ArrayList<>();

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

}
