package file.impl;

import Util.StringUtil;
import constant.Constant;
import file.FileHandle;

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
    public Map<String, List<String>> readStructFile(String structFilePath) {
        Map<String, List<String>> allSyntaxMap = new HashMap<>();
        File structFile = new File(structFilePath);
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(structFile));
            while ((line = br.readLine()) != null) {

                //Add the number than first element of list
                String keyNumber = stringUtil.getPrefixNumberFromSyntax(line.trim().replace("\uFEFF", ""));

                //The next elements will be syntax string for this number
                String[] syntaxes = stringUtil.getSyntaxListFromSyntaxFile(line.trim().replace("\uFEFF", ""));

                allSyntaxMap.put(keyNumber, Arrays.asList(syntaxes));
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error: read file structure fail");
        }
        return allSyntaxMap;
    }


    /**
     * @param messageFilePath
     * @return return list message
     */
    @Override
    public List<String> readMessageFile(String messageFilePath) {
        List<String> messageList = new ArrayList<>();
        File messageFile = new File(messageFilePath);
        String line = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(messageFile));
            while ((line = bufferedReader.readLine()) != null) {
                messageList.add(line.trim().replace("\uFEFF", ""));
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error: read file message fail");
        }
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

