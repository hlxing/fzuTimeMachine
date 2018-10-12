package com.west2.fzuTimeMachine.dao;

import com.west2.fzuTimeMachine.model.po.Time;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 时光DAO
 * @author: hlx 2018-10-03
 **/
public interface TimeDao {

    void save(Time time);

    void updateStatusAndVisible(Time time);

    Time get(@Param("id") Integer id);

    void update(Time time);

    void updatePraise(@Param("id") Integer id, @Param("praiseNum") Integer praiseNum);

    void updateVisible(@Param("timeId") Integer timeId, @Param("visible") Byte visible);

    List<Time> getByUserId(Integer userId);

    List<Time> getByUncheck();
}
