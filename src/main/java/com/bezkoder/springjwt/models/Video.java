package com.bezkoder.springjwt.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String videoname;




    private String pathoriginal;


    private String patchblurmodel1;

    private String patchblurmodel2;

    private String opacity;


    private String emailuser;

    private String username;


}
