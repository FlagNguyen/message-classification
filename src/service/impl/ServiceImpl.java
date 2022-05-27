package service.impl;

import Util.StringUtil;
import constant.Constant;
import file.FileHandle;
import file.impl.FileHandleImpl;
import service.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServiceImpl implements Service {

    private final StringUtil stringUtil = new StringUtil();
    private final FileHandle fileHandle = new FileHandleImpl();

    /**
     * @param message
     * @return true if valid message, and vice versa
     * @implNote check if the input message has a valid syntax, right prefix number, right syntax of this number
     */
    @Override
    public boolean isValidSyntax(String message) {
        Map<String, List<String>> allSyntax = fileHandle.readStructFile(Constant.STRUCT_FILE_PATH);
        String prefixNumber = stringUtil.getPrefixNumberFromMessage(message);
        String syntaxOfMessage = stringUtil.getSyntaxFromMessage(message);

        if (!allSyntax.containsKey(prefixNumber)) {
            return false;
        }

        List<String> syntaxOfPrefixNumber = allSyntax.get(prefixNumber);
        if (!syntaxOfPrefixNumber.contains(syntaxOfMessage)) {
            return false;
        }

        return true;
    }

    /**
     * @param message
     * @return true for valid message, and vice versa
     * @implNote check if the input message has a valid sending time with right format and not time in future
     */
    @Override
    public boolean isValidTime(String message) {
        String time = stringUtil.getTimeStringFromMessage(message);
        //Check time format is valid
        boolean isValidTimeFormat = true;
        DateFormat dateFormat = new SimpleDateFormat(Constant.TIME_FORMAT);
        dateFormat.setLenient(false);
        Date givenDate = new Date();
        try {
            givenDate = dateFormat.parse(time.trim());
        } catch (ParseException pe) {
            isValidTimeFormat = false;
        }

        //check given time is future or not
        boolean isTimeFuture = false;
        Date current = new Date();
        if (givenDate.after(current)) {
            isTimeFuture = true;
        }

        //return result: true = valid time
        if (!isTimeFuture && isValidTimeFormat) {
            return true;
        }
        return false;
    }

    /**
     * @param messageList
     * @return the list contain messages which satisfy all criteria of program
     */
    @Override
    public List<String> validMessage(List<String> messageList) {
        List<String> outputValidMessage = new ArrayList<>();

        //Remove message has invalid syntax and invalid time format
        for (int i = 0; i < messageList.size(); i++) {
            if (!isValidTime(messageList.get(i)) || !isValidSyntax(messageList.get(i))) {
                messageList.remove(i);
            }
        }

        //Get set of phone number in message (not duplicate)
        Set<String> phoneNumberSet = new HashSet<>();
        for (String message : messageList) {
            phoneNumberSet.add(stringUtil.getPhoneNumberFromMessage(message));
        }

        //Get messages of each of phone number
        Map<String, List<String>> messageMap = new HashMap<>();
        for (String phoneNumber : phoneNumberSet) {
            List<String> messageOfPhone = new ArrayList<>();
            for (String message : messageList) {
                if (phoneNumber.equals(stringUtil.getPhoneNumberFromMessage(message))) {
                    messageOfPhone.add(message);
                }
                messageOfPhone = sortTimeList(messageOfPhone);
                messageMap.put(phoneNumber, messageOfPhone);
            }
        }

        for (String phoneNumber : phoneNumberSet) {
            //Get list message which send by this phone number
            List<String> messageOfPhone = messageMap.get(phoneNumber);

            // Get Map has key: prefix number; value: list of message to this prefix number
            Map<String, List<String>> messagesOfPrefixNumber = messageOfPrefixNumber(messageOfPhone);
            for (String prefixNumber : messagesOfPrefixNumber.keySet()) {
                List<String> messages = messagesOfPrefixNumber.get(prefixNumber);


                List<String> validMess = new ArrayList<>();
                if (messages.size() != 1) {
                    int i = 0;
                    int j = 1;
                    while (true) {
                        if (stringUtil.getDateTimeFromMessage(messages.get(j)).getTime()
                                - stringUtil.getDateTimeFromMessage(messages.get(i)).getTime()
                                > Constant.ONE_MONTH) {
                            validMess.add(messages.get(i));
                            i = j;
                            if (i == messages.size() - 1) {
                                validMess.add(messages.get(i));
                                break;
                            }
                        }
                        ++j;
                    }
                    messages.clear();
                    messages.addAll(validMess);
                }
            }
            //Add valid message into outputValidMessage List.
            for (List<String> list : messagesOfPrefixNumber.values()) {
                outputValidMessage.addAll(list);
            }
        }
        return outputValidMessage;
    }

    /**
     * @param messageOfPhone
     * @return a Map which has: key - prefix number
     * and value is messages of each prefix number which send by the phoneNumber
     */
    private Map<String, List<String>> messageOfPrefixNumber(List<String> messageOfPhone) {
        Map<String, List<String>> messagesOfPrefixNumber = new HashMap<>();
        Set<String> prefixNumberSet = new HashSet<>();
        for (String message : messageOfPhone) {
            prefixNumberSet.add(stringUtil.getPrefixNumberFromMessage(message));
        }

        for (String prefixNumber : prefixNumberSet) {
            List<String> specificMessage = new ArrayList<>();
            for (String message : messageOfPhone) {
                if (prefixNumber.equals(stringUtil.getPrefixNumberFromMessage(message))) {
                    specificMessage.add(message);
                }
            }
            messagesOfPrefixNumber.put(prefixNumber, specificMessage);
        }
        return messagesOfPrefixNumber;
    }

    /**
     * @param messageList
     * @return sort input list by sending time and return the sorted list
     */
    private List<String> sortTimeList(List<String> messageList) {
        for (int i = 0; i < messageList.size() - 1; i++) {
            for (int j = 0; j < messageList.size() - i - 1; j++) {
                if ((stringUtil.getDateTimeFromMessage(messageList.get(j)))
                        .after((stringUtil.getDateTimeFromMessage(messageList.get(j + 1))))) {
                    String temp = messageList.get(j);
                    messageList.set(j, messageList.get(j + 1));
                    messageList.set(j + 1, temp);
                }
            }
        }
        return messageList;
    }
}







