package ChatSystem.Modele;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Représente un message textuel
 */
public class Texte extends Message {
    /**
     * Contenu textuel du message
     */
    private final String contenu;

    /**
     * Crée un message textuel
     * @param dateEnvoi date et heure d'envoi du message
     * @param contenu contenu textuel du message
     * @param envoyeur utilisateur envoyeur du message
     */
    public Texte(LocalDateTime dateEnvoi, String contenu, Utilisateur envoyeur) {
        super(dateEnvoi, envoyeur);
        this.contenu = contenu;
    }

    /**
     * Retourne le contenu du message
     * @return contenu textuel du message
     */
    public String getContenu() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.contenu;
    }

}
