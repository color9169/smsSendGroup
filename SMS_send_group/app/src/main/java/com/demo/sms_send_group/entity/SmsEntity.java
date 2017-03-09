package com.demo.sms_send_group.entity;

import java.io.Serializable;

/**
 * Created by color on 17/2/28.
 */

public class SmsEntity implements Serializable {
    private String name;
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
