package com.Nirlvy.Newbackend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDTO {

    private Boolean priceOrCount;

    private Double num;

    private String type;

    private String img;

    private String device;

    private String cpeople;

}
