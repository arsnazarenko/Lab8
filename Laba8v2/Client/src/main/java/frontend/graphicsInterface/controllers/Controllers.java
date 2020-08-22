package frontend.graphicsInterface.controllers;


import client.servises.ObjectDataValidator;
import frontend.ClientManager;
import frontend.graphicsInterface.Collection;
import frontend.graphicsInterface.LocaleActionListener;
import frontend.graphicsInterface.Menu;
import frontend.graphicsInterface.loginForm.LogInWindow;
import frontend.graphicsInterface.mainWindow.MainWindow;
import frontend.graphicsInterface.mainWindow.commands.CommandPanel;
import frontend.graphicsInterface.mainWindow.table.TablePanel;
import frontend.objectWindows.*;
import library.clientCommands.SpecialSignals;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class Controllers {
    private static Locale locale;
    private LogInWindow logInWindow;
    private MainWindow mainWindow;
    private Menu menu;
    private LogInController logInController;
    private CommandsController commandsController;
    private WindowController windowController;
    private List<LocaleActionListener> listeners = new ArrayList<>();
    private OrganizationController organizationCreateController;
    private OrganizationController organizationCreateControllerForMap;
    private ObjectsMapController mapController;
    private Collection collection;

    public Controllers(LogInWindow logInWindow, ClientManager clientManager, Menu menu, Locale locale) {
        this.logInWindow = logInWindow;
        this.menu = menu;
        this.locale = locale;
        collection = new Collection();
        organizationCreateController = new OrganizationController(new OrganizationView(), new ObjectCreatorUI(new ObjectDataValidator()), clientManager,collection);
        organizationCreateControllerForMap = new OrganizationController(new OrganizationView(), new ObjectCreatorUI(new ObjectDataValidator()), clientManager,collection);
        ObjectsMapModel mapModel = new ObjectsMapModel(20, organizationCreateControllerForMap);
        ObjectsMapModel.Entity entity =  mapModel.generateIcons(new ArrayDeque<>());
        mapController = new ObjectsMapController(new ObjectsMapView(mapModel.getCellSize(), entity.getCellCount()),mapModel);
        logInController = new LogInController(logInWindow, clientManager, mapController.getView());
        commandsController = new CommandsController(clientManager, organizationCreateController, mapController);
        windowController = new WindowController();
        listeners.add(menu);
        listeners.add(logInWindow);
        listeners.add(logInWindow.getAuthorizationPanel());
        listeners.add(logInWindow.getRegistrationPanel());
//        listeners.add(mapController.getView());
        listeners.add(organizationCreateController.getView());
    }

    public static Locale getLocale() {
        return locale;
    }

    public void setLogListeners() {
        LogInWindow.Buttons buttons = logInWindow.getButtons();
        buttons.getButtonLog().addActionListener(a -> {
            logInController.addLogButtonListener();

        });
        buttons.getButtonSignUp().addActionListener(a -> logInController.addSignButtonListener(true));
        buttons.getButtonSignIn().addActionListener(a -> logInController.addSignButtonListener(false));
        buttons.getButtonReg().addActionListener(a -> {
            logInController.addRegButtonListener();

        });
    }

    public void logResponseHandle(SpecialSignals signal) {
        mainWindow = logInController.logResponseHandle(signal,collection);
        if (mainWindow != null) setMainWindow();
    }

    public void regResponseHandle(SpecialSignals signal) {
        mainWindow = logInController.regResponseHandle(signal,collection);
        if (mainWindow != null) setMainWindow();
    }

    public void commandResponseHandle(Object response) {
        System.out.println(response);
        commandsController.handle(response);
    }

    private void setMainWindow() {
        listeners.add(mainWindow.getCommandPanel());
        listeners.add(mainWindow.getTablePanel());
        listeners.add(mainWindow.getUserPanel());
        listeners.add(mainWindow);
        mainWindow.setJMenuBar(logInWindow.getJMenuBar());
        mainWindow.getJMenuBar().add(mainWindow.getFile());
        commandsController.setMainWindow(mainWindow);
        setCommandsListeners();
        setTableListeners();
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowController.confirm(e);
            }
        });
        mainWindow.setVisible(true);
    }

    private void setCommandsListeners() {
        CommandPanel.Buttons buttons = mainWindow.getCommandPanel().getButtons();
        buttons.getShowButton().addActionListener(a -> commandsController.show());
        buttons.getClearButton().addActionListener(a -> commandsController.clear());
        buttons.getFilterNameButton().addActionListener(a -> commandsController.filter());
        buttons.getHeadButton().addActionListener(a -> commandsController.head());
        buttons.getMaxEmplButton().addActionListener(a -> commandsController.max());
        buttons.getPrintAscendButton().addActionListener(a -> commandsController.print());
        buttons.getRemoveIdButton().addActionListener(a -> commandsController.removeId(commandsController.validateId()));
        buttons.getInfoButton().addActionListener(a -> commandsController.info());
        buttons.getAddButton().addActionListener(a -> commandsController.add());
        buttons.getAddIfMinButton().addActionListener(a -> commandsController.addIfMin());
        buttons.getRemoveLowerButton().addActionListener(a -> commandsController.removeLower());
        buttons.getUpdateIdButton().addActionListener(a -> commandsController.updateId(commandsController.validateId()));
        commandsController.load();
    }

    private void setTableListeners() {
        TablePanel.ButtonsPanel buttonsPanel = mainWindow.getTablePanel().getButtonsPanel();
        JTable table = mainWindow.getTablePanel().getTable();
        buttonsPanel.getRemoveButton().addActionListener(a ->
        {
            try {
                commandsController.removeId(Long.parseLong((String) table.getValueAt(table.getSelectedRow(), 0)));
            } catch (NumberFormatException e) {
                ResourceBundle bundle = ResourceBundle.getBundle("translate", Controllers.getLocale());
                UIManager.put("OptionPane.okButtonText", bundle.getString("ok"));
                JOptionPane.showMessageDialog(null, bundle.getString("error_8"), bundle.getString("error_title"), JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonsPanel.getUpdateButton().addActionListener(a -> {
            try {
                commandsController.updateId(Long.parseLong((String) table.getValueAt(table.getSelectedRow(), 0)));
            } catch (NumberFormatException e) {
                ResourceBundle bundle = ResourceBundle.getBundle("translate", Controllers.getLocale());
                UIManager.put("OptionPane.okButtonText", bundle.getString("ok"));
                JOptionPane.showMessageDialog(null, bundle.getString("error_8"), bundle.getString("error_title"), JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void setMenuListeners() {
        menu.getRussian().addActionListener(a -> {
            locale = new Locale("ru");
            setLanguage(locale);
        });
        menu.getSpanish().addActionListener(a -> {
            locale = new Locale("es_GT");
            setLanguage(locale);
        });
        menu.getPolish().addActionListener(a -> {
            locale = new Locale("pl");
            setLanguage(locale);
        });
        menu.getSlovak().addActionListener(a -> {
            locale = new Locale("sk");
            setLanguage(locale);
        });

    }

    public void setWindowListener() {
        logInWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowController.confirm(e);
            }
        });
    }

    private void setLanguage(Locale locale) {
        listeners.forEach(o -> o.localeChange(locale));
    }
}
