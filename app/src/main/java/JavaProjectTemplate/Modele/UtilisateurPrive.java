package JavaProjectTemplate.Modele;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class UtilisateurPrive extends Utilisateur {
    public UtilisateurPrive(String pseudonyme) throws UnknownHostException {
        super(InetAddress.getLocalHost(), "default");
    }

}
