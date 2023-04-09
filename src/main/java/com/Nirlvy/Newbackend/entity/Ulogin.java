package com.Nirlvy.Newbackend.entity;

import lombok.Data;

@Data
public class Ulogin {

    private Integer id;

    private String userName;

    private String password;

    private String token;

    private String img;
}
