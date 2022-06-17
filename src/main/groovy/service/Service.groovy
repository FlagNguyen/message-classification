package service;

import domain.Message;

import java.util.List;

interface Service {

    boolean isValidSyntax(Message message);

    boolean isValidTime(String time);

    List<String> validMessage(List<Message> messageList);
}
