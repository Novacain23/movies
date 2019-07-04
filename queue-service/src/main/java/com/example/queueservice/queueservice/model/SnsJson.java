package com.example.queueservice.queueservice.model;


import java.util.Date;
import java.util.Objects;

public class SnsJson {


    private String Type;
    private String MessageId;
    private String TopicArn;
    private String Message;
    private Date Timestamp;


    public SnsJson(String type, String messageId, String topicArn, String message, Date timestamp) {
        Type = type;
        MessageId = messageId;
        TopicArn = topicArn;
        Message = message;
        Timestamp = timestamp;
    }

    public SnsJson() {
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getTopicArn() {
        return TopicArn;
    }

    public void setTopicArn(String topicArn) {
        TopicArn = topicArn;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Date getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Date timestamp) {
        Timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SnsJson snsJson = (SnsJson) o;
        return Objects.equals(Type, snsJson.Type) &&
                Objects.equals(MessageId, snsJson.MessageId) &&
                Objects.equals(TopicArn, snsJson.TopicArn) &&
                Objects.equals(Message, snsJson.Message) &&
                Objects.equals(Timestamp, snsJson.Timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Type, MessageId, TopicArn, Message, Timestamp);
    }
}
