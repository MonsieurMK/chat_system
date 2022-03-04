package chat_system.vue;

import chat_system.controlleur.MainController;
import chat_system.modele.Conversation;
import chat_system.modele.Message;
import chat_system.modele.Texte;
import chat_system.modele.Utilisateur;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Interface graphique de l'application
 */
@SuppressWarnings({"rawtypes", "Convert2Lambda", "unchecked", "SameParameterValue", "ConstantConditions"})
public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel sidePanel;
    private JPanel centerPanel;
    private JScrollPane usersPanel;
    private JScrollPane conversationsPanel;
    private JList usersList;
    private JList conversationsList;
    private JList messagesList;
    private JTextField sendingMessageField;
    private JButton sendButton;
    private JPanel sendingPanel;
    private JPanel messageOptionPanel;
    private JButton closeConversationButton;

    /**
     * Controlleur principal
     */
    private final MainController mainController;

    /**
     * Formatteur de date et d'heure
     */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM HH:mm:ss");

    private Conversation currentConversation = null;

    /**
     * Crée l'interface graphique
     *
     * @param title          titre de la fenêtre
     * @param isFullscreen   vrai si en plein écran
     * @param mainController controlleur principal
     */
    public MainFrame(String title, boolean isFullscreen, MainController mainController) {
        super(title);

        this.mainController = mainController;

        ImageIcon img = new ImageIcon(this.getClass().getResource("/Logos/logo_sans_texte.png"));
        this.setIconImage(img.getImage());

        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        if (isFullscreen) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainFrame.this.mainController.deconnexion();
            }
        });

        // send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.sendMessage();
            }
        });

        // enter key pressed on message
        sendingMessageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    MainFrame.this.sendMessage();
                }
            }
        });

        // user clicked on users list
        usersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    MainFrame.this.mainController.openConversation((Utilisateur) MainFrame.this.getUserListModel().get(index));
                }
            }
        });

        /*usersList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (usersList.getSelectedValue() != null) {
                        MainFrame.this.mainController.openConversation((Utilisateur) usersList.getSelectedValue());
                    }
                }
            }
        });*/

        // conversation clicked on conversations list
        conversationsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    Conversation conversation = (Conversation) MainFrame.this.getConversationListModel().get(index);
                    MainFrame.this.displayConversation(conversation);
                    MainFrame.this.currentConversation = conversation;
                }
            }
        });

        /*conversationsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Conversation conversation = (Conversation) conversationsList.getSelectedValue();
                    MainFrame.this.displayConversation(conversation);
                    MainFrame.this.currentConversation = conversation;
                }
            }
        });*/

        // close conversation button clicked
        closeConversationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Conversation conversation = (Conversation) conversationsList.getSelectedValue();
                if (conversation == null) {
                    MainFrame.this.mainController.closeConversation(MainFrame.this.currentConversation);
                } else {
                    MainFrame.this.mainController.closeConversation((Conversation) conversationsList.getSelectedValue());
                }
                // might directly just use current conversation ?
            }
        });
    }

    public void setCurrentConversation(Conversation currentConversation) {
        this.currentConversation = currentConversation;
    }

    /**
     * Envoi un message écrit dans la zone de texte
     */
    private void sendMessage() {
        if (!sendingMessageField.getText().isBlank()) {
            Texte message = new Texte(LocalDateTime.now(), sendingMessageField.getText(), this.mainController.getUtilisateurPrive(), this.currentConversation);
            this.mainController.sendMessage(message);
            this.getMessageListModel().addElement(displayTexte(message));
            sendingMessageField.setText("");
        }
    }

    /**
     * Affiche un message reçu
     *
     * @param message message reçu
     */
    public void receiveMessage(Message message) {
        // txt only for now
        Texte txt = (Texte) message;
        this.getMessageListModel().addElement(displayTexte((Texte) message));
    }

    /**
     * Ajoute un utilisateur aux utilisateurs détectés et affichés
     *
     * @param utilisateur utilisateur à afficher
     */
    public void addUserToList(Utilisateur utilisateur) {
        this.getUserListModel().addElement(utilisateur);
    }

    public void removeUserFromList(Utilisateur utilisateur) {
        if (this.getUserListModel().contains(utilisateur)) {
            this.getUserListModel().removeElement(utilisateur);
        }
    }

    /**
     * Vide la liste des utilisateurs détectés
     *
     * @deprecated
     */
    @Deprecated
    public void refreshUserList() {
        this.getUserListModel().clear();
    }

    /**
     * Affiche une conversation sur la liste
     *
     * @param conversation conversation à afficher
     */
    public void addConversationToList(Conversation conversation) {
        this.getConversationListModel().addElement(conversation);
    }

    public void removeConversationFromList(Conversation conversation) {
        if (this.getConversationListModel().contains(conversation)) {
            this.getConversationListModel().removeElement(conversation);
        }
    }

    /**
     * Permet ou non l'écriture et l'envoi de messages
     *
     * @param state vrai si permis, faux sinon
     */
    public void setSendMessageState(boolean state) {
        this.sendingMessageField.setEnabled(state);
        this.sendButton.setEnabled(state);
    }

    public void setCloseConversationState(boolean state) {
        this.closeConversationButton.setEnabled(state);
    }

    /**
     * Affiche le contenu d'une conversation
     *
     * @param conversation conversation à afficher
     */
    public void displayConversation(Conversation conversation) {
        this.getMessageListModel().clear();
        if (conversation != null) {
            // for txt messages only
            for (Message message :
                    conversation.getMessages()) {
                this.getMessageListModel().addElement(displayTexte((Texte) message));
            }

        }
    }

    /**
     * Formate texte dans la zone de messages
     *
     * @param texte texte à afficher
     * @return texte formaté
     */
    public static String displayTexte(Texte texte) {
        return "(" + texte.getDateEnvoi().format(DATE_FORMAT) + ")" + texte.getEnvoyeur().getPseudonyme() + ">" + texte.getContenu();
    }

    /**
     * Retire une conversation de la liste
     *
     * @param conversation conversation à retirer
     */
    public void removeConversation(Conversation conversation) {
        this.getConversationListModel().removeElement(conversation);
    }

    /**
     * Efface les messages affichés
     */
    public void clearDisplayedMessages() {
        this.getMessageListModel().clear();
    }

    /**
     * Retourne la liste modifiable des conversations
     *
     * @return liste modifiable
     */
    private DefaultListModel getConversationListModel() {
        return (DefaultListModel) this.conversationsList.getModel();
    }

    /**
     * Retourne la liste modifiable des utilisateurs
     *
     * @return liste modifiable
     */
    private DefaultListModel getUserListModel() {
        return (DefaultListModel) this.usersList.getModel();
    }

    /**
     * Retourne la liste modifiable des messages
     *
     * @return liste modifiable
     */
    private DefaultListModel getMessageListModel() {
        return (DefaultListModel) this.messagesList.getModel();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(sidePanel, gbc);
        usersPanel = new JScrollPane();
        sidePanel.add(usersPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        usersList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        usersList.setModel(defaultListModel1);
        usersPanel.setViewportView(usersList);
        conversationsPanel = new JScrollPane();
        sidePanel.add(conversationsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        conversationsList = new JList();
        conversationsList.setEnabled(true);
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        conversationsList.setModel(defaultListModel2);
        conversationsPanel.setViewportView(conversationsList);
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 3.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(centerPanel, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 15.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(scrollPane1, gbc);
        messagesList = new JList();
        final DefaultListModel defaultListModel3 = new DefaultListModel();
        messagesList.setModel(defaultListModel3);
        scrollPane1.setViewportView(messagesList);
        sendingPanel = new JPanel();
        sendingPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(sendingPanel, gbc);
        sendingMessageField = new JTextField();
        sendingMessageField.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 2.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 10;
        sendingPanel.add(sendingMessageField, gbc);
        sendButton = new JButton();
        sendButton.setEnabled(false);
        this.$$$loadButtonText$$$(sendButton, this.$$$getMessageFromBundle$$$("main_panel", "send"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        sendingPanel.add(sendButton, gbc);
        messageOptionPanel = new JPanel();
        messageOptionPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(messageOptionPanel, gbc);
        closeConversationButton = new JButton();
        closeConversationButton.setEnabled(false);
        this.$$$loadButtonText$$$(closeConversationButton, this.$$$getMessageFromBundle$$$("main_panel", "close"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        messageOptionPanel.add(closeConversationButton, gbc);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
