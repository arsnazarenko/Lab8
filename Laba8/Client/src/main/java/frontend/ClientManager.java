package frontend;
import client.servises.ArgumentValidateManager;
import client.servises.MessageService;
import client.servises.ScriptManager;
import library.clientCommands.Command;
import library.clientCommands.SpecialSignals;
import library.clientCommands.UserData;
import library.clientCommands.commandType.ExecuteScriptCommand;

import java.util.ArrayDeque;
import java.util.Deque;


public class ClientManager {
    private MessageService messageService;
    private ArgumentValidateManager argumentValidator;
    private UserData userData;
    private ScriptManager scriptManager;


    public ClientManager(MessageService messageService, ArgumentValidateManager argumentValidator, ScriptManager scriptManager) {
        this.messageService = messageService;
        this.argumentValidator = argumentValidator;
        this.scriptManager = scriptManager;
    }

    public void executeCommand(Command command) {
        messageService.putInRequestQueue(command);
    }

    public boolean handlerAuth(SpecialSignals logResponse) {
        System.out.println(logResponse);
        if (logResponse != null) {
            if (logResponse == SpecialSignals.AUTHORIZATION_TRUE || logResponse == SpecialSignals.REG_TRUE) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public Deque<Command> createScriptQueue(ExecuteScriptCommand scriptCommand) {
        return new ArrayDeque<>(scriptManager.recursiveCreatingScript(scriptManager.scriptRun(scriptCommand)));
    }


    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public ArgumentValidateManager getArgumentValidator() {
        return argumentValidator;
    }
}