package JavaProjectTemplate.Modele;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private String nom;

    public List<Utilisateur> utilisateurs = new ArrayList<>();

    public UtilisateurPrive utilisateurPrive;

    public String getNom() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.nom;
    }

    public void setNom(String value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.nom = value;
    }

    public Conversation(String nom) {
    }

    public Conversation(String nom, Utilisateur utilisateur) {
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
    }

    public void ajouterMessage(Message message, Utilisateur envoyeur) {
    }

}
