package ua.patriot.PatriotOilBot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.patriot.PatriotOilBot.database.User;
import ua.patriot.PatriotOilBot.database.UserRepository;
import ua.patriot.PatriotOilBot.sender.BotSender;
import ua.patriot.PatriotOilBot.sender.MessageSender;

@Slf4j
@Component
public class BotFunction {
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private BotSender botSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Lazy
    private BotButton botButton;
    final static String LOG_TEXT = "Error occurred: ";

    private boolean value;

    // надсилає повідомлення, якщо користувач ввів команду старт
    public void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Привіт, " + name + ":wave:" +
                "\nРаді бачити Вас у нашому чат боті, тут ви знайдете усю необхідну інформацію щодо цін на пальне," +
                " ваших бонусів, розташування АЗС та багато іншого" + ":fuelpump:");

        sendMessage(chatId, answer);
        log.info("Replied to user " + name);
    }

    // надсилає повідомлення користувачу, рефакторинг
    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        messageSender.executeMessage(message);
    }


    // надсилає геолокацію АЗС
    public void sendMap(long chatId) {
        String locationText = EmojiParser.parseToUnicode(":round_pushpin:" + "Ми розташовані ось тут");
        double latitude = 41.71399047938002;
        double longitude = -71.52058564537897;
        SendLocation location = new SendLocation(String.valueOf(chatId), latitude, longitude);
        try {
            botSender.execute(location);
        } catch (TelegramApiException e) {
            log.error(LOG_TEXT + e.getMessage());
        }
        sendMessage(chatId, locationText);
    }

    // надсилає повідомлення з бонусною карткою та кількістю бонусів
    public void sendBonusMessage(long chatId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile("https://www.techopedia.com/wp-content/uploads/2023/03/aee977ce-f946-4451-8b9e-bba278ba5f13.png"));
        var users = userRepository.findAll();
        for (User user : users) {
            if (user.getChatId() == chatId) {
                if (user.getUserBonus() > 0) {
                    sendPhoto.setCaption(EmojiParser.parseToUnicode("На вашому рахунку " + user.getUserBonus() + " бонус-(ів)" + ":moneybag:"));
                    messageSender.executePhoto(sendPhoto);
                } else {
                    sendPhoto.setCaption(EmojiParser.parseToUnicode("На жаль, на вашому рахунку немає бонусів" + ":pensive:"));
                    messageSender.executePhoto(sendPhoto);
                }
            }
        }
    }

    // зберігає відгук в бд
    public void executeFeedback(Update update) {
        var users = userRepository.findAll();
        for (User user : users) {
            if (user.getChatId() == update.getMessage().getChatId()) {
                if (user.getFeedbackStatus()) {
                    user.setUserFeedback(update.getMessage().getText());
                    user.setFeedbackStatus(false);
                    userRepository.save(user);
                    sendMessage(update.getMessage().getChatId(), EmojiParser.parseToUnicode("Відгук надіслано" + ":white_check_mark:"));
                }
                else {
                    sendMessage(update.getMessage().getChatId(), EmojiParser.parseToUnicode("Ви ввели невірну команду" + ":pensive:"));
                }
            }
        }
    }

    // якщо користувач дійсно хоче ввести відгук, змінює статус відгуку на +
    public void setUserStatusTrue(Update update) {
        var users = userRepository.findAll();
        for (User user : users) {
            if (user.getChatId() == update.getCallbackQuery().getMessage().getChatId()) {
                user.setFeedbackStatus(true);
                userRepository.save(user);
            }
        }
    }

    // перевіряє чи дійсно користувач хоче ввести відгук, чи це хибна команда
//    public boolean  getFeedbackStatus() {
//        var users = userRepository.findAll();
//        for (User user : users) {
//
//            value = user.getFeedbackStatus();
//        }
//        return value;
//    }
}
