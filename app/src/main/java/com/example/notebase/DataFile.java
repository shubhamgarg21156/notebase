package com.example.notebase;

public class DataFile {
    String ccolor;
    String tcolor;
    String title;
    String content;
    public DataFile(){}

    public DataFile(String content,String title,String ccolor,String tcolor){
        this.content=content;
        this.title=title;
        this.ccolor=ccolor;
        this.tcolor=tcolor;
    }

    public String getTcolor() {
        return tcolor;
    }

    public String getCcolor() {
        return ccolor;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
