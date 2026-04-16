package com.esports.model;

public class Event {
    private int    id;
    private String name;
    private String gameTitle;
    private String eventDate;
    private String eventTime;

    public Event() {}

    public Event(int id, String name, String gameTitle, String eventDate, String eventTime) {
        this.id        = id;
        this.name      = name;
        this.gameTitle = gameTitle;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public int    getId()        { return id; }
    public String getName()      { return name; }
    public String getGameTitle() { return gameTitle; }
    public String getEventDate() { return eventDate; }
    public String getEventTime() { return eventTime; }

    public void setId(int id)               { this.id = id; }
    public void setName(String name)        { this.name = name; }
    public void setGameTitle(String g)      { this.gameTitle = g; }
    public void setEventDate(String d)      { this.eventDate = d; }
    public void setEventTime(String t)      { this.eventTime = t; }

    @Override
    public String toString() {
        return String.format("[%d] %-25s | %-20s | %s %s",
                id, name, gameTitle, eventDate, eventTime);
    }
}