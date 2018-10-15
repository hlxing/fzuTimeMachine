package com.west2.fzuTimeMachine.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @description: 时光审核DTO
 * @author: hlx 2018-10-12
 **/
@ApiModel(description = "时光审核DTO")
@Data
public class TimeCheckDTO implements Serializable {

    @NotNull
    @ApiModelProperty(required = true,notes = "时光id",example = "13")
    private Integer timeId;

    @NotNull
    @ApiModelProperty(required = true,notes = "审核状态(0为成功,其他失败)",example = "0")
    private Integer status;

    public TimeCheckDTO() {
    }

}
