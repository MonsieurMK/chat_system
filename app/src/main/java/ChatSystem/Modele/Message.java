package ChatSystem.Modele;

import java.time.LocalDateTime;
import java.util.Date;

public abstract class Message {
    private LocalDateTime dateEnvoi;
    private Utilisateur envoyeur;

    public Message(LocalDateTime dateEnvoi, Utilisateur envoyeur) {
        this.dateEnvoi = dateEnvoi;
        this.envoyeur = envoyeur;
    }

    public Utilisateur getEnvoyeur() {
        return envoyeur;
    }

    public LocalDateTime getDateEnvoi() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.dateEnvoi = value;
    }

}
