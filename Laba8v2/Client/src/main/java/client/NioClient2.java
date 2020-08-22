package client;

import client.servises.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.NoSuchElementException;

public class NioClient2 {
    public static void main(String[] args) {
        try(DatagramChannel datagramChannel = DatagramChannel.open()) {
            //ConsoleClient consoleClient = null;
            int port = Integer.parseInt(args[1]);
            SocketAddress socketAddress = new InetSocketAddress(args[0], port);
            String ver = args[2];
            IValidator objectDataValidator = new ObjectDataValidator();
            IValidator objectDataValidatorCon = new ObjectDataValidatorConsole();
            IObjectCreator objectCreator = new ObjectCreator(objectDataValidator);
            IObjectCreator objectCreatorConsole = new ObjectCreator(objectDataValidatorCon);
            ArgumentValidateManager argumentValidateManager = new ArgumentValidateManager();
            ICommandProducerManager validator = new CommandProduceManager(objectCreatorConsole, argumentValidateManager);
            IReader reader = new Reader(validator);
            IAnswerHandler answerHandler = new AnswerHandler();
            ICommandCreator commandCreator = new CommandCreator(reader);
            datagramChannel.connect(socketAddress);
            datagramChannel.configureBlocking(false);
            ConsoleClient consoleClient = new ConsoleClient(commandCreator, ByteBuffer.allocate(256 * 1024),
                    socketAddress, answerHandler);
            GuiClient client = new GuiClient(ByteBuffer.allocate(256 * 1024), socketAddress);
            if (ver.equals("console")) {
                consoleClient.process(datagramChannel);
            } else if (ver.equals("gui")){
                client.process(datagramChannel);
            } else {
                System.out.println("НЕКОРРЕКТНАЯ ВЕРСИЯ\nSAMPLE: java -jar Client.jar [hostname] [port] [version]");
            }

        } catch (IOException e) {
            System.out.println("ОШИБКА ПОДКЛЮЧЕНИЯ К СЕРВЕРУ");
            System.exit(0);
        } catch (NoSuchElementException e) {
            System.out.println("ЭКСТРЕННОЕ ЗАВЕРШЕНИЕ");
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("УКАЖИТЕ ХОСТ И ПОРТ СЕРВЕРА\nSAMPLE: java -jar Client.jar [hostname] [port] [version]");
        } catch (NumberFormatException e) {
            System.out.println("НЕКОРРЕКТНЫЙ ПОРТ\nSAMPLE: java -jar Client.jar [hostname] [port] [version]");
        } catch (UnresolvedAddressException e) {
            System.out.println("НЕКОРРЕКТНЫЙ ХОСТ\nSAMPLE: java -jar Client.jar [hostname] [port] [version]");
        } catch (IllegalArgumentException e) {
            System.out.println("НЕКОРРЕКТНЫЙ ВВОД\nSAMPLE: java -jar Client.jar [hostname] [port] [version]");
        }
    }
}