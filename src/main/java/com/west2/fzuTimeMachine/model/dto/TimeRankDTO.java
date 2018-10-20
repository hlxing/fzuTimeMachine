package com.west2.fzuTimeMachine.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 时光排序传输对象
 * @author: hlx 2018-10-15
 **/
@Data
public class TimeRankDTO implements Serializable, Comparable<TimeRankDTO> {

    private Integer id;

    private Integer userId;

    private String imgUrl;

    private String location;

    private Long createTime;

    private Integer praiseNum;

    private Double score;

    public TimeRankDTO() {
    }

    @Override
    public int compareTo(TimeRankDTO o) {
        if (score > o.getScore()) {
            return 1;
        } else {
            return -1;
        }
    }
}
