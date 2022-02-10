package JavaProjectTemplate.Modele;

import java.util.Date;

public abstract class Message {
    private Date dateEnvoi;

    public Message(Date dateEnvoi) {
    }

    public Date getDateEnvoi() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.dateEnvoi;
    }

    public void setDateEnvoi(Date value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.dateEnvoi = value;
    }

}
