package domain;

class Message {
    private String phoneNumber;
    private String content;
    private String time;
    private String prefixNumber;

    Message(String phoneNumber, String content, String time, String prefixNumber) {
        this.phoneNumber = phoneNumber;
        this.content = content;
        this.time = time;
        this.prefixNumber = prefixNumber;
    }

    String getPhoneNumber() {
        return phoneNumber
    }

    String getContent() {
        return content
    }

    String getTime() {
        return time
    }

    String getPrefixNumber() {
        return prefixNumber
    }

    @Override
    String toString() {
        return getPhoneNumber() + "(" + getContent() + "|" + getTime() + "|" + getPrefixNumber() + ")";
    }
}
