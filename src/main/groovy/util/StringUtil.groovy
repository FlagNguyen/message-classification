package util

import constant.Constant

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.logging.Level
import java.util.logging.Logger

class StringUtil {

    private final Logger logger = Logger.getLogger(StringUtil.class.getName())

    String getPhoneNumberFromMessage(String message) {
        return message.trim().split("\\(")[0].trim()
    }

    String getPrefixNumberFromMessage(String message) {
        return message.split("\\(")[1].replace(")", "").split("\\|")[2]
    }

    String getTimeStringFromMessage(String message) {
        return message.split("\\(")[1].replace(")", "").split("\\|")[1]
    }

    Date getDateTimeFromMessage(String message) {
        return convertStringToDate(message.split("\\(")[1].replace(")", "").split("\\|")[1])
    }

    String getSyntaxFromMessage(String message) {
        return message.split("\\(")[1].replace(")", "").split("\\|")[0].toUpperCase()
    }


    String getPrefixNumberFromSyntax(String syntax) {
        return syntax.split(":")[0].replace("+", "")
    }

    String[] getSyntaxListFromSyntaxFile(String syntax) {
        String[] syntaxes = syntax.split("\\(")[1].replace(").", "")
                .trim().replaceAll(" ", "").split(",")

        //Format syntax message
        for (int i = 0; i < syntaxes.length; i++) {
            syntaxes[i] = syntaxes[i].trim().toUpperCase()
        }
        return syntaxes
    }

    Map<String, List<String>> getMessagesByPrefixNumber(List<String> messages) {
        Map<String, List<String>> prefixNumbersAndCorrespondingMessages = new HashMap<>()
        Set<String> distinctPrefixNumbers = new HashSet<>()

        //Add prefix number to set
        for (message in messages) {
            distinctPrefixNumbers.add(getPrefixNumberFromMessage(message))
        }

        for (prefixNumber in distinctPrefixNumbers) {
            List<String> messagesOfPrefix = new ArrayList<>()
            for (message in messages) {
                if (getPrefixNumberFromMessage(message).equals(prefixNumber)) {
                    messagesOfPrefix.add(message)
                }
            }
            prefixNumbersAndCorrespondingMessages.put(prefixNumber, messagesOfPrefix)
        }
        return prefixNumbersAndCorrespondingMessages
    }

    /**
     * 
     * @param time
     * @return Date type of input time 
     */
    Date convertStringToDate(String time) {
        DateFormat dateFormat = new SimpleDateFormat(Constant.TIME_FORMAT);
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(time.trim());
        } catch (ParseException pe) {
            logger.log(Level.WARNING, "Error: convert string to date failure");
        }
        return null;
    }
}
