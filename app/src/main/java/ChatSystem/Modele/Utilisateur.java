package ChatSystem.Modele;


import java.net.InetAddress;
import java.util.Objects;

/**
 * Représente un utilisateur du système
 */
public class Utilisateur {
    /**
     * Addresse IP de l'utilisateur
     */
    private final InetAddress addresseIP;

    /**
     * Pseudonyme unique de l'utilisateur
     */
    private String pseudonyme;

    /**
     * Conversation associée à l'utilisateur (1 pour cette version)
     */
    private Conversation conversation;

    /**
     * Crée un utilisateur
     * @param addresseIP addresse IP de l'utilisateur
     * @param pseudonyme pseudonyme de l'utilisateur
     */
    public Utilisateur(InetAddress addresseIP, String pseudonyme) {
        this.addresseIP = addresseIP;
        this.pseudonyme = pseudonyme;
    }

    /**
     * Retourne l'addresse IP de l'utilisateur
     * @return addresse IP de l'utilisateur
     */
    public InetAddress getAddresseIP() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.addresseIP;
    }

    /**
     * Retourne le pseudonyme de l'utilisateur
     * @return pseudonyme de l'utilisateur
     */
    public String getPseudonyme() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.pseudonyme;
    }

    /**
     * Modifie le pseudonyme de l'utilisateur
     * @param pseudo nouveau pseudonyme
     */
    public void setPseudonyme(String pseudo) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.pseudonyme = pseudo;
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
