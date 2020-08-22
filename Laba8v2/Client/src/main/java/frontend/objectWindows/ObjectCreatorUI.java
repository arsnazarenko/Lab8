package frontend.objectWindows;

import client.servises.ObjectDataValidator;
import frontend.graphicsInterface.controllers.Controllers;
import library.сlassModel.*;
import javax.swing.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ObjectCreatorUI {
    private List<String> validateResults;
    private ObjectDataValidator validator;
    private OrganizationView view;


    public ObjectCreatorUI(ObjectDataValidator validator) {
        this.validator = validator;
    }

    public Organization create(boolean addressEnabled, boolean townEnabled) throws ParseException {
        this.validateResults = new ArrayList<>();
        String name = setName();
        double coordX = setX();
        float coordY = setY();
        Double annualTurnover = setAnnualTurnover();
        Integer employeesCount = setEmployeesCount();
        OrganizationType type = setOrganizationType();
        Address officialAddress = null;
        Location town = null;
        if(addressEnabled) {
            String street = setStreet();
            String zipCode = setZipCode();
            if(townEnabled) {
                Long x = setTownX();
                Double y = setTownY();
                Double z = setTownZ();
                String townName = setTownName();
                town = new Location(x, y, z, townName);
            }
            officialAddress = new Address(street, zipCode, town);
        }

        if(!validateResults.isEmpty()) {
            return null;
        }
        System.out.println(name + " " + coordX + " " + coordY + " " + annualTurnover + " " + employeesCount + " " + type + " " + officialAddress);
        return new Organization(null, null, name, new Coordinates(coordX, coordY), null,
                employeesCount, type, annualTurnover, officialAddress);
    }

    private double setTownZ() throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        JTextField zText = view.getTownZText();
        String str = zText.getText();
        if (validator.locationZValidate(str)) {
            return nf.parse(str).doubleValue();
        }
        addIncorrectFieldMessage(zText);
        return 0;
    }

    private String setTownName() {
        JTextField nameText = view.getTownNameText();
        String str = nameText.getText();
        if (validator.stringFieldValidate(str)) {
            return str;
        }
        addIncorrectFieldMessage(nameText);
        return null;
    }

    private Double setTownY() throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        JTextField yText = view.getTownYText();
        String str = yText.getText();
        if (validator.locationYValidate(str)) {
            return nf.parse(str).doubleValue();
        }
        addIncorrectFieldMessage(yText);
        return null;
    }

    private Long setTownX() throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        JTextField xText = view.getTownXText();
        String str = xText.getText();
        if (validator.locationXValidate(str)) {
            return nf.parse(str).longValue();
        }
        addIncorrectFieldMessage(xText);
        return null;
    }

    private String setZipCode() {
        JTextField zipCodeText = view.getAddressZipCodeText();
        String str = zipCodeText.getText();
        if (validator.zipCodeValidate(str)) {
            return str;
        }
        addIncorrectFieldMessage(zipCodeText);
        return null;
    }

    private String setStreet() {
        JTextField streetText = view.getAddressStreetText();
        String str = streetText.getText();
        if (validator.stringFieldValidate(str)) {
            return str;
        }
        addIncorrectFieldMessage(streetText);
        return null;
    }

    private OrganizationType setOrganizationType() {
        JComboBox<String> type = view.getTypeBox();
        String str = type.getItemAt(type.getSelectedIndex());
        if (validator.organizationTypeValidate(str)) {
            return OrganizationType.valueOf(str);
        }
        return null;
    }

    private Integer setEmployeesCount() throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        JTextField emplCountText = view.getEmployeesCountText();
        String str = emplCountText.getText();
        if (validator.employeesCountValidate(str)) {
            return nf.parse(str).intValue();
        }
        addIncorrectFieldMessage(emplCountText);
        return null;
    }

    private Double setAnnualTurnover() throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        JTextField annualTurnoverText = view.getAnnualTurnoverText();
        String str = annualTurnoverText.getText();
        if (validator.annualTurnoverValidate(str)) {
            if (str.equals("")) return null;
            return nf.parse(str).doubleValue();
        }
        addIncorrectFieldMessage(annualTurnoverText);
        return null;
    }

    private float setY() throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        JTextField yText = view.getCoordsYText();
        String str = yText.getText();
        if (validator.coordsYValidate(str)) {
            return nf.parse(str).floatValue();
        }
        addIncorrectFieldMessage(yText);
        return 0;
    }

    private double setX() throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        JTextField xText = view.getCoordsXText();
        String str = xText.getText();
        if (validator.coordsXValidate(str)) {
            return nf.parse(str).doubleValue();
        }
        addIncorrectFieldMessage(xText);
        return 0;
    }


    private String setName() {
        JTextField nameText = view.getNameText();
        String str = nameText.getText();
        if (validator.stringFieldValidate(str)) {
            return str;
        }
        addIncorrectFieldMessage(nameText);
        return null;
    }

    private void addIncorrectFieldMessage(JTextField text) {
        validateResults.add(view.getComponentsPairs().get(text).getText());
    }

    public void setView(OrganizationView view) {
        this.view = view;
    }

    public List<String> getValidateResults() {
        return validateResults;
    }
}
