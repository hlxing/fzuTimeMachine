package com.west2.fzuTimeMachine.dao;

import com.west2.fzuTimeMachine.model.po.TimePraise;
import org.apache.ibatis.annotations.Param;


/**
 * @description: 时光点赞DAO
 * @author: yyf 2018-10-11
 **/
public interface TimePraiseDao {

    void save(TimePraise timePraise);

    TimePraise getByUserId(@Param("userId") Integer userId);

    void deleteByUserId(@Param("userId") Integer userId);
}
