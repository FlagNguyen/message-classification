package domain;

public class Message {
    private String phoneNumber;
    private String content;
    private String time;
    private String prefixNumber;

    public Message() {
    }

    public Message(String phoneNumber, String content, String time, String prefixNumber) {
        this.phoneNumber = phoneNumber;
        this.content = content;
        this.time = time;
        this.prefixNumber = prefixNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrefixNumber() {
        return prefixNumber;
    }

    public void setPrefixNumber(String prefixNumber) {
        this.prefixNumber = prefixNumber;
    }

    @Override
    public String toString() {
        return getPhoneNumber() +"("+ getContent() + "|" + getTime() + "|" + getPrefixNumber() + ")";
    }
}
