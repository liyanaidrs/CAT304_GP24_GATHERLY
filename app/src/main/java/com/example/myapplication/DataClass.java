package com.example.myapplication;

public class DataClass {

    private String dataTitle;
    private String dataParticipant;
    private String dataDesc;
    private String dataLang, dataDate;
    private String dataImage;
    private String key;
    private String c1, c2, c3;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataTitle() {
        return dataTitle;
    }
    public String getDataParticipant() {
        return dataParticipant;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataLang() {
        return dataLang;
    }
    public String getDataDate() {
        return dataDate;
    }
    public String getDataImage() {
        return dataImage;
    }
    public String getC1()
    {
        return c1;
    }
    public String getC2()
    {
        return c2;
    }
    public String getC3()
    {
        return c3;
    }

    public DataClass(String dataTitle, String dataParticipant, String dataDesc, String dataLang, String dataDate, String dataImage, String c1,String c2, String c3) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.dataDate= dataDate;
        this.dataImage = dataImage;
        this.dataParticipant=dataParticipant;
        this.c1=c1;
        this.c2=c2;
        this.c3=c3;

    }
    public DataClass(){}

}