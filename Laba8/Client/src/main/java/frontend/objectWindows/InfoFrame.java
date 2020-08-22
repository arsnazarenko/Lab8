package frontend.objectWindows;

import frontend.graphicsInterface.controllers.Controllers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public class InfoFrame extends JFrame{
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JButton removeButton;
    private JButton updateButton;
    private final String FONT = "Century Gothic";

    public InfoFrame(){

        createFrame();
    }

    private void createFrame() {
        ResourceBundle bundle = ResourceBundle.getBundle("translate",Controllers.getLocale());
        setSize(600, 200);
        getContentPane().setBackground(Color.ORANGE);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().setVisible(false);
                e.getWindow().dispose();
            }
        });

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(FONT,Font.PLAIN,12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        removeButton = new JButton(bundle.getString("remove"));
        removeButton.setFont(new Font(FONT,Font.PLAIN,12));
        removeButton.setBackground(Color.LIGHT_GRAY);
        removeButton.setFocusPainted(false);
        updateButton = new JButton(bundle.getString("update"));
        updateButton.setFont(new Font(FONT,Font.PLAIN,12));
        updateButton.setBackground(Color.LIGHT_GRAY);
        updateButton.setFocusPainted(false);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 1, 1));
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        add(scrollPane,BorderLayout.CENTER);
        add(buttonPanel,BorderLayout.SOUTH);
        setVisible(true);

    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public void setRemoveButton(JButton removeButton) {
        this.removeButton = removeButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public void setUpdateButton(JButton updateButton) {
        this.updateButton = updateButton;
    }
}
