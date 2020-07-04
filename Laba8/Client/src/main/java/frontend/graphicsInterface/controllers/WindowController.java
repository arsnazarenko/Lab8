package frontend.graphicsInterface.controllers;



import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

public class WindowController{

    public void confirm(WindowEvent e){
        ResourceBundle bundle = ResourceBundle.getBundle("translate",Controllers.getLocale());
        UIManager.put("OptionPane.yesButtonText", bundle.getString("yes"));
        UIManager.put("OptionPane.noButtonText", bundle.getString("no"));
        int PromptResult = JOptionPane.showConfirmDialog(e.getWindow(),
                bundle.getString("mg_exit"),bundle.getString("confirmation"),JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (PromptResult == 0) {
            System.exit(0);
        }
    }



}
