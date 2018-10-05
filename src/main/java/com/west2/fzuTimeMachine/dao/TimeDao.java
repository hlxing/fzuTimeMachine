package com.west2.fzuTimeMachine.dao;

import com.west2.fzuTimeMachine.model.po.Time;
import org.apache.ibatis.annotations.Param;

/**
 * @description: 时光DAO
 * @author: hlx 2018-10-03
 **/
public interface TimeDao {

    void save(Time time);

    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    void getById(@Param("id") Integer id);

    void updateById(Time time);
}
