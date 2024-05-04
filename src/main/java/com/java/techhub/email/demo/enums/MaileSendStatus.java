package com.java.techhub.email.demo.enums;


public enum MaileSendStatus {
    SUCCESS("1"), INFI("2"), ERROR("3");

    String status;

    private MaileSendStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
