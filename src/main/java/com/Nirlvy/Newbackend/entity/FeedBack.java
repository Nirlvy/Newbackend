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
 * @since 2023-04-08
 */
@Getter
@Setter
public class FeedBack implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Integer id;

    private String content;

    private String device;

    private String img;

    private String contact;

    private Integer phoneNum;

    private LocalDateTime time;

    private Boolean read;

}
