package com.smart.contact.util;

public class Message {
    private String className;
    private String text;

    public Message() {
    }

    public Message(String className, String text) {
        this.className = className;
        this.text = text;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "className='" + className + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
