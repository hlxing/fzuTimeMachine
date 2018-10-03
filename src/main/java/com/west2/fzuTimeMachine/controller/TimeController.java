package com.west2.fzuTimeMachine.controller;

import com.west2.fzuTimeMachine.model.dto.UploadBackDTO;
import com.west2.fzuTimeMachine.model.dto.UploadDTO;
import com.west2.fzuTimeMachine.model.po.ApiResult;
import com.west2.fzuTimeMachine.model.vo.UploadVO;
import com.west2.fzuTimeMachine.service.TimeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    public ApiResult<UploadVO> upload(@RequestBody @Valid UploadDTO uploadDTO, HttpSession session) {
        ApiResult<UploadVO> apiResult = new ApiResult<>();
        UploadVO uploadVO = timeService.upload(uploadDTO, (Integer) session.getAttribute("userId"));
        apiResult.setData(uploadVO);
        return apiResult;
    }

    @ApiOperation(value = "时光发布回调", notes = "由七牛云服务器发出,用于校验图片成功上传")
    @PostMapping("/uploadBack")
    public ApiResult<String> uploadBack(@RequestBody UploadBackDTO uploadBackDTO) {
        ApiResult<String> apiResult = new ApiResult<>();
        timeService.uploadBack(uploadBackDTO);
        return apiResult;
    }


}
