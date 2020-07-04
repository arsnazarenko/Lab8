package client.commandProducers;

import library.clientCommands.Command;
import library.clientCommands.UserData;
import library.clientCommands.commandType.MaxByEmployeeCommand;

public class MaxByEmployeesCommandProd implements StandardCommandProducer{
    @Override
    public Command createCommand(UserData userData) {
        return new MaxByEmployeeCommand(userData);
    }
}
