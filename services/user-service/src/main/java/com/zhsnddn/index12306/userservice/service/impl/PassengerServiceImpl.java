package com.zhsnddn.index12306.userservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhsnddn.index12306.framework.starter.cache.DistributedCache;
import com.zhsnddn.index12306.framework.starter.common.toolkit.BeanUtil;
import com.zhsnddn.index12306.framework.starter.convention.exception.ClientException;
import com.zhsnddn.index12306.framework.starter.convention.exception.ServiceException;
import com.zhsnddn.index12306.framework.starter.user.core.UserContext;
import com.zhsnddn.index12306.userservice.common.enums.VerifyStatusEnum;
import com.zhsnddn.index12306.userservice.dao.entity.PassengerDO;
import com.zhsnddn.index12306.userservice.dao.mapper.PassengerMapper;
import com.zhsnddn.index12306.userservice.dto.req.PassengerRemoveReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.PassengerReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.PassengerUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.PassengerRespDTO;
import com.zhsnddn.index12306.userservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.zhsnddn.index12306.userservice.common.constant.RedisKeyConstant.USER_PASSENGER_LIST;

/**
 * 乘车人服务实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerMapper passengerMapper;
    private final DistributedCache distributedCache;

    @Override
    public List<PassengerRespDTO> listPassengerQueryByUsername(String username) {
        String actualUserPassengerListStr = getActualUserPassengerListStr(username);
        return Optional.ofNullable(actualUserPassengerListStr)
                .map(each -> JSON.parseArray(each, PassengerDO.class))
                .map(each -> BeanUtil.convert(each, PassengerRespDTO.class))
                .orElse(null);
    }

    @Override
    public void savePassenger(PassengerReqDTO requestParam) {
        verifyPassenger(requestParam);
        String username = UserContext.getUsername();
        try {
            PassengerDO passengerDO = BeanUtil.convert(requestParam, PassengerDO.class);
            passengerDO.setUsername(username);
            passengerDO.setCreateDate(new Date());
            passengerDO.setVerifyStatus(VerifyStatusEnum.REVIEWED.getCode());
            int inserted = passengerMapper.insert(passengerDO);
            if (!SqlHelper.retBool(inserted)) {
                throw new ServiceException(String.format("[%s] 新增乘车人失败", username));
            }
        } catch (Exception ex) {
            if (ex instanceof ServiceException) {
                log.error("{}，请求参数：{}", ex.getMessage(), JSON.toJSONString(requestParam));
            } else {
                log.error("[{}] 新增乘车人失败，请求参数：{}", username, JSON.toJSONString(requestParam), ex);
            }
            throw ex;
        }
        distributedCache.delete(USER_PASSENGER_LIST + username);
    }

    @Override
    public void removerPassenger(PassengerRemoveReqDTO requestParam) {
        String username = UserContext.getUsername();
        LambdaQueryWrapper<PassengerDO> queryWrapper = Wrappers.lambdaQuery(PassengerDO.class)
                .eq(PassengerDO::getUsername, username)
                .eq(PassengerDO::getId, requestParam.getId());
        PassengerDO passengerDO = passengerMapper.selectOne(queryWrapper);
        if (Objects.isNull(passengerDO)) {
            throw new ClientException("乘车人不存在");
        }
        try {
            LambdaUpdateWrapper<PassengerDO> deleteWrapper = Wrappers.lambdaUpdate(PassengerDO.class)
                    .eq(PassengerDO::getUsername, username)
                    .eq(PassengerDO::getId, requestParam.getId());
            // 逻辑删除，修改数据库表记录 del_flag
            int deleted = passengerMapper.delete(deleteWrapper);
            if (!SqlHelper.retBool(deleted)) {
                throw new ServiceException(String.format("[%s] 删除乘车人失败", username));
            }
        } catch (Exception ex) {
            if (ex instanceof ServiceException) {
                log.error("{}，请求参数：{}", ex.getMessage(), JSON.toJSONString(requestParam));
            } else {
                log.error("[{}] 删除乘车人失败，请求参数：{}", username, JSON.toJSONString(requestParam), ex);
            }
            throw ex;
        }
        distributedCache.delete(USER_PASSENGER_LIST + username);
    }

    @Override
    public void updatePassenger(PassengerUpdateReqDTO requestParam) {
        String username = UserContext.getUsername();
        try {
            PassengerDO passengerDO = BeanUtil.convert(requestParam, PassengerDO.class);
            passengerDO.setUsername(username);
            LambdaUpdateWrapper<PassengerDO> updateWrapper = Wrappers.lambdaUpdate(PassengerDO.class)
                    .eq(PassengerDO::getUsername, username)
                    .eq(PassengerDO::getId, requestParam.getId());
            int updated = passengerMapper.update(passengerDO, updateWrapper);
            if (!SqlHelper.retBool(updated)) {
                throw new ServiceException(String.format("[%s] 更改乘车人失败", username));
            }
        } catch (Exception ex) {
            if (ex instanceof ServiceException) {
                log.error("{}，请求参数：{}", ex.getMessage(), JSON.toJSONString(requestParam));
            } else {
                log.error("[{}] 新增乘车人失败，请求参数：{}", username, JSON.toJSONString(requestParam), ex);
            }
            throw ex;
        }
        distributedCache.delete(USER_PASSENGER_LIST + username);

    }

    private String getActualUserPassengerListStr(String username) {
        return distributedCache.safeGet(
                USER_PASSENGER_LIST + username,
                String.class,
                () -> {
                    LambdaQueryWrapper<PassengerDO> queryWrapper = Wrappers.lambdaQuery(PassengerDO.class)
                            .eq(PassengerDO::getUsername, username);
                    List<PassengerDO> passengerDOList = passengerMapper.selectList(queryWrapper);
                    return CollUtil.isNotEmpty(passengerDOList) ? JSON.toJSONString(passengerDOList) : null;
                },
                1,
                TimeUnit.DAYS
        );
    }

    private void verifyPassenger(PassengerReqDTO requestParam) {
        int length = requestParam.getRealName().length();
        if (!(length >= 2 && length <= 16)) {
            throw new ClientException("乘车人名称请设置2-16位的长度");
        }
        if (!IdcardUtil.isValidCard(requestParam.getIdCard())) {
            throw new ClientException("乘车人证件号错误");
        }
        if (!PhoneUtil.isMobile(requestParam.getPhone())) {
            throw new ClientException("乘车人手机号错误");
        }
    }
}
