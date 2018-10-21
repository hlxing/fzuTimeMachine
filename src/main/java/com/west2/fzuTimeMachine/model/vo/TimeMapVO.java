package com.west2.fzuTimeMachine.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光地图视图
 * @author: hlx 2018-10-20
 **/
@Data
public class TimeMapVO implements Serializable {

    private Integer id;

    private Double longitude;

    private Double latitude;
}
