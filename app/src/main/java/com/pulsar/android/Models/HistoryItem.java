package com.pulsar.android.Models;

public class HistoryItem {

    String strDate;
    String strAmount;
    String strToken;
    String strUsd;

    public HistoryItem(String date, String amount, String token, String usd) {
        this.strDate=date;
        this.strToken=token;
        this.strAmount=amount;
        this.strUsd=usd;

    }

    public String getDate() {
        return strDate;
    }

    public String getStrToken() {
        return strToken;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public String getStrUsd() {
        return strUsd;
    }

}