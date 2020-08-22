package client.servises;



import library.clientCommands.InfoCollection;
import library.clientCommands.SpecialSignals;
import library.сlassModel.Organization;

import java.util.Deque;
/**
 * Класс-обработчик ответов от сервера
 * @see AnswerHandler
 */
public class AnswerHandler implements IAnswerHandler {
    /**В зависимости от класса ответа делает ту или иную операцию
     * @param answerObject - объект ответа от сервера
     */
    @Override
    public void answerHandle(Object answerObject) {
        if(answerObject != null) {
            if(answerObject instanceof Deque) {
                Deque<?> organizationDeque = (Deque<?>) answerObject;
                if (organizationDeque.isEmpty()) {
                    System.out.println("Объектов не найдено");
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    organizationDeque.forEach(o -> stringBuilder.append(o.toString()).append("\n"));
                    System.out.println(stringBuilder.toString());
                }

            } else if (answerObject instanceof String || answerObject instanceof Organization) {
                System.out.println(answerObject);
            } else if(answerObject instanceof SpecialSignals) {
                SpecialSignals specialSignals = (SpecialSignals) answerObject;
                System.out.println(specialSignals.toString());
            } else if(answerObject instanceof InfoCollection) {
                InfoCollection info = (InfoCollection) answerObject;
                System.out.println("Класс коллекции: " + info.getName().getSimpleName() + "\n" +
                        "Количество объектов: " + info.getCount() + "\n" +
                        "Дата создания коллекции: " + info.getDate());
            }
        } else {
            System.out.println("Объектов не найдено");
        }

    }
}
