package file.impl


import constant.Constant
import domain.Message
import domain.Structure
import file.FileHandle
import org.apache.commons.io.FileUtils
import util.SortMessageByTime
import util.StringUtil

import java.util.logging.Logger

class FileHandleImpl implements FileHandle {
    private final StringUtil stringUtil = new StringUtil()
    private static Logger logger = Logger.getLogger(file.impl.FileHandleImpl.class.getName())

     /** @param structFilePath
     * @return output is map which:
     * key is number and map value is list of syntax string of this number
     */
    @Override
    List<Structure> readStructureFile(String structFilePath) {
        List<Structure> structures = new ArrayList<>()
        File structFile = new File(structFilePath)

        List structureLines = FileUtils.readLines(structFile, "UTF-8")

        for (String line : structureLines) {
            String prefixNumber = stringUtil.getPrefixNumberFromSyntax(line.trim().replace("\uFEFF", ""))
            String[] syntaxes = stringUtil.getSyntaxListFromSyntaxFile(line.trim().replace("\uFEFF", ""))

            structures.add(new Structure(prefixNumber, Arrays.asList(syntaxes)))
        }
        logger.info("Read structure file successfully")
        return structures
    }


    /**
     * @param messageFilePath
     * @return return list message
     */
    @Override
    List<Message> readMessageFile(String messageFilePath) {
        List<Message> receivedMessages = new ArrayList<>()
        File messageFile = new File(messageFilePath)

        List messageLines = FileUtils.readLines(messageFile, "UTF-8")

        for (String line : messageLines) {
            String phoneNumber = stringUtil.getPhoneNumberFromMessage(line)
            String content = stringUtil.getSyntaxFromMessage(line)
            String time = stringUtil.getTimeStringFromMessage(line)
            String prefixNumber = stringUtil.getPrefixNumberFromMessage(line)

            receivedMessages.add(new Message(phoneNumber, content, time, prefixNumber))
        }

        Collections.sort(receivedMessages, new SortMessageByTime())
//        logger.log(Level.INFO, "Read file message successfully")
        logger.info("Read file message successfully")
        return receivedMessages
    }

    /**
     * @param validMessages
     * @implNote write valid messages into specify text file with name is prefix number
     */
    @Override
    void writeValidMessage(List<String> validMessages) {
        Map<String, List<String>> mapWithPrefixNum = stringUtil.getMapWithPrefixNumber(validMessages)
        for (String prefixNumber : mapWithPrefixNum.keySet()) {
            for (String mess : mapWithPrefixNum.get(prefixNumber)) {
                FileUtils.write(new File(Constant.OUTPUT_MESSAGE_FILE_PATH + "+" + prefixNumber + ".txt"), mess + "\n",
                        "UTF-8", true)
            }
        }
        logger.info("Write into file successfully")
    }
}

