package JavaProjectTemplate.Modele;


import java.net.InetAddress;

public class Utilisateur {
    private InetAddress addresseIP;

    private String pseudonyme;

    public Conversation conversation;

    public Utilisateur(InetAddress addresseIP, String pseudonyme) {
        this.addresseIP = addresseIP;
        this.pseudonyme = pseudonyme;
    }

    public InetAddress getAddresseIP() {
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
