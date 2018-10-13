package com.west2.fzuTimeMachine.model.vo;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @description: 我的时光视图对象
 * @author: hlx 2018-10-07
 **/
@Data
public class TimeCollectionVO {

    private Integer id;

    private Integer timeId;

    private Integer userId;

    private String title;

    private String imgUrl;

    private String content;
}
