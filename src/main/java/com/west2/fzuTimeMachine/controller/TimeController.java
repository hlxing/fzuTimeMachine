package com.west2.fzuTimeMachine.controller;

import com.west2.fzuTimeMachine.model.dto.TimeCheckDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUpdateDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadBackDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadDTO;
import com.west2.fzuTimeMachine.model.po.ApiResult;
import com.west2.fzuTimeMachine.model.vo.TimeCollectionVO;
import com.west2.fzuTimeMachine.model.vo.TimeMeVO;
import com.west2.fzuTimeMachine.model.vo.TimeUnCheckVO;
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

    @ApiOperation(value = "时光删除", notes = "删除自己的时光")
    @ApiImplicitParam(name = "timeId", value = "时光id")
    @GetMapping("/delete")
    public ApiResult<String> delete(@RequestParam("timeId") @NotNull Integer timeId, HttpSession session){
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.delete(timeId, (Integer) session.getAttribute("userId"));
        apiResult.setText("delete my time success");
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

    @ApiOperation(value = "时光点赞", notes = "请求一次执行一次,如果已经点赞,则取消点赞;反之")
    @ApiImplicitParam(name = "timeId", value = "时光id")
    @GetMapping("/praise")
    public ApiResult<String> praise(@RequestParam("timeId") @NotNull Integer timeId,HttpSession session){
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.praise(timeId,(Integer) session.getAttribute("userId"));
        apiResult.setText("praise time success");
        return apiResult;
    }

    @ApiOperation(value = "时光审核", notes = "需要管理员权限")
    @PostMapping("/check")
    public ApiResult<String> check(@RequestBody @Valid TimeCheckDTO timeCheckDTO) {
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.check(timeCheckDTO);
        apiResult.setText("check success");
        return apiResult;
    }

    @ApiOperation(value = "获取未审核的时光", notes = "需要管理员权限")
    @GetMapping("/unCheck")
    public ApiResult<List<TimeUnCheckVO>> getUnCheck() {
        ApiResult<List<TimeUnCheckVO>> apiResult = new ApiResult<>();
        List<TimeUnCheckVO> timeUnCheckVOList = timeService.getUnCheck();
        apiResult.setData(timeUnCheckVOList);
        return apiResult;
    }

    @ApiOperation(value = "收藏时光", notes = "收藏喜欢的时光")
    @GetMapping("/collect")
    public ApiResult<String> collect(@RequestParam("timeId") @NotNull Integer timeId,HttpSession session){
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.Collect(timeId,(Integer) session.getAttribute("userId"));
        apiResult.setText("collect time success");
        return apiResult;
    }

    @ApiOperation(value = "取消收藏时光", notes = "删除已经收藏的时光")
    @GetMapping("/unCollect")
    public ApiResult<String> unCollect(@RequestParam("id") @NotNull Integer id,HttpSession session){
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.unCollect(id,(Integer) session.getAttribute("userId"));
        apiResult.setText("unCollect time success");
        return apiResult;
    }

    @ApiOperation(value = "我的收藏", notes = "已经收藏的全部时光")
    @GetMapping("/collection")
    public ApiResult<List<TimeCollectionVO>> getCollection(HttpSession session){
        ApiResult<List<TimeCollectionVO>> apiResult = new ApiResult<>();
        List<TimeCollectionVO> timeCollectionVOS = timeService.getCollection((Integer) session.getAttribute("userId"));
        apiResult.setData(timeCollectionVOS);
        return apiResult;
    }
}
