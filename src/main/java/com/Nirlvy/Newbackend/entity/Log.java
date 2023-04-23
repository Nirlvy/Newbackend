package com.Nirlvy.Newbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Log implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Integer id;

    private Boolean io;

    private String device;

    private String product;

    private Double price;

    private String type;

    private Integer count;

    private LocalDateTime time;

    private String cpeople;
}
