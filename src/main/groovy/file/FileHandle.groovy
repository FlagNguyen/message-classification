package file

import domain.Message
import domain.Structure

interface FileHandle {
    List<Structure> readStructureFile(String structFilePath);

    List<Message> readMessageFile(String messageFilePath);

    void writeValidMessage(List<String> validMessages);
}
