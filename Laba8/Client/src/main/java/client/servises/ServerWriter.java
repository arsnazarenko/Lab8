package client.servises;

import library.clientCommands.Command;
import library.clientCommands.UserData;
import library.clientCommands.commandType.ExecuteScriptCommand;
import library.clientCommands.commandType.ExitCommand;

import java.nio.channels.Selector;
import java.util.*;


public class ServerWriter implements Runnable {
    private volatile UserData sessionUser;
    private final ICommandCreator commandCreator;
    private final MessageService messageService;
    private final Selector selector;
    private final Queue<Command> queue = new LinkedList<>();
    private ScriptManager scriptManager;


    public ServerWriter(ICommandCreator commandCreator, MessageService messageService, Selector selector, ScriptManager scriptManager ) {
        this.commandCreator = commandCreator;
        this.messageService = messageService;
        this.selector = selector;
        this.scriptManager = scriptManager;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (NoSuchElementException e) {
            System.out.println("ЭКСТРЕННОЕ ЗАВЕРШЕНИЕ");
            System.exit(0);
        }
    }


    public void process() {
        System.out.println(
                "Добро пожаловать!\n\n" +
                        "Используйте команды log или reg для входа\nПри успешной авторизации введите help для справки по командам\n\n");
        while (sessionUser == null) {
            Command command;
            command = commandCreator.authorization(System.in);

            messageService.putInRequestQueue(command);// request to server
            synchronized (messageService) {
                try {
                    messageService.wait();   //sleep until getting response about login
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        while (!Thread.currentThread().isInterrupted()) {
            Command command = null;
            if (!queue.isEmpty()) {
                command = queue.poll();
                if (command != null) {
                    messageService.putInRequestQueue(command);
                }
            } else {
                command = commandCreator.createCommand(System.in, sessionUser);
                if (command.getClass() == ExecuteScriptCommand.class) {
                    queue.addAll(scriptManager.recursiveCreatingScript(scriptManager.scriptRun(command)));
                } else if (command.getClass() == ExitCommand.class) {
                    System.exit(0);
                } else {
                    if (command != null) {
                        messageService.putInRequestQueue(command);
                    }
                }
            }

        }

    }


    public void stop() {
        Thread.currentThread().interrupt();
    }



    public void setSessionUser(UserData sessionUser) {
        this.sessionUser = sessionUser;
    }
}
