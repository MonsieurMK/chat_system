package ChatSystem.Modele;


import java.net.InetAddress;
import java.util.Objects;

public class Utilisateur {
    private InetAddress addresseIP;

    private String pseudonyme;

    private Conversation conversation;

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

    @Override
    public String toString() {
        return this.pseudonyme + " (" + this.addresseIP.getHostAddress() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return addresseIP.equals(that.addresseIP) && pseudonyme.equals(that.pseudonyme);
    }

}
