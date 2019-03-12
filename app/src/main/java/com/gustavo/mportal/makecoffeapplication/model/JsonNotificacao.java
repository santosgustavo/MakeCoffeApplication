package com.gustavo.mportal.makecoffeapplication.model;

public class JsonNotificacao {

    private String to;
    private Notificacao notification;

    public JsonNotificacao(String to, Notificacao notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notificacao getNotification() {
        return notification;
    }

    public void setNotification(Notificacao notification) {
        this.notification = notification;
    }
}
