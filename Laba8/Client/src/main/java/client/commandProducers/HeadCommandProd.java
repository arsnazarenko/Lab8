package client.commandProducers;

import library.clientCommands.Command;
import library.clientCommands.UserData;
import library.clientCommands.commandType.HeadCommand;

public class HeadCommandProd implements StandardCommandProducer {
    @Override
    public Command createCommand(UserData userData) {
        return new HeadCommand(userData);
    }
}
