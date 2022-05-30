package service;

import domain.Message;

import java.util.List;

public interface Service {

    boolean isValidSyntax(Message message);

    boolean isValidTime(String time);

    List<String> validMessage(List<Message> messageList);
}
