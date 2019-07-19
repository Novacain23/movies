package com.example.notificationservice.notificationservice.model;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.print.attribute.standard.Destination;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

@Entity
public class Notification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "contact_info")
    private String contactInfo;
    @Column(name = "messageContent")
    private String messageContent;
    @Column(name = "sentTime")
    private Calendar sentTime;
    @Column(name = "isCorp")
    private boolean isCorp;



    public Notification(int id, String contactInfo, String messageContent, Calendar sentTime, boolean isCorp) {
        this.id = id;
        this.contactInfo = contactInfo;
        this.messageContent = messageContent;
        this.sentTime = sentTime;
        this.isCorp = isCorp;
    }
    public Notification(String contactInfo, String messageContent, Calendar sentTime, boolean isCorp){
        this.contactInfo = contactInfo;
        this.messageContent = messageContent;
        this.sentTime = sentTime;
        this.isCorp = isCorp;
    }

    public Notification() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Calendar getSentTime() {
        return sentTime;
    }

    public void setSentTime(Calendar sentTime) {
        this.sentTime = sentTime;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public boolean isCorp() {
        return isCorp;
    }

    public void setCorp(boolean corp) {
        isCorp = corp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id &&
                isCorp == that.isCorp &&
                Objects.equals(contactInfo, that.contactInfo) &&
                Objects.equals(messageContent, that.messageContent) &&
                Objects.equals(sentTime, that.sentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contactInfo, messageContent, sentTime, isCorp);
    }
}
