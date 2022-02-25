package ChatSystem.Modele;

import java.time.LocalDateTime;
import java.util.Date;

public class Texte extends Message {
    private String contenu;

    public Texte(LocalDateTime dateEnvoi, String contenu, Utilisateur envoyeur) {
        super(dateEnvoi, envoyeur);
        this.contenu = contenu;
    }

    public String getContenu() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.contenu;
    }

}
