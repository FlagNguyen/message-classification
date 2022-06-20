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
        File structureFile = new File(structFilePath)

        List structureLines = FileUtils.readLines(structureFile, "UTF-8")

        for (line in structureLines) {
            String prefixNumber = stringUtil.getPrefixNumberFromSyntax(line.trim().replace("\uFEFF", ""))
            String[] syntaxes = stringUtil.getSyntaxListFromSyntaxFile(line.trim().replace("\uFEFF", ""))

            structures.add(new Structure(prefixNumber, Arrays.asList(syntaxes)))
        }
        logger.info("Read structure files "+structureFile+" successfully")
        return structures
    }


    /**
     * @param messageFilePath
     * @return return list message
     */
    @Override
    List<Message> readMessageFile(String messageFilePath) {
        List receivedMessages = new ArrayList<>()
        File messageFile = new File(messageFilePath)

        List messageLines = FileUtils.readLines(messageFile, "UTF-8")

        for (line in messageLines) {
            String phoneNumber = stringUtil.getPhoneNumberFromMessage(line)
            String content = stringUtil.getSyntaxFromMessage(line)
            String time = stringUtil.getTimeStringFromMessage(line)
            String prefixNumber = stringUtil.getPrefixNumberFromMessage(line)

            receivedMessages.add(new Message(phoneNumber, content, time, prefixNumber))
        }

        Collections.sort(receivedMessages, new SortMessageByTime())
        logger.info("Read file message successfully")
        return receivedMessages
    }

    /**
     * @param validMessages
     * @implNote write valid messages into specify text file with name is prefix number
     */
    @Override
    void writeValidMessage(List<String> validMessages) {
        Map<String, List<String>> messageByPrefixNum = stringUtil.getMessagesByPrefixNumber(validMessages)
        for (prefixNumber in messageByPrefixNum.keySet()) {
            for (message in messageByPrefixNum.get(prefixNumber)) {
                FileUtils.write(new File(Constant.OUTPUT_MESSAGE_FILE_PATH + "+" + prefixNumber + ".txt"), message + "\n",
                        "UTF-8", true)
            }
        }
        logger.info("Write into file successfully")
    }
}

