package service.impl

import constant.Constant
import domain.Message
import domain.Structure
import file.FileHandle
import file.impl.FileHandleImpl
import service.Service
import util.StringUtil

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class ServiceImpl implements Service {

    private final StringUtil stringUtil = new StringUtil()
    private final FileHandle fileHandle = new FileHandleImpl()

    /**
     * @param message
     * @return true if valid message, and vice versa
     * @implNote check if the input message has a valid syntax, right prefix number, right syntax of this number
     */
    @Override
    boolean isValidSyntax(Message message) {
        boolean isValidSyntax = false

        List<Structure> syntaxStructures = fileHandle.readStructureFile(Constant.STRUCT_FILE_PATH)

        String prefixNumberFromMessage = message.getPrefixNumber()
        String syntaxFromMessage = message.getContent()
        List<String> syntaxOfPrefixNumber = new ArrayList<>()

        for (int i = 0; i < syntaxStructures.size(); i++) {
            if (prefixNumberFromMessage.equalsIgnoreCase(syntaxStructures.get(i).getPrefixNumber())) {
                isValidSyntax = true
                syntaxOfPrefixNumber = syntaxStructures.get(i).getSyntaxes()
                break
            }
        }

        if (!syntaxOfPrefixNumber.contains(syntaxFromMessage)) {
            isValidSyntax = false
        }

        return isValidSyntax
    }

    /**
     * @param time
     * @return true for valid message, and vice versa
     * @implNote check if the input message has a valid sending time with right format and not time in future
     */
    @Override
    boolean isValidTime(String time) {
        //Check time format is valid
        boolean isValidTimeFormat = true
        DateFormat dateFormat = new SimpleDateFormat(Constant.TIME_FORMAT)
        dateFormat.setLenient(false)
        Date givenDate = new Date()
        try {
            givenDate = dateFormat.parse(time.trim())
        } catch (ParseException pe) {
            isValidTimeFormat = false
        }

        //check given time is future or not
        boolean isTimeFuture = false
        Date current = new Date()
        if (givenDate.after(current)) {
            isTimeFuture = true
        }

        //return result: true = valid time
        if (!isTimeFuture && isValidTimeFormat) {
            return true
        }
        return false
    }

    /**
     * @param inputRawMessages
     * @return the list contain messages which satisfy all criteria of program
     */
    @Override
    List<String> validMessage(List<Message> inputRawMessages) {
        List<String> outputProcessedValidMessage = new ArrayList<>()

        //Remove message has invalid syntax and invalid time format
        for (int i = 0; i < inputRawMessages.size(); i++) {
            if (!isValidTime(inputRawMessages.get(i).getTime()) || !isValidSyntax(inputRawMessages.get(i))) {
                inputRawMessages.remove(i)
            }
        }

        //Get set of phone number in message (not duplicate)
        Set<String> distinctPhoneNumbers = new HashSet<>()
        for (Message message : inputRawMessages) {
            distinctPhoneNumbers.add(message.getPhoneNumber())
        }

        //Get messages of each of phone number
        // Map: Key - phone number & value - list message which sent by this phone number
        Map<String, List<String>> messagesByPhoneNumber = new HashMap<>()
        for (phoneNumber in distinctPhoneNumbers) {

            List<String> messageOfPhone = new ArrayList<>()
            for (message in inputRawMessages) {
                if (phoneNumber.equals(message.getPhoneNumber())) {
                    messageOfPhone.add(message.toString())
                }
                //Sort by time
                messageOfPhone = sortTimeList(messageOfPhone)
                messagesByPhoneNumber.put(phoneNumber, messageOfPhone)
            }
        }


        for (phoneNumber in distinctPhoneNumbers) {
            // Get Map has key:
            // K: prefix number value
            // V: list of message which be sent to this prefix number
            Map<String, List<String>> messagesByPrefixNumbers = classifyMessageByPrefixNumber(messagesByPhoneNumber.get(phoneNumber))
            for (prefixNumber in messagesByPrefixNumbers.keySet()) {
                List<String> messagesSentToPrefixNumber = messagesByPrefixNumbers.get(prefixNumber)

                List<String> validMessages = new ArrayList<>()
                if (messagesSentToPrefixNumber.size() != 1) {
                    int i = 0
                    int j = 1

                    /**
                     * Check two consecutive messages sent 1 month apart
                     */
                    while (true) {
                        if (stringUtil.getDateTimeFromMessage(messagesSentToPrefixNumber.get(j)).getTime() - stringUtil.getDateTimeFromMessage(messagesSentToPrefixNumber.get(i)).getTime() > Constant.ONE_MONTH) {
                            validMessages.add(messagesSentToPrefixNumber.get(i))
                            i = j
                            if (i == messagesSentToPrefixNumber.size() - 1) {
                                validMessages.add(messagesSentToPrefixNumber.get(i))
                                break
                            }
                        }
                        ++j
                    }
                    messagesSentToPrefixNumber.clear()
                    messagesSentToPrefixNumber.addAll(validMessages)
                }
            }
            //Add valid message into outputValidMessage List.
            for (list in messagesByPrefixNumbers.values()) {
                outputProcessedValidMessage.addAll(list)
            }
        }
        return outputProcessedValidMessage
    }

    /**
     * @param messagesSentByPhoneNumber
     * @return a Map which has: key - prefix number
     * and value is messages of each prefix number which send by the phoneNumber
     */
    private Map<String, List<String>> classifyMessageByPrefixNumber(List<String> messagesSentByPhoneNumber) {
        Map<String, List<String>> messagesOfPrefixNumber = new HashMap<>()

        Set<String> distinctPrefixNumbers = new HashSet<>()
        for (message in messagesSentByPhoneNumber) {
            distinctPrefixNumbers.add(stringUtil.getPrefixNumberFromMessage(message))
        }

        for (prefixNumber in distinctPrefixNumbers) {
            List<String> messagesSentToPrefixNumber = new ArrayList<>()
            for (message in messagesSentByPhoneNumber) {
                if (prefixNumber.equals(stringUtil.getPrefixNumberFromMessage(message))) {
                    messagesSentToPrefixNumber.add(message)
                }
            }
            messagesOfPrefixNumber.put(prefixNumber, messagesSentToPrefixNumber)
        }
        return messagesOfPrefixNumber
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
                    String temp = messageList.get(j)
                    messageList.set(j, messageList.get(j + 1))
                    messageList.set(j + 1, temp)
                }
            }
        }
        return messageList
    }
}







