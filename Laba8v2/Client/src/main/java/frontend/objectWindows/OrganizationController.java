package frontend.objectWindows;

import frontend.ClientManager;
import frontend.graphicsInterface.Collection;
import frontend.graphicsInterface.controllers.Controllers;
import library.clientCommands.Command;
import library.clientCommands.UserData;
import library.clientCommands.commandType.UpdateIdCommand;
import library.сlassModel.Address;
import library.сlassModel.Location;
import library.сlassModel.Organization;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OrganizationController {

    private OrganizationView view;
    private ObjectCreatorUI objectCreator;
    private ClientManager clientManager;

    private Class<?> commandClass;
    private Long idForUpdate;

    private boolean addressEnabled = false;
    private boolean townEnabled = false;
    private Collection collection;

    public OrganizationController(OrganizationView view, ObjectCreatorUI objectCreator, ClientManager clientManager, Collection collection) {
        this.clientManager = clientManager;
        this.view = view;
        this.objectCreator = objectCreator;
        this.objectCreator.setView(view);
        this.collection = collection;
        initController();
    }

    public void runCreation(Class<?> commandClass) {
        this.commandClass = commandClass;
        if(commandClass == UpdateIdCommand.class){
            NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
            Organization organization = collection.getCollection().stream().filter(o->o.getId().equals(idForUpdate)).findFirst().get();
            Address address = organization.getOfficialAddress();
            Location location = address==null?null:address.getTown();
            view.getNameText().setText(organization.getName());
            view.getCoordsXText().setText(nf.format(organization.getCoordinates().getX()));
            view.getCoordsYText().setText(nf.format(organization.getCoordinates().getY()));
            view.getEmployeesCountText().setText(nf.format(organization.getEmployeesCount()));
            view.getTypeBox().setSelectedIndex(organization.getType().ordinal());
            view.getAnnualTurnoverText().setText(organization.getAnnualTurnover()==null?"":nf.format(organization.getAnnualTurnover()));
            if (address!=null){
                view.getAddressRadioButton().doClick();
                view.getAddressStreetText().setText(address.getStreet());
                view.getAddressZipCodeText().setText(address.getZipCode());
                if(location!=null){
                    view.getTownRadioButton().doClick();
                    view.getTownXText().setText(nf.format(location.getX()));
                    view.getTownYText().setText(nf.format(location.getY()));
                    view.getTownZText().setText(nf.format(location.getZ()));
                    view.getTownNameText().setText(location.getName());
                }
            }
        }
        view.getFrame().setVisible(true);
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public void unsetVisibleOrganizationCreator() {
        view.getFrame().setVisible(false);
    }

    private void initController() {
        view.getAddressRadioButton().addActionListener(e -> addressManage());
        view.getTownRadioButton().addActionListener(e -> townManage());
        view.getSaveObjectButton().addActionListener(e -> saveObject());
        view.getClearButton().addActionListener(e -> clear());
        view.getFrame().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                clear();
                e.getWindow().setVisible(false);
            }
        });
    }

    private void clear() {
        JRadioButton addressButton = view.getAddressRadioButton();
        if (addressButton.isSelected()) {
            addressButton.doClick();
        }
        view.getTypeBox().setSelectedIndex(0);
        view.getOrganizationTextComponents().forEach(c -> c.setText(""));
    }

    private void saveObject() {
        ResourceBundle bundle = ResourceBundle.getBundle("translate", Controllers.getLocale());
        Organization organization = null;
        try {
            organization = objectCreator.create(addressEnabled, townEnabled);
        } catch (ParseException e) {

        }
        System.out.println(organization);
        if (organization == null) {
            StringBuilder sb = new StringBuilder(bundle.getString("fields") + ": "+ "\n");
            objectCreator.getValidateResults().forEach(s -> sb.append(s + "\n"));
            UIManager.put("OptionPane.okButtonText", bundle.getString("ok"));
            JOptionPane.showMessageDialog(null, sb.toString(), bundle.getString("error_fields"), JOptionPane.ERROR_MESSAGE);
        } else {
            Command command = createCommand(commandClass, organization);
            if (command != null)
            clientManager.executeCommand(command);
            this.commandClass = null;
            this.idForUpdate = null;
            clear();
            unsetVisibleOrganizationCreator();
        }

    }

    private Command createCommand(Class<?> commandClass, Organization organization) {

        try {
            if(commandClass == UpdateIdCommand.class) {
                return (Command) commandClass.getDeclaredConstructor(Organization.class, Long.class, UserData.class).newInstance(organization, idForUpdate , clientManager.getUserData());
            }
            return (Command) commandClass.getDeclaredConstructor(Organization.class, UserData.class).newInstance(organization, clientManager.getUserData());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void townManage() {
        if(!townEnabled) {
            view.getTownComponents().forEach(c -> c.setEnabled(true));
            townEnabled = true;
        } else {
            view.getTownTextComponents().forEach(c -> c.setText(""));
            view.getTownComponents().forEach(c -> c.setEnabled(false));
            townEnabled = false;
        }
    }

    private void addressManage() {
        JRadioButton townRadioButton = view.getTownRadioButton();
        if (!addressEnabled) {
            view.getAddressComponents().forEach(c -> c.setEnabled(true));
            addressEnabled = true;
        } else {

            if (townRadioButton.isSelected()) {
                townRadioButton.doClick();
            }
            view.getTownTextComponents().forEach(c -> c.setText(""));
            view.getAddressTextComponents().forEach(c -> c.setText(""));
            view.getTownComponents().forEach(c -> c.setEnabled(false));
            view.getAddressComponents().forEach(c -> c.setEnabled(false));
            addressEnabled = false;
            townEnabled = false;
        }

    }

    public void setIdForUpdate(Long idForUpdate) {
        this.idForUpdate = idForUpdate;
    }

    //    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new OrganizationController(new OrganizationView(), new ObjectCreatorUI(new ObjectDataValidator())));
//    }


    public OrganizationView getView() {
        return view;
    }
}
