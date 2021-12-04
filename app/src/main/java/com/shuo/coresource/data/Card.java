package com.shuo.coresource.data;

public class Card {
    private int card_id;
    private String user_name;
    private String card_name;
    private String card_description;
    private String card_date;
    private int card_count;
    private boolean card_consumed;

    public Card(int card_id, String user_name, String card_name, String card_description, String card_date, int card_count, boolean card_consumed) {
        super();
        this.card_id = card_id;
        this.user_name=user_name;
        this.card_name = card_name;
        this.card_description = card_description;
        this.card_date = card_date;
        this.card_count = card_count;
        this.card_consumed = card_consumed;
    }

    public boolean isCard_consumed() {
        return card_consumed;
    }

    public void setCard_consumed(boolean card_consumed) {
        this.card_consumed = card_consumed;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getCard_id() {
        return card_id;
    }
    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }
    public String getCard_name() {
        return card_name;
    }
    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }
    public String getCard_description() {
        return card_description;
    }
    public void setCard_description(String card_description) {
        this.card_description = card_description;
    }
    public String getCard_date() {
        return card_date;
    }
    public void setCard_date(String card_date) {
        this.card_date = card_date;
    }
    public int getCard_count() {
        return card_count;
    }
    public void setCard_count(int card_count) {
        this.card_count = card_count;
    }
}
