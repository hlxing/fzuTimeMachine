package com.west2.fzuTimeMachine.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光更新DTO
 * @author: yyf 2018-10-03
 **/
@Data
public class TimeUpdateDTO implements Serializable {

    private Integer timeId;

    private String title;

    private String content;

    private String label;

    public TimeUpdateDTO() {

    }

}