package server.business.handlers;

import library.clientCommands.Command;
import library.clientCommands.SpecialSignals;
import library.clientCommands.UserData;
import library.сlassModel.Organization;
import server.business.CollectionManager;
import server.business.dao.UserDAO;

import java.util.*;
import java.util.stream.Collectors;

public class PrintAscendingCommandHandler implements ICommandHandler {

    private CollectionManager collectionManager;
    private UserDAO<UserData, String> usrDao;

    public PrintAscendingCommandHandler(CollectionManager collectionManager, UserDAO<UserData, String> usrDao) {
        this.collectionManager = collectionManager;
        this.usrDao = usrDao;
    }

    @Override
    public Object processCommand(Command command) {
        if (authorization(command.getUserData(), usrDao) != 0L) {
            synchronized (collectionManager) {
                return collectionManager.getOrgCollection().
                        stream().
                        sorted(Comparator.comparing(Organization::getCreationDate)).collect(Collectors.toCollection(ArrayDeque::new));
            }
        }
        return SpecialSignals.AUTHORIZATION_FALSE;

    }
}
