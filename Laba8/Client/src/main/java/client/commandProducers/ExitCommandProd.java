package client.commandProducers;

import library.clientCommands.Command;
import library.clientCommands.UserData;
import library.clientCommands.commandType.ExitCommand;

public class ExitCommandProd implements StandardCommandProducer {
    @Override
    public Command createCommand(UserData userData) {
        return new ExitCommand(userData);
    }
}
