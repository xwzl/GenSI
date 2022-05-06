package com.roy.gensi.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import sun.nio.ch.ThreadPool;

import java.util.concurrent.*;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

public class ThreadPoolHolder {

    //    public ExecutorService executorService = Executors.newFixedThreadPool(10);
    public static ExecutorService pushMessageExecutor = new ThreadPoolExecutor(10, 20, 200L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            new ThreadFactoryBuilder().setNameFormat("thread-call-push-%d").build());

    public static ExecutorService callBusiExecutor = new ThreadPoolExecutor(50, 100, 200L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            new ThreadFactoryBuilder().setNameFormat("thread-call-busi-%d").build());
}
