package JavaProjectTemplate.Modele;

import java.util.Date;

public class Texte extends Message {
    private String contenu;

    public Texte(Date dateEnvoi, String contenu) {
        super(null);
        this.contenu = contenu;
    }

    public String getContenu() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.contenu;
    }

}
