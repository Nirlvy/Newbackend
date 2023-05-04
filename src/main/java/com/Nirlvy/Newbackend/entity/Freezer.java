package com.Nirlvy.Newbackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
@Data
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deployTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime supplyTime;
}
