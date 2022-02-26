package ChatSystem.Modele;

import java.time.LocalDateTime;
import java.util.Date;

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

    /**
     * Crée un nouveau message
     * @param dateEnvoi date et heure d'envoi
     * @param envoyeur utilisateur envoyeur du message
     */
    public Message(LocalDateTime dateEnvoi, Utilisateur envoyeur) {
        this.dateEnvoi = dateEnvoi;
        this.envoyeur = envoyeur;
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

    /**
     * Modifie la date et l'heure du message
     * @param value nouvelle date et heure du message
     */
    public void setDateEnvoi(LocalDateTime value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.dateEnvoi = value;
    }

}
