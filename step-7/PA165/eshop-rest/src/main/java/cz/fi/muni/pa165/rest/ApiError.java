package cz.fi.muni.pa165.rest;

import java.util.List;

public class ApiError {
    List<String> messages;

    public ApiError(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {

        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
