package ua.patriot.PatriotOilBot.dispatcher;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.patriot.PatriotOilBot.database.UserRepository;
import ua.patriot.PatriotOilBot.service.BotButton;
import ua.patriot.PatriotOilBot.service.BotFunction;
import ua.patriot.PatriotOilBot.service.Register;

import java.util.HashMap;

@Component
public class Dispatcher {
    @Autowired
    private BotFunction botFunction;
    @Autowired
    private BotButton botButton;
    @Autowired
    private Register register;
    @Autowired
    private UserRepository userRepository;

    final static String SEND_HELP = "Для того щоб почати роботу із ботом введіть команду /start, або виберіть потрібну команду з меню";
    final static String SEND_SCHEDULE_WORK = EmojiParser.parseToUnicode("На жаль, через війну у нашій країні ми були змушені змінити час роботи. Тому тримайте актуальний графік" + ":stopwatch:" +
            "\n\n :radio_button:" + "Понеділок - П'ятниця  " + ":arrow_right:" + "  05:00-00:00\n\n " +
            ":radio_button:" + "Субота  " + ":arrow_right:" + "  06:00-23:00\n\n " +
            ":radio_button:" + "Неділя  " + ":arrow_right:" + "  07:00-22:00");


    // визначає подальшу дії програми в залежності від типу оновлення
    public void messageHandler(Update update){
            if (update.hasMessage() && update.getMessage().hasText()) {
                textMessageHandler(update);
            } else if (update.hasCallbackQuery()) {
                callbackMessageHandler(update);
            }
    }

    // обробляє оновлення з текстом
    public void textMessageHandler(Update update) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if(userRepository.findById(update.getMessage().getChatId()).isPresent()) {
                switch (messageText) {
                    case "/start":
                        botFunction.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/help":
                        botFunction.sendMessage(chatId, SEND_HELP);
                        break;
                    case "/work":
                        botFunction.sendMessage(chatId, SEND_SCHEDULE_WORK);
                        break;
                    case "/bonus":
                        botFunction.sendBonusMessage(chatId);
                        break;
                    case "/price":
                        botButton.sendPrice(chatId);
                        break;
                    case "/location":
                        botFunction.sendMap(chatId);
                        break;
                    case "/feedback":
                        botButton.sendFeedbackMessage(update);
                        break;
                    case "register":
                        botButton.registrationMessage(update);
                    default:
                       botFunction.executeFeedback(update);
                }
            }
            else {
                botButton.registrationMessage(update);
            }
    }

    // обробляє оновлення, які містять дані при натисканні на кнопки
    public void callbackMessageHandler (Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        if (userRepository.findById(update.getCallbackQuery().getMessage().getChatId()).isPresent()) {
            if (callbackData.equals("DIESEL_BUTTON")) {
                String text =EmojiParser.parseToUnicode(":radio_button:" + "Ціна дизельного палива становить 50 гривень за літр");
                botButton.sendBackButton(chatId, "FUEL_MENU", text);
            } else if (callbackData.equals("GASOLINE_BUTTON")) {
                botButton.chooseGasoline(chatId);
            } else if (callbackData.equals("GAS_BUTTON")) {
                String text = EmojiParser.parseToUnicode(":radio_button:" + "Ціна за літр газу становить 35 гривень");
                botButton.sendBackButton(chatId, "FUEL_MENU", text);
            } else if (callbackData.equals("BUTTON_92")) {
                String text = EmojiParser.parseToUnicode(":radio_button:" + "Ціна бензину А-92 становить 48 гривень за літр");
                botButton.sendBackButton(chatId, "GASOLINE_MENU", text);
            } else if (callbackData.equals("BUTTON_95")) {
                String text = EmojiParser.parseToUnicode(":radio_button:" + "Ціна бензину А-95 становить 52 гривні за літр");
                botButton.sendBackButton(chatId, "GASOLINE_MENU", text);
            } else if (callbackData.equals("BUTTON_98")) {
                String text = EmojiParser.parseToUnicode(":radio_button:" + "Ціна бензину А-98 становить 55 гривень за літр");
                botButton.sendBackButton(chatId, "GASOLINE_MENU", text);
            } else if (callbackData.equals("BUTTON_100")) {
                String text = EmojiParser.parseToUnicode(":radio_button:" + "Ціна бензину А-100 становить 65 гривень за літр");
                botButton.sendBackButton(chatId, "GASOLINE_MENU", text);
            } else if (callbackData.equals("FUEL_MENU")) {
                botButton.sendPrice(chatId);
            } else if (callbackData.equals("GASOLINE_MENU")) {
                botButton.chooseGasoline(chatId);
            } else if (callbackData.equals("FEEDBACK_BUTTON")) {
                botFunction.sendMessage(chatId, "Очікую на ваш відгук...");
                botFunction.setUserStatusTrue(update);
                botFunction.executeFeedback(update);
            }
        }
        else if (callbackData.equals("REGISTER_BUTTON")){
            register.registerUser(update);
            botFunction.sendMessage(chatId,EmojiParser.parseToUnicode( "Ви успішно зареєстровані" + ":white_check_mark:"));
            }
        }

}

