import Util.StringUtil;
import constant.Constant;
import file.FileHandle;
import file.impl.FileHandleImpl;
import service.Service;
import service.impl.ServiceImpl;

public class Main {
    protected static final FileHandle fileHandle = new FileHandleImpl();
    protected static final StringUtil stringUtil = new StringUtil();
    protected static final Service service = new ServiceImpl();

    public static void main(String[] args) {
        fileHandle.writeValidMessage(
                service.validMessage(
                        fileHandle.readMessageFile(Constant.INPUT_MESSAGE_FILE_PATH)));
    }
}
