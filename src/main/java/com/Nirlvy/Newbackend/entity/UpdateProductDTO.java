package com.Nirlvy.Newbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDTO {

    @JsonProperty("priceOrCount")
    private Boolean priceOrCount;

    @JsonProperty("num")
    private Double num;

    @JsonProperty("name")
    private String name;

    @JsonProperty("device")
    private String device;

    @JsonProperty("cpeople")
    private String cpeople;

}
