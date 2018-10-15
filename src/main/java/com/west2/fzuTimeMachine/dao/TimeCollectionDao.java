package com.west2.fzuTimeMachine.dao;


import com.west2.fzuTimeMachine.model.po.TimeCollection;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 时光收藏DAO
 * @author: yyf 2018-10-13
 **/
public interface TimeCollectionDao {

    void save(TimeCollection timeCollection);

    TimeCollection get(@Param("id") Integer id);

    void delete(@Param("id") Integer id);

    List<TimeCollection> getByUserId(@Param("userId") Integer userId);

    TimeCollection getByTimeIdAndUserId(@Param("timeId") Integer timeId,@Param("userId") Integer userId);
}
