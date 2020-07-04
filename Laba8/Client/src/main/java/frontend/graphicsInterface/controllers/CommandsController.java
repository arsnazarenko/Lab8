package frontend.graphicsInterface.controllers;

import frontend.ClientManager;
import frontend.graphicsInterface.mainWindow.MainWindow;
import frontend.objectWindows.ObjectsMapController;
import frontend.objectWindows.OrganizationController;
import library.clientCommands.Command;
import library.clientCommands.InfoCollection;
import library.clientCommands.commandType.*;
import library.сlassModel.Organization;

import javax.swing.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CommandsController{
    private ClientManager clientManager;
    private MainWindow mainWindow;
    private OrganizationController organizationCreator;
    private ObjectsMapController mapController;


    public CommandsController(ClientManager clientManager, OrganizationController organizationCreator, ObjectsMapController mapController) {
        this.clientManager = clientManager;
        this.organizationCreator = organizationCreator;
        this.mapController = mapController;
    }

    public void show() {
        clientManager.executeCommand(new ShowCommand(clientManager.getUserData()));
    }

    public void removeId(Long id) {
        if (id != null) {
            clientManager.executeCommand(new RemoveIdCommand(id, clientManager.getUserData()));
        }
    }

    public void clear() {
        clientManager.executeCommand(new ClearCommand(clientManager.getUserData()));
    }

    public void filter() {
        String subString = validateName();
        if (subString != null) {
            clientManager.executeCommand(new FilterContainsNameCommand(subString, clientManager.getUserData()));
        }
    }

    public void head(){
        clientManager.executeCommand(new HeadCommand(clientManager.getUserData()));
    }

    public void max(){
        clientManager.executeCommand(new MaxByEmployeeCommand(clientManager.getUserData()));
    }

    public void print(){
        clientManager.executeCommand(new PrintAscendingCommand(clientManager.getUserData()));
    }

    public void info(){
        clientManager.executeCommand(new InfoCommand(clientManager.getUserData()));
    }

    public void load(){
        mainWindow.getLoader().addActionListener(a-> {
            ResourceBundle bundle = ResourceBundle.getBundle("translate",Controllers.getLocale());
            UIManager.put(
                    "FileChooser.openButtonText", bundle.getString("open"));
            UIManager.put(
                    "FileChooser.cancelButtonText", bundle.getString("cancel"));
            UIManager.put(
                    "FileChooser.fileNameLabelText", bundle.getString("name_file"));
            UIManager.put(
                    "FileChooser.filesOfTypeLabelText", bundle.getString("type_file"));
            UIManager.put(
                    "FileChooser.lookInLabelText", bundle.getString("dir"));
            UIManager.put(
                    "FileChooser.openInLabelText", bundle.getString("open_Label"));
            UIManager.put(
                    "FileChooser.folderNameLabelText", bundle.getString("path"));
            JFileChooser fileChooser = new JFileChooser();
            // Определение режима - только файл
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            // Если файл выбран, то получаем его
            int ret = fileChooser.showDialog(null, bundle.getString("open"));
                if (ret == JFileChooser.APPROVE_OPTION) {
                    String file = fileChooser.getSelectedFile().getPath();
                    Deque<Command> commands = clientManager.createScriptQueue(new ExecuteScriptCommand(file, clientManager.getUserData()));
                    for (Command command : commands) {
                        if (command != null) {
                            clientManager.executeCommand(command);
                        }
                    }
                }
        });
    }

    public void handle(Object answerObject) {
        ResourceBundle bundle = ResourceBundle.getBundle("translate",Controllers.getLocale());
        UIManager.put("OptionPane.okButtonText", bundle.getString("ok"));
        System.out.println(answerObject instanceof Deque);
        if (answerObject instanceof Deque) {
            System.out.println(answerObject);
            Deque<Organization> organizations = ((Deque<?>) answerObject).stream().map(o -> (Organization) o).collect(Collectors.toCollection(ArrayDeque::new));
            mapController.updateObjectsMapView(organizations);
            mainWindow.getTablePanel().getCollection().setCollection(new ArrayList<>(organizations));
            mainWindow.getTablePanel().updateData();
        } else if(answerObject instanceof Organization){
            Object [] org = mainWindow.getTablePanel().getCollection().toLocalizedArray(Controllers.getLocale(),(Organization)answerObject);
            JOptionPane.showMessageDialog(null, Arrays.toString(org),null,JOptionPane.INFORMATION_MESSAGE);
        } else if (answerObject instanceof InfoCollection){
            InfoCollection info = (InfoCollection)answerObject;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(bundle.getString("name_collection")).append(": ").append(info.getName()).append("\n");
            stringBuilder.append(bundle.getString("count")).append(": ").append(NumberFormat.getNumberInstance(Controllers.getLocale()).format(info.getCount())).append("\n");
            stringBuilder.append(bundle.getString("date")).append(": ").append(DateFormat.getDateInstance(DateFormat.SHORT,Controllers.getLocale()).format(info.getDate()));
            JOptionPane.showMessageDialog(null,stringBuilder,null,JOptionPane.INFORMATION_MESSAGE);
        }
        else if (answerObject == null) {
            JOptionPane.showMessageDialog(null,bundle.getString("nul"),null,JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Long validateId() {
        ResourceBundle bundle = ResourceBundle.getBundle("translate",Controllers.getLocale());
        UIManager.put("OptionPane.okButtonText", bundle.getString("ok"));
        UIManager.put("OptionPane.cancelButtonText", bundle.getString("cancel"));
        Long id;
        String result = JOptionPane.showInputDialog(null,
                bundle.getString("id_massage"),null,JOptionPane.QUESTION_MESSAGE);
        id = clientManager.getArgumentValidator().idValid(result);
        if (id == null) {
                JOptionPane.showMessageDialog(null,
                        bundle.getString("error_5"), bundle.getString("error_title"),JOptionPane.ERROR_MESSAGE);
        }
        return id;
    }

    public String validateName() {
        ResourceBundle bundle = ResourceBundle.getBundle("translate",Controllers.getLocale());
        UIManager.put("OptionPane.okButtonText", bundle.getString("ok"));
        UIManager.put("OptionPane.cancelButtonText", bundle.getString("cancel"));
        String name;
        String result = JOptionPane.showInputDialog(null,
                bundle.getString("str_massage"),null,JOptionPane.QUESTION_MESSAGE);
        name = clientManager.getArgumentValidator().subStringIsValid(result);
        if (name == null) {
            JOptionPane.showMessageDialog(null,
                    bundle.getString("error_6"), bundle.getString("error_title"),JOptionPane.ERROR_MESSAGE);
        }
        return name;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void add() {
        organizationCreator.runCreation(AddCommand.class);
    }

    public void addIfMin() {
        organizationCreator.runCreation(AddIfMinCommand.class);
    }

    public void removeLower() {
        organizationCreator.runCreation(RemoveLowerCommand.class);
    }

    public void updateId(Long id) {
        if (id != null) {
            organizationCreator.setIdForUpdate(id);
            organizationCreator.runCreation(UpdateIdCommand.class);
        }
    }
}
