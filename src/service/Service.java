package service;

import java.util.List;

public interface Service {

    boolean isValidSyntax(String message);

    boolean isValidTime(String time);

    List<String> validMessage(List<String> messageList);
}
