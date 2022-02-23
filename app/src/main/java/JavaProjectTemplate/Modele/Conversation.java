package JavaProjectTemplate.Modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Conversation extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintStream out;

    private String nom;

    public List<Utilisateur> utilisateurs = new ArrayList<>();

    public UtilisateurPrive utilisateurPrive;

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

    public Conversation(String nom, Utilisateur utilisateur, Socket socket) {
        this.nom = nom;
        this.utilisateurs = new ArrayList<>();
        this.utilisateurs.add(utilisateur);
        this.socket = socket;
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
    }

    public void ajouterMessage(Message message, Utilisateur envoyeur) {
    }

    public void envoyerMessage(Message message) {
        out.println(((Texte) message).getContenu());
    }

    @Override
    public void run() {
        System.out.println("Listening for new messages");
        String msg = null;
        while (true) {
            try {
                msg = this.in.readLine();
                System.out.println("RECEIVED MSG: " + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
