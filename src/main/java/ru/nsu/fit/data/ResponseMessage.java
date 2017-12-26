package ru.nsu.fit.data;

/**
 * Created by Diman on 11/12/2017.
 */
public class ResponseMessage {
    private String message;

    public ResponseMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }
}
