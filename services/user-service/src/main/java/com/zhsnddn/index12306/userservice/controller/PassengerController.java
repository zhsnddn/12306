package com.zhsnddn.index12306.userservice.controller;

import com.zhsnddn.index12306.framework.starter.convention.result.Result;
import com.zhsnddn.index12306.framework.starter.idempotent.annotation.Idempotent;
import com.zhsnddn.index12306.framework.starter.idempotent.enums.IdempotentSceneEnum;
import com.zhsnddn.index12306.framework.starter.idempotent.enums.IdempotentTypeEnum;
import com.zhsnddn.index12306.framework.starter.user.core.UserContext;
import com.zhsnddn.index12306.framework.starter.web.Results;
import com.zhsnddn.index12306.userservice.dto.req.PassengerRemoveReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.PassengerReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.PassengerRespDTO;
import com.zhsnddn.index12306.userservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 乘车人控制器
 */
@RestController
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    /**
     * 查询乘车人列表
     */
    @GetMapping("/api/user-service/passenger/query")
    public Result<List<PassengerRespDTO>> listPassengerQueryByUsername() {
        return Results.success(passengerService.listPassengerQueryByUsername(UserContext.getUsername()));
    }

    /**
     * 新增乘车人
     */
    @Idempotent(
            uniqueKeyPrefix = "index12306-user:lock_passenger-alter:",
            key = "T(com.zhsnddn.index12306.framework.starter.user.core.UserContext).getUsername()",
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.RESTAPI,
            message = "正在新增乘车人，请稍后再试..."
    )
    @PostMapping("/api/user-service/passenger/save")
    public Result<Void> savePassenger(@RequestBody PassengerReqDTO requestParam) {
        passengerService.savePassenger(requestParam);
        return Results.success();
    }

    /**
     * 修改乘车人
     */
    @Idempotent(
            uniqueKeyPrefix = "index12306-user:lock_passenger-alter:",
            key = "T(com.zhsnddn.index12306.framework.starter.user.core.UserContext).getUsername()",
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.RESTAPI,
            message = "正在修改乘车人，请稍后再试..."
    )
    @PostMapping("/api/user-service/passenger/update")
    public Result<Void> updatePassenger(@RequestBody PassengerReqDTO requestParam) {
        passengerService.updatePassenger(requestParam);
        return Results.success();
    }

    /**
     * 删除乘车人
     */
    @Idempotent(
            uniqueKeyPrefix = "index12306-user:lock_passenger-alter:",
            key = "T(com.zhsnddn.index12306.framework.starter.user.core.UserContext).getUsername()",
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.RESTAPI,
            message = "正在移除乘车人，请稍后再试..."
    )
    @PostMapping("/api/user-service/passenger/remove")
    public Result<Void> removePassenger(@RequestBody PassengerRemoveReqDTO requestParam) {
        passengerService.removerPassenger(requestParam);
        return Results.success();
    }
}
