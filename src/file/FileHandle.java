package file;

import domain.Message;
import domain.Structure;

import java.util.List;

public interface FileHandle {
    List<Structure> readStructFile(String structFilePath);

    List<Message> readMessageFile(String messageFilePath);

    void writeValidMessage(List<String> validMessages);
}
