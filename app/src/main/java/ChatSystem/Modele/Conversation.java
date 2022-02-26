package ChatSystem.Modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Représente une conversation
 */
public class Conversation extends Thread {

    /**
     * Socket de la connexion TCP
     */
    private final Socket socket;
    /**
     * Flux entrant
     */
    private BufferedReader in;
    /**
     * Flux sortant
     */
    private PrintStream out;

    /**
     * Nom de la conversation
     */
    private String nom;

    /**
     * Utilisateurs participants à la conversation (1 pour la version actuelle)
     */
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    /**
     * Messages de la conversation
     */
    @SuppressWarnings("FieldMayBeFinal")
    private List<Message> messages = new ArrayList<>();

    /**
     * Utilisateur du système
     */
    private UtilisateurPrive utilisateurPrive;

    /**
     * Gestionnaire de conversations
     */
    private GestionnaireConv gestionnaireConv;

    /**
     * Renvoie le nom de la conversation
     * @return nom de la conversation
     */
    public String getNom() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.nom;
    }

    /**
     * Modifie le nom de la conversation
     * @param nom nouveau nom de la conversation
     */
    public void setNom(String nom) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.nom = nom;
        this.utilisateurs = new ArrayList<>();
    }

    /**
     * Crée une conversation
     * @param nom nom de la conversation
     * @param socket socket utilisé
     */
    public Conversation(String nom, Socket socket) {
        this.nom = nom;
        this.socket = socket;

        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée une conversation
     * @param nom nom de la conversation
     * @param utilisateur utilisateur unique de la conversation
     * @param socket socket utilisé
     * @param gestionnaireConv gestionnaire de conversations
     */
    public Conversation(String nom, Utilisateur utilisateur, Socket socket, GestionnaireConv gestionnaireConv) {
        this.nom = nom;
        this.utilisateurs = new ArrayList<>();
        this.utilisateurs.add(utilisateur);
        this.socket = socket;
        this.gestionnaireConv = gestionnaireConv;

        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajoute un utilisateur à la conversation
     * @param utilisateur utilisateur à ajouter
     */
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
    }

    /**
     * Ajoute un message à la conversation
     * @param message message à ajouter
     */
    public void ajouterMessage(Message message) {
        this.messages.add(message);
    }

    /**
     * Renvoie l'ensemble des messages de la conversation
     * @return messages de la conversation
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Envoie un message sur le flux sortant de la conversation
     * @param message message à envoyer
     */
    public void envoyerMessage(Message message) {
        out.println(((Texte) message).getContenu());
        this.messages.add(message);
    }

    /**
     * Retourne le socket de la conversation
     * @return socket de la conversation
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Réception des messages entrants
     */
    @Override
    // must send and receive objects instead of strings
    public void run() {
        String msg;
        while (true) {
            try {
                msg = this.in.readLine();
                if (msg != null) {
                    // works for single user conv only
                    Message message = new Texte(LocalDateTime.now(), msg, this.utilisateurs.get(0));
                    this.gestionnaireConv.receptionMessage(this, message);
                    this.messages.add(message);
                } else {
                    break;
                }
            } catch (SocketException se) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gestionnaireConv.fermerConversation(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return socket.getInetAddress().equals(that.socket.getInetAddress());
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
