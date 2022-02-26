package ChatSystem.Vue;

import ChatSystem.Controlleur.MainController;
import ChatSystem.Modele.Conversation;
import ChatSystem.Modele.Message;
import ChatSystem.Modele.Texte;
import ChatSystem.Modele.Utilisateur;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.ResourceBundle;

@SuppressWarnings({"rawtypes", "Convert2Lambda", "unchecked", "SameParameterValue"})
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

    private final MainController mainController;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM HH:mm:ss");

    public MainFrame(String title, boolean isFullscreen, MainController mainController) {
        super(title);

        this.mainController = mainController;

        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        if (isFullscreen) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        this.setVisible(true);

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
        usersList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (usersList.getSelectedValue() != null) {
                        MainFrame.this.mainController.openConversation((Utilisateur) usersList.getSelectedValue());
                    }
                }
            }
        });

        // conversation clicked on conversations list
        conversationsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    MainFrame.this.displayConversation((Conversation) conversationsList.getSelectedValue());
                }
            }
        });
    }

    private void sendMessage() {
        if (!sendingMessageField.getText().isBlank()) {
            Texte message = new Texte(LocalDateTime.now(), sendingMessageField.getText(), this.mainController.getUtilisateurPrive());
            this.mainController.sendMessage(message);
            this.getMessageListModel().addElement(displayTexte(message));
            sendingMessageField.setText("");
        }
    }

    public void receiveMessage(Message message) {
        // txt only for now
        Texte txt = (Texte) message;
        this.getMessageListModel().addElement(displayTexte((Texte) message));
    }

    public void addUserToList(Utilisateur utilisateur) {
        this.getUserListModel().addElement(utilisateur);
    }

    public void refreshUserList() {
        this.getUserListModel().clear();
    }

    public void addConversationToList(Conversation conversation) {
        this.getConversationListModel().addElement(conversation);
    }

    public void setSendMessageState(boolean state) {
        this.sendingMessageField.setEnabled(state);
        this.sendButton.setEnabled(state);
    }

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

    public static String displayTexte(Texte texte) {
        return "(" + texte.getDateEnvoi().format(DATE_FORMAT) + ")" + texte.getEnvoyeur().getPseudonyme() + ">" + texte.getContenu();
    }

    public void removeConversation(Conversation conversation) {
        this.getConversationListModel().removeElement(conversation);
    }

    public void clearDisplayedMessages() {
        this.getMessageListModel().clear();
    }

    private DefaultListModel getConversationListModel() {
        return (DefaultListModel) this.conversationsList.getModel();
    }

    private DefaultListModel getUserListModel() {
        return (DefaultListModel) this.usersList.getModel();
    }

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
        gbc.gridy = 0;
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
        gbc.gridy = 1;
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
