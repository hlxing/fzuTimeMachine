package com.west2.fzuTimeMachine.dao;

import com.west2.fzuTimeMachine.model.po.TimePraise;
import org.apache.ibatis.annotations.Param;


/**
 * @description: 时光点赞DAO
 * @author: yyf 2018-10-11
 **/
public interface TimePraiseDao {

    void mark(TimePraise timePraise);

    TimePraise get(@Param("userId") Integer userId);

    void unmark(@Param("userId") Integer userId);
}
