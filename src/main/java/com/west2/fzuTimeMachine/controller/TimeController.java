package com.west2.fzuTimeMachine.controller;

import com.west2.fzuTimeMachine.model.dto.TimeUpdateDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadBackDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadDTO;
import com.west2.fzuTimeMachine.model.po.ApiResult;
import com.west2.fzuTimeMachine.model.vo.TimeMeVO;
import com.west2.fzuTimeMachine.model.vo.TimeUploadVO;
import com.west2.fzuTimeMachine.service.TimeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 时光控制层
 * @author: hlx 2018-10-03
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("/time")
public class TimeController {

    private TimeService timeService;

    @Autowired
    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @ApiOperation(value = "时光发布", notes = "发布完成后将获取OOS凭证,进一步上传时光图片")
    @PostMapping("/upload")
    public ApiResult<TimeUploadVO> upload(@RequestBody @Valid TimeUploadDTO timeUploadDTO, HttpSession session) {
        ApiResult<TimeUploadVO> apiResult = new ApiResult<>();
        TimeUploadVO timeUploadVO = timeService.upload(timeUploadDTO, (Integer) session.getAttribute("userId"));
        apiResult.setData(timeUploadVO);
        return apiResult;
    }

    @ApiOperation(value = "时光发布回调", notes = "由七牛云服务器发出,用于校验图片成功上传")
    @PostMapping("/uploadBack")
    public ApiResult<String> uploadBack(@RequestBody TimeUploadBackDTO timeUploadBackDTO) {
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.uploadBack(timeUploadBackDTO);
        return apiResult;
    }

    @ApiOperation(value = "时光修改", notes = "修改时光标题、内容、标签")
    @PostMapping("/update")
    public ApiResult<String> updateTime(@RequestBody TimeUpdateDTO timeUpdateDTO) {
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.update(timeUpdateDTO);
        apiResult.setText("update time success");
        return apiResult;
    }

    @ApiOperation(value = "时光删除", notes = "需要管理员权限")
    @ApiImplicitParam(name = "timeId", value = "时光id")
    @GetMapping("/delete")
    public ApiResult<String> delete(@RequestParam("timeId") @NotNull Integer timeId){
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.delete(timeId);
        apiResult.setText("delete success");
        return apiResult;
    }

    @ApiOperation(value = "我的时光", notes = "获取历史发布的时光")
    @GetMapping("/me")
    public ApiResult<List<TimeMeVO>> get(HttpSession session) {
        ApiResult<List<TimeMeVO>> apiResult = new ApiResult<>();
        List<TimeMeVO> timeMeVOList = timeService.getMe((Integer) session.getAttribute("userId"));
        apiResult.setData(timeMeVOList);
        return apiResult;
    }

}
