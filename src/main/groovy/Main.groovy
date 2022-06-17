import domain.Message;
import util.StringUtil;
import constant.Constant;
import file.FileHandle;
import file.impl.FileHandleImpl;
import service.Service;
import service.impl.ServiceImpl;


static void main(String[] args) {
    final FileHandle fileHandle = new FileHandleImpl();
    final StringUtil stringUtil = new StringUtil();
    final Service service = new ServiceImpl();
        fileHandle.writeValidMessage(
                service.validMessage(
                        fileHandle.readMessageFile(Constant.INPUT_MESSAGE_FILE_PATH)));

//        for (Message message: fileHandle.readMessageFile(Constant.INPUT_MESSAGE_FILE_PATH)){
//            System.out.println(message.toString());
//        }
    }

