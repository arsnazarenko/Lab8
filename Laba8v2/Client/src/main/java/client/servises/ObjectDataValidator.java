package client.servises;

import frontend.graphicsInterface.controllers.Controllers;
import library.сlassModel.OrganizationType;

import java.text.NumberFormat;
import java.text.ParseException;

public class ObjectDataValidator implements IValidator{


    public boolean stringFieldValidate(String str) {
        if (str != null) {
            if (!str.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    public boolean coordsXValidate(String str) {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        boolean flag = true;
        try{
            double res = nf.parse(str).doubleValue();
            if(res <= - 98 || res >= 10000) {
                flag = false;
            }
            return flag;
        } catch (NumberFormatException | ParseException e) {
            flag = false;
            return flag;
        }
    }

    public boolean coordsYValidate(String str) {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        boolean flag = true;
        try{
            float res = nf.parse(str).floatValue();
            if(res <= - 148 || res >= 10000) {
                flag = false;
            }
            return flag;
        } catch (NumberFormatException | ParseException e) {
            flag = false;
            return flag;
        }
    }

    public boolean annualTurnoverValidate(String str) {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        boolean flag = true;
        try{
            if(str.equals("")) {
                return flag;
            }
            Double res = nf.parse(str).doubleValue();
            if(res <= 0) {
                flag = false;
            }
            return flag;
        } catch (NumberFormatException | ParseException e) {
            flag = false;
            return flag;
        }

    }

    public boolean employeesCountValidate(String str) {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        boolean flag = true;
        try{
            int res = nf.parse(str).intValue();
            if(res <= 0) {
                flag = false;
            }
            return flag;
        } catch (NumberFormatException | ParseException e) {
            flag = false;
            return flag;
        }
    }
    public boolean organizationTypeValidate(String str) {
        boolean flag = true;
        try{
            OrganizationType res = OrganizationType.valueOf(str);
            return flag;
        } catch (IllegalArgumentException e) {
            flag = false;
            return flag;
        }
    }

    public boolean zipCodeValidate(String str) {
        if(stringFieldValidate(str)) {
            if(str.length() >= 7) {
                return true;
            }
        }
        return false;
    }

    public boolean locationXValidate(String str) {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        boolean flag = true;
        try{
            Long res = nf.parse(str).longValue();
            return flag;
        } catch (NumberFormatException | ParseException e) {
            flag = false;
            return flag;
        }
    }
    public boolean locationYValidate(String str) {
        NumberFormat nf = NumberFormat.getNumberInstance(Controllers.getLocale());
        boolean flag = true;
        try{
            double res = nf.parse(str).doubleValue();
            return flag;
        } catch (NumberFormatException | ParseException e) {
            flag = false;
            return flag;
        }
    }

    public boolean locationZValidate(String str) {
        return locationYValidate(str);
    }








}
