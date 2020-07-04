package client.servises;

import library.clientCommands.Command;
import library.clientCommands.commandType.ExecuteScriptCommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ScriptManager {
    private ICommandCreator commandCreator;

    public ScriptManager(ICommandCreator commandCreator) {
        this.commandCreator = commandCreator;
    }

    public Queue<Command> scriptRun(Command command) {
        Queue<Command> commandQueue = new LinkedList<>();
        ExecuteScriptCommand script = (ExecuteScriptCommand) command;
        try (InputStream scriptStream = new FileInputStream(new File(script.getScript()))) {
            //генерим список команд с логином и паролем, которые указаны при отправке команды execute_script
            commandQueue = commandCreator.createCommandQueue(scriptStream, script.getUserData());
            return commandQueue;
        } catch (NoSuchElementException e) {
            System.out.println("ОШИБКА СКРИПТА");
        } catch (IOException e) {
            System.out.println("ОШИБКА ФАЙЛА");
        }
        return commandQueue; // при возникшей ошибке возвращаем пустую очередь
    }

    public List<Command> recursiveCreatingScript(Queue<Command> commands) {
        List<Command> newCommands = new ArrayList<>();
        Queue<Command> commandQueue = commands.stream().filter(Objects::nonNull).collect(Collectors.toCollection(ArrayDeque::new));
        while (true) {
            Command command = commandQueue.poll();
            if (command == null) {
                break;
            } else {
                newCommands.add(command);
                if (command.getClass() == ExecuteScriptCommand.class) {
                    Queue<Command> newCommandQueue = scriptRun(command);
                    newCommandQueue.addAll(commandQueue);
                    commandQueue = newCommandQueue;
                }
            }
        }

        return newCommands;
    }

}
