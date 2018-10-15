package com.west2.fzuTimeMachine.service;

import com.west2.fzuTimeMachine.model.dto.TimeCheckDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUpdateDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadBackDTO;
import com.west2.fzuTimeMachine.model.dto.TimeUploadDTO;
import com.west2.fzuTimeMachine.model.vo.TimeCollectionVO;
import com.west2.fzuTimeMachine.model.vo.TimeMeVO;
import com.west2.fzuTimeMachine.model.vo.TimeUnCheckVO;
import com.west2.fzuTimeMachine.model.vo.TimeUploadVO;

import javax.validation.Valid;
import java.util.List;


/**
 * @description: 时光服务接口
 * @author: hlx 2018-10-03
 **/
public interface TimeService {

    /**
     * 时光上传
     * @param timeUploadDTO 上传传输对象
     * @param userId 用户id
     * @return 上传响应视图对象,包括key和图片上传凭证uploadToken
     */
    TimeUploadVO upload(TimeUploadDTO timeUploadDTO, Integer userId);

    /**
     * 时光图片上传回调,用于校验图片是否成功上传
     * @param timeUploadBackDTO 上传回调传输对象
     */
    void uploadBack(TimeUploadBackDTO timeUploadBackDTO);

    /**
     * 时光内容更新
     */
    void update(TimeUpdateDTO timeUpdateDTO);

    /**
     * 时光删除
     * @param timeId 时光id
     * @param userId 用户id
     */
    void delete(Integer timeId, Integer userId);

    /**
     * 获取自己发布的时光
     * @param userId 用户id
     * @return 自己时光VO
     */
    List<TimeMeVO> getMe(Integer userId);

    /**
     * 点赞/取消点赞
     * @param timeId 时光id
     * @param userId 用户id
     */
    void praise(Integer timeId,Integer userId);

    /**
     * 时光审核
     * @param timeCheckDTO 时光审核DTO
     */
    void check(TimeCheckDTO timeCheckDTO);

    /**
     * 获取所有未审核的时光
     * @return 时光未审核VO链表
     */
    List<TimeUnCheckVO> getUnCheck();

    /**
     * 收藏时光
     */
    void Collect(Integer timeId, Integer userId);

    /**
     * 取消收藏
     */
    void unCollect(Integer id, Integer userId);

    /**
     * 获取时光收藏
     * @return 时光收藏VO链表
     */
    List<TimeCollectionVO> getCollection(Integer userId);

}
