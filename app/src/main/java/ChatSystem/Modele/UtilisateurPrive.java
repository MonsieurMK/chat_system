package ChatSystem.Modele;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utilisateur courant du système
 */
public class UtilisateurPrive extends Utilisateur {
    /**
     * Crée l'utilisateur courant du système
     * @param pseudonyme pseudonyme de l'utilisateur
     * @throws UnknownHostException lorsque l'adresse IP locale est introuvable
     */
    public UtilisateurPrive(String pseudonyme) throws UnknownHostException {
        super(InetAddress.getLocalHost(), pseudonyme);
    }

}
