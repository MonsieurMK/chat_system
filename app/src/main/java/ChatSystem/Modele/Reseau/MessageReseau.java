package ChatSystem.Modele.Reseau;

import java.io.Serializable;

public class MessageReseau implements Serializable {
    private final MessageReseauType type;
    private String username;

    public MessageReseau(MessageReseauType type, String username) {
        this.type = type;
        this.username = username;
    }

    public MessageReseau(MessageReseauType type) {
        this.type = type;
    }

    public MessageReseauType getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }
}
