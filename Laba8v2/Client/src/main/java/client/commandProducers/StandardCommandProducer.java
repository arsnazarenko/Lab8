package client.commandProducers;

import library.clientCommands.Command;
import library.clientCommands.UserData;

public interface StandardCommandProducer {
    Command createCommand(UserData userData);
}
