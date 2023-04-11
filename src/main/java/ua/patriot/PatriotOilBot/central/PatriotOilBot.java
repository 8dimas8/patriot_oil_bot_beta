package ua.patriot.PatriotOilBot.central;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.patriot.PatriotOilBot.config.BotConfig;
import ua.patriot.PatriotOilBot.service.BotButton;
import ua.patriot.PatriotOilBot.service.BotMethod;
import ua.patriot.PatriotOilBot.service.BotService;
import ua.patriot.PatriotOilBot.service.Register;

@Slf4j
@Component
public class PatriotOilBot extends TelegramLongPollingBot {
    final BotConfig config;
    private final BotMethod botMethod;

    private final BotService botService;
    private final BotButton botButton;
    private final Register register;

    final static String SEND_HELP = "Для того щоб почати роботу із ботом введіть команду /start, або виберіть потрібну команду з меню," +
            " також натиснувши на кнопку меню у нижній частині екрану, ви можете отримати прелік усіх команд" +
            "" +
            "";
    final static String SEND_SCHEDULE_WORK = EmojiParser.parseToUnicode("На жаль, через війну у нашій країні ми були змушені змінити час роботи. Тому тримайте актуальний графік" + ":stopwatch:" +
            "\n\n :radio_button:" + "Понеділок - П'ятниця  " + ":arrow_right:" + "  05:00-00:00\n\n " +
            ":radio_button:" + "Субота  " + ":arrow_right:" + "  06:00-23:00\n\n " +
            ":radio_button:" + "Неділя  " + ":arrow_right:" + "  07:00-22:00");
    final static String SEND_BONUS =EmojiParser.parseToUnicode("За вашим рахунком поки що немає бонусів" + ":pensive:");
    final static String REGISTER_YES_BUTTON = "REGISTER_YES";
    final static String REGISTER_NO_BUTTON = "REGISTER_NO";

    public PatriotOilBot(BotConfig config, BotService botService, BotMethod botMethod, BotButton botButton, Register register){
        this.config = config;
        this.botMethod = botMethod;
        this.botService = botService;
        this.botButton = botButton;
        this.register = register;
        botService.menu();
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                    case "/start":
                        botMethod.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/help":
                        botMethod.sendMessage(chatId, SEND_HELP);
                    break;
                case "/work":
                    botMethod.sendMessage(chatId,SEND_SCHEDULE_WORK);
                    break;
                case "/bonus":
                    botMethod.sendMessage(chatId,SEND_BONUS);
                    break;
                case "/price":
                    botButton.sendPrice(chatId);
                    break;
                default:
                    botMethod.sendMessage(chatId, EmojiParser.parseToUnicode("Ви ввели невірну команду" + ":pensive:"));
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if(callbackData.equals("DIESEL_BUTTON")){
                String text = "Ціна дизельного палива становить 50 гривень за літр";
                botButton.sendBackButton(chatId, "FUEL_MENU", text);
            }
            else if(callbackData.equals("GASOLINE_BUTTON")){
                botButton.chooseGasoline(chatId);
            } else if (callbackData.equals("GAS_BUTTON")) {
                String text = "Ціна за літр газу становить 35 гривень";
                botButton.sendBackButton(chatId,"FUEL_MENU", text);
            } else if (callbackData.equals("BUTTON_92")) {
                String text = "Ціна бензину А-92 становить 48 гривень за літр";
                botButton.sendBackButton(chatId, "GASOLINE_MENU", text);
            }else if (callbackData.equals("BUTTON_95")) {
                String text = "Ціна бензину А-95 становить 52 гривні за літр";
                botButton.sendBackButton(chatId, "GASOLINE_MENU", text);
            }else if (callbackData.equals("BUTTON_98")) {
                String text = "Ціна бензину А-98 становить 55 гривень за літр";
                botButton.sendBackButton(chatId, "GASOLINE_MENU", text);
            }else if (callbackData.equals("BUTTON_100")) {
                String text = "Ціна бензину А-100 становить 65 гривень за літр";
                botButton.sendBackButton(chatId, "GASOLINE_MENU", text);
            } else if (callbackData.equals("FUEL_MENU")) {
                botButton.sendPrice(chatId);
            } else if (callbackData.equals("GASOLINE_MENU")) {
                botButton.chooseGasoline(chatId);
            } else if (callbackData.equals(REGISTER_YES_BUTTON)) {
                String text = "Ви успішно зареєстровані!";
                botService.executeEditMessageText(text, chatId, messageId);
            } else if (callbackData.equals(REGISTER_NO_BUTTON)) {
                String text = "Ви незареєстровані!";
                botService.executeEditMessageText(text, chatId, messageId);
            }
        }
    }
}
