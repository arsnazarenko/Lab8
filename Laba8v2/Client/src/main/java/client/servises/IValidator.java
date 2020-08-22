package client.servises;

import library.сlassModel.OrganizationType;

public interface IValidator {
    public boolean stringFieldValidate(String str);

    public boolean coordsXValidate(String str);

    public boolean coordsYValidate(String str);

    public boolean annualTurnoverValidate(String str);

    public boolean employeesCountValidate(String str);
    public boolean organizationTypeValidate(String str);

    public boolean zipCodeValidate(String str);

    public boolean locationXValidate(String str);

     boolean locationYValidate(String str);
    boolean locationZValidate(String str);
}
