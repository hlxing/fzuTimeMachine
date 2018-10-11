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

    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    Time get(@Param("id") Integer id);

    void update(Time time);

    void updatePraise(@Param("id") Integer id, @Param("praiseNum") Integer praiseNum);

    void delete(Integer timeId);

    List<Time> getByUserId(Integer userId);
}
