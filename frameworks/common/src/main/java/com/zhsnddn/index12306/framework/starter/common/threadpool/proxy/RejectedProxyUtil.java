package com.zhsnddn.index12306.framework.starter.common.threadpool.proxy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.zhsnddn.index12306.framework.starter.common.toolkit.ThreadUtil;

import java.lang.reflect.Proxy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 拒绝策略代理工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RejectedProxyUtil {

    /**
     * 创建拒绝策略代理类
     *
     * @param rejectedExecutionHandler 真正的线程池拒绝策略执行器
     * @param rejectedNum              拒绝策略执行统计器
     * @return 代理拒绝策略
     */
    public static RejectedExecutionHandler createProxy(RejectedExecutionHandler rejectedExecutionHandler, AtomicLong rejectedNum) {
        // 动态代理模式: 增强线程池拒绝策略，比如：拒绝任务报警或加入延迟队列重复放入等逻辑
        return (RejectedExecutionHandler) Proxy
                .newProxyInstance(
                        rejectedExecutionHandler.getClass().getClassLoader(),
                        new Class[]{RejectedExecutionHandler.class},
                        new RejectedProxyInvocationHandler(rejectedExecutionHandler, rejectedNum));
    }

    /**
     * 测试线程池拒绝策略动态代理程序
     */
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1, 3, 1024, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        AtomicLong rejectedNum = new AtomicLong();
        RejectedExecutionHandler proxyRejectedExecutionHandler = RejectedProxyUtil.createProxy(abortPolicy, rejectedNum);
        threadPoolExecutor.setRejectedExecutionHandler(proxyRejectedExecutionHandler);
        for (int i = 0; i < 5; i++) {
            try {
                threadPoolExecutor.execute(() -> ThreadUtil.sleep(100000L));
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        System.out.println("================ 线程池拒绝策略执行次数: " + rejectedNum.get());
    }
}