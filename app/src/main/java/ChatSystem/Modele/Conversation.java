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

public class Conversation extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintStream out;

    private String nom;

    private List<Utilisateur> utilisateurs = new ArrayList<>();

    private List<Message> messages = new ArrayList<>();

    private UtilisateurPrive utilisateurPrive;

    private GestionnaireConv gestionnaireConv;

    public String getNom() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.nom;
    }

    public void setNom(String value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.nom = value;
        this.utilisateurs = new ArrayList<>();
    }

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

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
    }

    public void ajouterMessage(Message message) {
        this.messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void envoyerMessage(Message message) {
        out.println(((Texte) message).getContenu());
        this.messages.add(message);
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    // must send and receive objects instead of strings
    public void run() {
        System.out.println("Listening for new messages");
        String msg = null;
        while (true) {
            try {
                msg = this.in.readLine();
                System.out.println("RECEIVED MSG: " + msg);
                // works for single user conv only
                Message message = new Texte(LocalDateTime.now(), msg, this.utilisateurs.get(0));
                this.gestionnaireConv.receptionMessage(this, message);
                this.messages.add(message);
            } catch (SocketException se) {
                System.out.println("lost connection");
                this.gestionnaireConv.fermerConversation(this);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return socket.equals(that.socket);
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
