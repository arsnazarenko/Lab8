package library.clientCommands;

import java.io.Serializable;
import java.util.Date;

public class InfoCollection implements Serializable {
    private Class<?> name;
    private int count;
    private Date date;

    public InfoCollection(Class<?> name, int count, Date date) {
        this.name = name;
        this.count = count;
        this.date = date;
    }

    public Class<?> getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public Date getDate() {
        return date;
    }
}
