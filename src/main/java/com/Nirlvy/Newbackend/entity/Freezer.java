package com.Nirlvy.Newbackend.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Nirlvy
 * @since 2023-04-07
 */
@Getter
@Setter
public class Freezer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private Double temp;

    private Boolean enable;

    private Boolean charge;

    private String location;

    private String seLocation;

    private String coordinates;

    private String img;

    private LocalDateTime deployTime;

    private LocalDateTime supplyTime;
}
