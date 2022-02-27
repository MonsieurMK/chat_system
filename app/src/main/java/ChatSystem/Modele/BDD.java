package ChatSystem.Modele;


import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class BDD {
    private BDD() {}

    private static Connection conn;

    public static void connect() {
        AppDirs appDirs = AppDirsFactory.getInstance();
        String dataFolder = appDirs.getUserDataDir("ChatSystem", null, "MonsieurSinge");
        (new File(dataFolder)).mkdirs();

        String url = "jdbc:sqlite:" + dataFolder + File.separator + "database.bd";

        try {
            BDD.conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {
        String reqUtilisateurs = """
        CREATE TABLE IF NOT EXISTS Utilisateur(
           adresse_ip VARCHAR(15),
           pseudonyme VARCHAR(50),
           PRIMARY KEY(adresse_ip)
        );
                """;

        String reqConversation = """
        CREATE TABLE IF NOT EXISTS Conversation(
           id_conversation CHAR(36),
           nom VARCHAR(50),
           PRIMARY KEY(id_conversation)
        );
                """;

        String reqMessage = """
        CREATE TABLE IF NOT EXISTS Message(
           id_message CHAR(36),
           date_envoi DATETIME,
           adresse_ip VARCHAR(15) NOT NULL,
           id_conversation CHAR(36) NOT NULL,
           contenu TEXT,
           PRIMARY KEY(id_message),
           FOREIGN KEY(adresse_ip) REFERENCES Utilisateur(adresse_ip),
           FOREIGN KEY(id_conversation) REFERENCES Conversation(id_conversation)
        );
                """;

        String reqEstMembre = """
        CREATE TABLE IF NOT EXISTS EstMembre(
           adresse_ip VARCHAR(15),
           id_conversation CHAR(36),
           PRIMARY KEY(adresse_ip, id_conversation),
           FOREIGN KEY(adresse_ip) REFERENCES Utilisateur(adresse_ip),
           FOREIGN KEY(id_conversation) REFERENCES Conversation(id_conversation)
        );
                """;

        String[] reqs = {reqUtilisateurs, reqConversation, reqMessage, reqEstMembre};

        Statement stmt;
        try {
            stmt = conn.createStatement();
            for (String req :
                    reqs) {
                stmt.execute(req);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ajouterUtilisateur(Utilisateur utilisateur) {
        String req = """
            INSERT INTO utilisateur (adresse_ip, pseudonyme)
            VALUES (?, ?);
                """;
        try {
            PreparedStatement pstmt = conn.prepareStatement(req);
            pstmt.setString(1, utilisateur.getAddresseIP().getHostAddress());
            pstmt.setString(2, utilisateur.getPseudonyme());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ajouterConversation(Conversation conversation) {
        String reqConv = """
                INSERT INTO conversation (id_conversation, nom)
                VALUES (?, ?);
                """;
        String reqMemSelf = """
                INSERT INTO EstMembre (adresse_ip, id_conversation)
                VALUES (?, ?);
                """;
        String reqMem = """
                    INSERT INTO EstMembre (adresse_ip, id_conversation)
                    VALUES (?, ?);
                """;
        try {
            PreparedStatement pstmt = conn.prepareStatement(reqConv);
            pstmt.setString(1, conversation.getIdConversation().toString());
            pstmt.setString(2, conversation.getNom());
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = conn.prepareStatement(reqMemSelf);
            pstmt.setString(1, GestionnaireConv.getUtilisateurPrive().getAddresseIP().getHostAddress());
            pstmt.setString(2, conversation.getIdConversation().toString());
            pstmt.executeUpdate();
            pstmt.close();

            for (Utilisateur utilisateur :
                    conversation.getUtilisateurs()) {
                pstmt = conn.prepareStatement(reqMem);
                pstmt.setString(1, utilisateur.getAddresseIP().getHostAddress());
                pstmt.setString(2, conversation.getIdConversation().toString());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // for text only
    public static void ajouterMessage(Message message) {
        String reqMessage = """
                INSERT INTO message (id_message, date_envoi, adresse_ip, id_conversation, contenu)
                VALUES (?, ?, ?, ?, ?);
                """;
        try {
            PreparedStatement pstmt = conn.prepareStatement(reqMessage);
            pstmt.setString(1, message.getIdMessage().toString());
            pstmt.setTimestamp(2, Timestamp.valueOf(message.getDateEnvoi()));
            pstmt.setString(3, message.getEnvoyeur().getAddresseIP().getHostAddress());
            pstmt.setString(4, message.getConversation().getIdConversation().toString());
            pstmt.setString(5, ((Texte) message).getContenu());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changerPseudo(Utilisateur utilisateur, String pseudonyme) {
    }

    // for only one member
    // set l'id de la conversation si elle existe dans la bdd
    // return false si pas dans la bdd
    public static boolean chercherConversation(Conversation conversation) {
        Utilisateur utilisateur = conversation.getUtilisateurs().get(0);

        String req = """
                SELECT id_conversation
                FROM EstMembre
                WHERE adresse_ip = ?
                """;
        try {
            PreparedStatement pstmt = conn.prepareStatement(req);
            pstmt.setString(1, utilisateur.getAddresseIP().getHostAddress());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UUID idConversation = UUID.fromString(rs.getString(1));
                conversation.setIdConversation(idConversation);
                rs.close();
                pstmt.close();

                for (Message message :
                        getMessagesDeConv(conversation)) {
                    conversation.ajouterMessage(message);
                }

                return true;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // works for text only
    public static List<Message> getMessagesDeConv(Conversation conversation) {
        ArrayList<Message> result = new ArrayList<>();

        String reqMsg = """
                SELECT *
                FROM message
                WHERE id_conversation = ?;
                """;
        try {
            PreparedStatement pstmt = conn.prepareStatement(reqMsg);
            pstmt.setString(1, conversation.getIdConversation().toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Utilisateur envoyeur;
                InetAddress address = InetAddress.getByName(rs.getString("adresse_ip"));
                /*
                // for multiple users per conversation
                for (Utilisateur utilisateur :
                        conversation.getUtilisateurs()) {
                    if (utilisateur.getAddresseIP().equals(address)) {
                        envoyeur = utilisateur;
                    }
                }*/
                envoyeur = GestionnaireConv.getUtilisateurFromIP(address);
                result.add(new Texte(rs.getTimestamp("date_envoi").toLocalDateTime(),
                        rs.getString("contenu"),
                        envoyeur,
                        conversation));
            }
        } catch (SQLException | UnknownHostException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Utilisateur getUtilisateur(InetAddress adresseIp) {
        Utilisateur result = null;

        String req = """
                SELECT pseudonyme
                FROM Utilisateur
                WHERE adresse_ip = ?
                """;
        try {
            PreparedStatement pstmt = conn.prepareStatement(req);
            pstmt.setString(1, adresseIp.getHostAddress());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new Utilisateur(adresseIp, rs.getString("pseudonyme"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
