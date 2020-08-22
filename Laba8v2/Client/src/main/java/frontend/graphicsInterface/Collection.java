package frontend.graphicsInterface;


import frontend.graphicsInterface.controllers.Controllers;
import library.сlassModel.Address;
import library.сlassModel.Location;
import library.сlassModel.Organization;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Collection {
    private ArrayList<Organization> collection = new ArrayList<>();

    public ArrayList<Object[]> getTableData() {
        return  collection.stream().map(o -> toLocalizedArray(Controllers.getLocale(), o)).collect(Collectors.toCollection(ArrayList::new));


    }

    public ArrayList<Object[]> getTableData(ArrayList<Organization> arrayList) {
        return  arrayList.stream().map(o -> toLocalizedArray(Controllers.getLocale(), o)).collect(Collectors.toCollection(ArrayList::new));


    }

    public ArrayList<Organization> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<Organization> collection) {
        this.collection = collection;
    }

    public Object[] toLocalizedArray(Locale locale, Organization organization) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        String street = null;
        String zipcode = null;
        Long x = null;
        Double y = null;
        Double z = null;
        String n = null;
        if (organization.getOfficialAddress() != null) {
            street = organization.getOfficialAddress().getStreet();
            zipcode = organization.getOfficialAddress().getZipCode();
            if (organization.getOfficialAddress().getTown() != null) {
                x = organization.getOfficialAddress().getTown().getX();
                y = organization.getOfficialAddress().getTown().getY();
                z = organization.getOfficialAddress().getTown().getZ();
                n = organization.getOfficialAddress().getTown().getName();
            }
        }
        return new Object[]{
                nf.format(organization.getId()),
                organization.getUserLogin(),
                organization.getName(),
                nf.format(organization.getCoordinates().getX()),
                nf.format(organization.getCoordinates().getY()),
                df.format(organization.getCreationDate()),
                nf.format(organization.getEmployeesCount()),
                organization.getType(),
                organization.getAnnualTurnover() == null? null:nf.format(organization.getAnnualTurnover()),
                street,
                zipcode,
                x == null?null:nf.format(x),
                y == null?null:nf.format(y),
                z == null?null:nf.format(z),
                n
        };
    }

    public static String toLocaleString(Organization organization,Locale locale){
        ResourceBundle bundle = ResourceBundle.getBundle("translate",locale);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        StringBuilder builder = new StringBuilder();
        builder.append(bundle.getString("org")).append("{");
        builder.append("id").append("=").append("'").append(nf.format(organization.getId())).append("'").append(",");
        builder.append(bundle.getString("login")).append("=").append("'").append(organization.getUserLogin()).append("'").append(",");
        builder.append(bundle.getString("name")).append("=").append("'").append(organization.getName()).append("'").append(",");
        builder.append(bundle.getString("coord")).append("=").append("{");
        builder.append("x").append("=").append("'").append(nf.format(organization.getCoordinates().getX())).append("'").append(",");
        builder.append("y").append("=").append("'").append(nf.format(organization.getCoordinates().getY())).append("'").append(",").append("}");
        builder.append(bundle.getString("creation_Date")).append("=").append("'").append(df.format(organization.getCreationDate())).append("'").append(",");
        builder.append(bundle.getString("employees")).append("=").append("'").append(organization.getEmployeesCount()).append("'").append(",");
        builder.append(bundle.getString("type")).append("=").append("'").append(organization.getType()).append("'").append(",");
        if (organization.getAnnualTurnover() == null) builder.append(bundle.getString("annual_turnover")).append("=").append("'").append("null").append("'").append(",");
        else builder.append(bundle.getString("annual_turnover")).append("=").append("'").append(nf.format(organization.getAnnualTurnover())).append("'").append(",");
        builder.append(bundle.getString("address")).append("=").append("{");
        Address address = organization.getOfficialAddress();
        if(address==null) builder.append("null").append("}").append("}");
        else {
            builder.append(bundle.getString("street")).append("=").append("'").append(address.getStreet()).append("'").append(",");
            builder.append(bundle.getString("zipcode")).append("=").append("'").append(address.getZipCode()).append("'").append(",");
            builder.append(bundle.getString("loc")).append("=").append("{");
            Location location = address.getTown();
            if(location == null) builder.append("null").append("}").append("}").append("}");
            else {
                builder.append("x").append("=").append("'").append(nf.format(location.getX())).append("'").append(",");
                builder.append("y").append("=").append("'").append(nf.format(location.getY())).append("'").append(",");
                builder.append("z").append("=").append("'").append(nf.format(location.getZ())).append("'").append(",");
                builder.append(bundle.getString("Lname")).append("=").append("'").append(location.getName()).append("'").append("}").append("}").append("}");
            }
        }
        return builder.toString();
    }
}
