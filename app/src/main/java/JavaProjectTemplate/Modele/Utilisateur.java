package JavaProjectTemplate.Modele;


public class Utilisateur {
    private String addresseIP;

    private String pseudonyme;

    public Conversation conversation;

    public Utilisateur(String addresseIP, String pseudonyme) {
    }

    public String getAddresseIP() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.addresseIP;
    }

    public String getPseudonyme() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.pseudonyme;
    }

    public void setPseudonyme(String value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.pseudonyme = value;
    }

}
