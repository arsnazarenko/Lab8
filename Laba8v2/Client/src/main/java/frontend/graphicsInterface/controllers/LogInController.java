package frontend.graphicsInterface.controllers;


import frontend.ClientManager;
import frontend.graphicsInterface.Collection;
import frontend.graphicsInterface.loginForm.AuthorizationPanel;
import frontend.graphicsInterface.loginForm.LogInWindow;
import frontend.graphicsInterface.loginForm.RegistrationPanel;
import frontend.graphicsInterface.mainWindow.MainWindow;
import frontend.graphicsInterface.mainWindow.UserPanel;
import frontend.graphicsInterface.mainWindow.commands.CommandPanel;
import frontend.graphicsInterface.mainWindow.table.TablePanel;
import frontend.objectWindows.ObjectsMapView;
import library.clientCommands.SpecialSignals;
import library.clientCommands.UserData;
import library.clientCommands.commandType.LogCommand;
import library.clientCommands.commandType.RegCommand;

import javax.swing.*;
import java.util.ResourceBundle;

public class LogInController {
    private LogInWindow logInWindow;
    private ClientManager clientManager;
    private AuthorizationPanel authorizationPanel;
    private RegistrationPanel registrationPanel;
    private LogInWindow.Buttons buttons;
    private ObjectsMapView mapView;
    private UserData sessionUser;


    public LogInController(LogInWindow logInWindow, ClientManager clientManager, ObjectsMapView mapView) {
        this.logInWindow = logInWindow;
        this.clientManager = clientManager;
        this.mapView = mapView;
        authorizationPanel = logInWindow.getAuthorizationPanel();
        registrationPanel = logInWindow.getRegistrationPanel();
        buttons = logInWindow.getButtons();
    }

    public void addSignButtonListener(boolean flag) {
        authorizationPanel.getLabelModel().getLabelTitle().setVisible(!flag);
        buttons.getButtonLog().setVisible(!flag);
        buttons.getButtonSignUp().setVisible(!flag);

        registrationPanel.getLabelModel().getLabelTitle().setVisible(flag);
        registrationPanel.getLabelModel().getLabelRestatePassword().setVisible(flag);
        registrationPanel.getFieldModel().getPasswordRestateField().setVisible(flag);
        buttons.getButtonSignIn().setVisible(flag);
        buttons.getButtonReg().setVisible(flag);
    }

    public void addLogButtonListener() {

        String login = authorizationPanel.getFieldModel().getLoginField().getText();
        String password = new String(authorizationPanel.getFieldModel().getPasswordField().getPassword());
        UserData userData = new UserData(login, password);
        sessionUser = userData;
        clientManager.executeCommand(new LogCommand(userData));
    }
    public MainWindow logResponseHandle(SpecialSignals signal, Collection collection) {
        ResourceBundle bundle = ResourceBundle.getBundle("translate", Controllers.getLocale());
        boolean res = clientManager.handlerAuth(signal);
        if (res) {
            logInWindow.dispose();
            return createMain(sessionUser,collection);
        } else {
            UIManager.put("OptionPane.okButtonText"    , bundle.getString("ok"));
            JOptionPane.showMessageDialog(null,
                    bundle.getString("mg_error_1"), null,JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }



    public void addRegButtonListener() {
        ResourceBundle bundle = ResourceBundle.getBundle("translate",Controllers.getLocale());

        String login = authorizationPanel.getFieldModel().getLoginField().getText();
        String password = new String(authorizationPanel.getFieldModel().getPasswordField().getPassword());
        String restatePassword = new String(registrationPanel.getFieldModel().getPasswordRestateField().getPassword());
        if (password.equals(restatePassword)) {
            UserData userData = new UserData(login, password);
            sessionUser = userData;
            clientManager.executeCommand(new RegCommand(userData));

        } else {
            UIManager.put("OptionPane.okButtonText"    , bundle.getString("ok"));
            JOptionPane.showMessageDialog(null,
                    bundle.getString("mg_error_3"), null,JOptionPane.WARNING_MESSAGE);
        }
    }

    public MainWindow regResponseHandle(SpecialSignals signal, Collection collection) {
        ResourceBundle bundle = ResourceBundle.getBundle("translate",Controllers.getLocale());
        boolean res = clientManager.handlerAuth(signal);
        System.out.println(res);
        if (res) {
            logInWindow.dispose();
            return createMain(sessionUser, collection);
        } else {
            UIManager.put("OptionPane.okButtonText"    , bundle.getString("ok"));
            JOptionPane.showMessageDialog(null,
                    bundle.getString("mg_error_2"), null,JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    private MainWindow createMain(UserData userData, Collection collection) {
        clientManager.setUserData(userData);
        String FONT = logInWindow.getFONT();
        TablePanel tablePanel = new TablePanel(FONT,collection);
        UserPanel userPanel = new UserPanel(userData.getLogin(),FONT);
        CommandPanel commandPanel = new CommandPanel(FONT);
        return new MainWindow(FONT,commandPanel,tablePanel,userPanel, mapView);
    }
}
