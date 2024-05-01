package com.bezkoder.springjwt.payload.request;

import jakarta.validation.constraints.NotBlank;



public class VideoRequest {




    private String videoname;

    private float opacity;

    private String emailuser;


    private String username;

    public String getVideoname() {
        return videoname;
    }

    public float getOpacity() {
        return opacity;
    }

    public String getEmailuser() {
        return emailuser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }
}
