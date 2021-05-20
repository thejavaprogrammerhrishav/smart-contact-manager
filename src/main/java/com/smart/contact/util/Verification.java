package com.smart.contact.util;

public class Verification {
    private String code;
    private String uuid;

    public Verification(String code, String uuid) {
        this.code = code;
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Verification{" +
                "code='" + code + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
