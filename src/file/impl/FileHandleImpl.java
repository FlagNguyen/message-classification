package file.impl;

import constant.Constant;
import domain.Message;
import domain.Structure;
import file.FileHandle;
import util.SortMessageByTime;
import util.StringUtil;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHandleImpl implements FileHandle {
    private final Logger logger = Logger.getLogger(FileHandleImpl.class.getName());
    private final StringUtil stringUtil = new StringUtil();

    /**
     * @param structFilePath
     * @return output is map which:
     * key is number and map value is list of syntax string of this number
     */
    @Override
    public List<Structure> readStructFile(String structFilePath) {
        List<Structure> structureList = new ArrayList<>();
        File structFile = new File(structFilePath);
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(structFile));
            while ((line = br.readLine()) != null) {

                //Add the number than first element of list
                String keyNumber = stringUtil.getPrefixNumberFromSyntax(line.trim().replace("\uFEFF", ""));

                //The next elements will be syntax string for this number
                String[] syntaxes = stringUtil.getSyntaxListFromSyntaxFile(line.trim().replace("\uFEFF", ""));

                structureList.add(new Structure(keyNumber, Arrays.asList(syntaxes)));
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error: read file structure fail");
        }
        return structureList;
    }


    /**
     * @param messageFilePath
     * @return return list message
     */
    @Override
    public List<Message> readMessageFile(String messageFilePath) {
        List<Message> messageList = new ArrayList<>();
        File messageFile = new File(messageFilePath);
        String line = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(messageFile));
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().replace("\uFEFF", "");

                String phoneNumber = stringUtil.getPhoneNumberFromMessage(line);
                String content = stringUtil.getSyntaxFromMessage(line);
                String time = stringUtil.getTimeStringFromMessage(line);
                String prefixNumber = stringUtil.getPrefixNumberFromMessage(line);

                messageList.add(new Message(phoneNumber, content, time, prefixNumber));
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error: read file message fail");
        }
        Collections.sort(messageList, new SortMessageByTime());
        logger.log(Level.INFO, "Read file message successfully");
        return messageList;
    }

    /**
     * @param validMessages
     * @implNote write valid messages into specify text file with name is prefix number
     */
    @Override
    public void writeValidMessage(List<String> validMessages) {
        Map<String, List<String>> mapWithPrefixNum = stringUtil.getMapWithPrefixNum(validMessages);
        for (String prefixNumber : mapWithPrefixNum.keySet()) {
            FileWriter outputFileFileWriter = null;
            try {
                outputFileFileWriter = new FileWriter(Constant.OUTPUT_MESSAGE_FILE_PATH + "+" + prefixNumber + ".txt", true);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error: fail when create output file .txt");
            }
            BufferedWriter outputBufferedWriter = new BufferedWriter(outputFileFileWriter);
            for (String mess : mapWithPrefixNum.get(prefixNumber)) {
                try {
                    outputBufferedWriter.write(mess + "\n");
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Error: when write message into file");
                }
            }
            try {
                outputBufferedWriter.flush();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error: when flushing buffered writer");
            }
        }
    }
}

