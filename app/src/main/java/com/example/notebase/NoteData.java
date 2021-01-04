package com.example.notebase;

import java.net.URL;

public class NoteData {
    String displayName;
    String email;
    URL photoURL;
    public  NoteData() {}
    public NoteData(String displayName, String email, URL photoURL) {
        this.displayName = displayName;
        this.email=email;
        this.photoURL=photoURL;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

//   // public String getPhotoURL() {
//        return photoURL;
//    }

}
