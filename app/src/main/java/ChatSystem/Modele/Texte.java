package ChatSystem.Modele;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

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
    public Texte(LocalDateTime dateEnvoi, String contenu, Utilisateur envoyeur, Conversation conversation) {
        super(dateEnvoi, envoyeur, conversation);
        this.contenu = contenu;
    }

    public Texte(LocalDateTime dateEnvoi, String contenu, Utilisateur envoyeur, Conversation conversation, UUID idMessage) {
        super(dateEnvoi, envoyeur, conversation, idMessage);
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
