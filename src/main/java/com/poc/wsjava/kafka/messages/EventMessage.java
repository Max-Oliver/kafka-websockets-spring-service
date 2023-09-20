package com.poc.wsjava.kafka.messages;

public class EventMessage {
    private String idUser;
    private String username;
    private String eventDetail;
    private String typeEvent;

    public EventMessage() {
    }

    public EventMessage(String username, String idUser, String eventDetail, String typeEvent) {
        this.username = username;
        this.idUser = idUser;
        this.eventDetail = eventDetail;
        this.typeEvent = typeEvent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(String typeEvent) {
        this.typeEvent = typeEvent;
    }


}
