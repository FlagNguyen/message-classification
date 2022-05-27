package file;

import java.util.List;
import java.util.Map;

public interface FileHandle {
    Map<String, List<String>> readStructFile(String structFilePath);

    List<String> readMessageFile(String messageFilePath);

    void writeValidMessage(List<String> validMessages);
}
