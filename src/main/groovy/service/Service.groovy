package service

import domain.Message

interface Service {

    boolean isValidSyntax(Message message)

    boolean isValidTime(String time)

    List<String> validMessage(List<Message> messageList)
}
