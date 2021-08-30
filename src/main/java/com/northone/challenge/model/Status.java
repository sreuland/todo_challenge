package com.northone.challenge.model;

public enum Status {
    UNKOWN(0), PENDING(0), DONE(1), INPROGRESS(2);
    private int code;

    private Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

