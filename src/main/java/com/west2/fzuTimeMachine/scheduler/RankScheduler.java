package com.west2.fzuTimeMachine.scheduler;

import com.google.common.collect.Ordering;
import com.west2.fzuTimeMachine.dao.TimeDao;
import com.west2.fzuTimeMachine.dao.UserDao;
import com.west2.fzuTimeMachine.model.dto.TimeRankDTO;
import com.west2.fzuTimeMachine.model.po.Time;
import com.west2.fzuTimeMachine.model.po.WechatUser;
import com.west2.fzuTimeMachine.model.vo.TimeRankVO;
import com.west2.fzuTimeMachine.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @description: 排行调度器
 * @author: hlx 2018-10-15
 **/
@Slf4j
@Component
public class RankScheduler {

    private static final int RANK_SIZE = 10;

    private static final String RANK_KEY = "rank";

    private RedisTemplate redisTemplate;

    private RedisService redisService;

    private TimeDao timeDao;

    private UserDao userDao;

    private ModelMapper modelMapper;

    @Autowired
    public RankScheduler(RedisTemplate redisTemplate, TimeDao timeDao,
                         ModelMapper modelMapper, UserDao userDao,
                         RedisService redisService) {
        this.redisTemplate = redisTemplate;
        this.timeDao = timeDao;
        this.modelMapper = modelMapper;
        this.userDao = userDao;
        this.redisService = redisService;
    }


    @Scheduled(cron = "0 0/1 * * * *")
    public void calculateRank() {
        String value = String.valueOf(System.currentTimeMillis());
        boolean getLock = redisService.tryLock("rank", value);
        if (getLock) {
            log.info("get lock success");
            push();
            redisService.unLock("rank", value);
            log.info("unlock success");
        }
    }

    @SuppressWarnings("unchecked")
    private void push() {
        Long now = System.currentTimeMillis();
        List<Time> timeList = timeDao.getAllByVisible(1);
        List<TimeRankDTO> timeRankDTOS = new ArrayList<>();
        for (Time time : timeList) {
            Double diffHour = (now - time.getCreateTime()) / (1000 * 3600.0);
            Double score = (1 + time.getPraiseNum()) * 6.6 / Math.pow(diffHour, 1.8);
            TimeRankDTO timeRankDTO = modelMapper.map(time, TimeRankDTO.class);
            timeRankDTO.setScore(score);
            timeRankDTOS.add(timeRankDTO);
        }
        Collections.sort(timeRankDTOS);
        List<TimeRankDTO> timeTopRankDTOS = Ordering.natural().greatestOf(timeRankDTOS, RANK_SIZE);

        // 清空排行榜
        redisTemplate.delete(RANK_KEY);

        timeTopRankDTOS.forEach((timeRankDTO) -> {
            TimeRankVO timeRankVO = modelMapper.map(timeRankDTO, TimeRankVO.class);
            WechatUser wechatUser = userDao.get(timeRankDTO.getUserId());
            timeRankVO.setAvatarUrl(wechatUser.getAvatarUrl());
            timeRankVO.setNickName(wechatUser.getNickName());
            redisTemplate.opsForList().rightPush(RANK_KEY, timeRankVO);
        });
    }

}
