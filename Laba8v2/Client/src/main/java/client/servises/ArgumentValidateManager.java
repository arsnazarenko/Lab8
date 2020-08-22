package client.servises;

public class ArgumentValidateManager {

    public Long idValid(String param) {
        Long result = null;
        try {
            result = Long.parseLong(param);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка вода id");
        }
        return result;
    }

    public String subStringIsValid(String param) {
        try {
            if (!param.trim().equals("")) return param;
        } catch (NullPointerException e) {
            System.out.println("НЕВЕРНАЯ ПОДСТРОКА");
        }
        return null;
    }
}
