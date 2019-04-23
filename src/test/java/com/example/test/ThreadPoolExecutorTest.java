package com.example.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池
 */
@SpringBootTest
public class ThreadPoolExecutorTest {

    /**
     * 线程池使用
     * @throws InterruptedException
     */
    @Test
    public void t() throws InterruptedException {

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println(LocalTime.now() + "  " + Thread.currentThread().getName() + " run");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(6, 10, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 1; i <= 4; i++) {
            executor.execute(myRunnable);
            executor.execute(myRunnable);
            executor.execute(myRunnable);
            System.out.println("--- 开三个_" + i + " ---");
            System.out.println("核心线程数" + executor.getCorePoolSize());
            System.out.println("线程池数" + executor.getPoolSize());
            System.out.println("队列任务数" + executor.getQueue().size());
        }

        Thread.sleep(8000);
        System.out.println("----8秒之后----");
        System.out.println("核心线程数" + executor.getCorePoolSize());
        System.out.println("线程池数" + executor.getPoolSize());
        System.out.println("队列任务数" + executor.getQueue().size());

    }

    /**
     * 并发处理数据汇总
     * @throws Exception
     */
    @Test
    public void t2() throws Exception {
        //使用的线程数
        int threadCounts = 10;

        //数据集
        List<Integer> list = new ArrayList<Integer>();
        for (int j = 0; j <= 1000000; j++) {
            list.add(j);
        }

        //平均分割List
        int len = list.size() / threadCounts;
        //List中的数量没有线程数多（很少存在）
        if (len == 0) {
            threadCounts = list.size();//采用一个线程处理List中的一个元素
            len = list.size() / threadCounts;//重新平均分割List
        }

        //执行线程list
        List<Callable<Long>> callList = new ArrayList<>();
        for (int i = 0; i < threadCounts; i++) {
            //分割后数据
            final List<Integer> subList;
            if (i == threadCounts - 1) {
                subList = list.subList(i * len, list.size());
            } else {
                subList = list.subList(i * len, len * (i + 1) > list.size() ? list.size() : len * (i + 1));
            }

            //采用匿名内部类实现
            callList.add(() -> {
                long subSum = 0L;
                for (Integer j : subList) {
                    subSum += j;
                }
                System.out.println("分配给线程：" + Thread.currentThread().getName() + "那一部分List的整数和为：\tSubSum:" + subSum);
                return subSum;
            });
        }

        //线程池,执行,结果累加
        ExecutorService exec = Executors.newFixedThreadPool(threadCounts);
        List<Future<Long>> futureList = exec.invokeAll(callList);
        long sum = 0;
        for (Future<Long> future : futureList) {
            sum += future.get();
        }
        exec.shutdown();
        System.out.println(sum);
    }

    @Test
    public void t3() {

    }

}
