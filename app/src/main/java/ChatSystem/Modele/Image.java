package ChatSystem.Modele;

import java.util.Date;

public class Image extends Message {
    private String lien;

    public Image(Date dateEnvoi, String lien, Utilisateur utilisateur) {
        super(null, utilisateur);
    }

    public String getLien() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.lien;
    }

}
