package service

import domain.Message
import domain.Structure

interface Service {

    boolean isValidSyntax(Message message,  List<Structure> syntaxStructures)

    boolean isValidTime(String time)

    List<String> validMessage(List<Message> messageList)
}
