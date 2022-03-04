package chat_system.modele;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe représentant les messages échangés
 */
public abstract class Message {
    /**
     * Date et heure d'envoi du message
     */
    private LocalDateTime dateEnvoi;
    /**
     * Utilisteur qui a envoyé le message
     */
    private final Utilisateur envoyeur;

    private final UUID idMessage;

    private final Conversation conversation;

    /**
     * Crée un nouveau message
     * @param dateEnvoi date et heure d'envoi
     * @param envoyeur utilisateur envoyeur du message
     */
    public Message(LocalDateTime dateEnvoi, Utilisateur envoyeur, Conversation conversation) {
        this.dateEnvoi = dateEnvoi;
        this.envoyeur = envoyeur;
        this.conversation = conversation;
        this.idMessage = UUID.randomUUID();
    }

    public Message(LocalDateTime dateEnvoi, Utilisateur envoyeur, Conversation conversation, UUID idMessage) {
        this.dateEnvoi = dateEnvoi;
        this.envoyeur = envoyeur;
        this.conversation = conversation;
        this.idMessage = idMessage;
    }

    /**
     * Retourne l'envoyeur du message
     * @return envoyeur du message
     */
    public Utilisateur getEnvoyeur() {
        return envoyeur;
    }

    /**
     * Retourne la date et l'heure du message
     * @return date et heure du message
     */
    public LocalDateTime getDateEnvoi() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.dateEnvoi;
    }

    public UUID getIdMessage() {
        return idMessage;
    }

    public Conversation getConversation() {
        return conversation;
    }

    /**
     * Modifie la date et l'heure du message
     * @param value nouvelle date et heure du message
     */
    public void setDateEnvoi(LocalDateTime value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.dateEnvoi = value;
    }

}
