import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// методы взяты из https://core.telegram.org/methods
public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try{
            telegramBotsApi.registerBot(new Bot());
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String answer){
        SendMessage sendMessage = new SendMessage();
        //sendMessage.enableMarkdown(true);
        sendMessage.enableHtml(true);

        //в какой конкретный чат отправляем ответ.
        sendMessage.setChatId(message.getChatId().toString());

        //на какое конкретное сообщение отвечаем.
        //sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(answer);
        try{
            setButtons(sendMessage);
            sendMessage(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/info":
                    sendMsg(message, "Введите город, чтобы узнать информацию о погоде.");
                    break;
                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Город не найден!");
                    }
            }

        }
    }

    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        //разметка для клавиатуры.
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        //связываем сообщение с клавиатурой.
        replyKeyboardMarkup.setSelective(true);

        //размер клавиатуры.
        replyKeyboardMarkup.setResizeKeyboard(true);

        //скрывать клавиатуры после нажатия кнопки или нет.
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        //создаем кнопку.
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow(); //первая строка клав-ы.

        keyboardFirstRow.add(new KeyboardButton("/info"));

        //добавляем первую строку в список клавиатуры.
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public String getBotUsername(){
        return "MyTest4600Bot";
    }

    public String getBotToken(){
        return "1172234687:AAF88EvIC-EvPjPzTY-rHNa5a_7n9rjlTaI";
    }
}
