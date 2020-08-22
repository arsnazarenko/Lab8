package server.business.handlers;


import library.clientCommands.Command;
import library.clientCommands.InfoCollection;
import library.clientCommands.SpecialSignals;
import library.clientCommands.UserData;
import server.business.CollectionManager;
import server.business.dao.UserDAO;

import java.util.Date;

public class InfoCommandHandler implements ICommandHandler {

    private CollectionManager collectionManager;
    private UserDAO<UserData, String> usrDao;

    public InfoCommandHandler(CollectionManager collectionManager, UserDAO<UserData, String> usrDao) {
        this.collectionManager = collectionManager;
        this.usrDao = usrDao;
    }

    @Override
    public Object processCommand(Command command) {
        if (authorization(command.getUserData(), usrDao) != 0L) {
            synchronized (collectionManager) {
                Class<?> type = collectionManager.getOrgCollection().getClass();
                int count = collectionManager.getOrgCollection().size();
                Date date = collectionManager.getCreationCollectionDate();
                return new InfoCollection(type,count,date);
            }
        }
        return SpecialSignals.AUTHORIZATION_FALSE;
        //информация о колллекции


    }
}
