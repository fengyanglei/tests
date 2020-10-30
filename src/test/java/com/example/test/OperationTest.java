package com.example.test;

import org.junit.Test;

/**
 * 位运算
 */
public class OperationTest {

    /**
     * 按位与 and &
     * 相同位的两个数字都为1，则为1；若有一个不为1，则为0。
     */
    @Test
    public void t1() {
        //判断奇偶数
        int a = 21;
        System.out.println((a & 1) == 0 ? "偶数" : "奇数");
    }

    /**
     * 按位或 or |
     * 相同位只要一个为1即为1
     */
    @Test
    public void t2() {
        int a = 22;
        //把最后一位变成1
        System.out.println(a | 1);
        //把最后一位变成0
        System.out.println(a | 1 - 1);
    }

    /**
     * 按位异或 xor ^
     * 相同位不同则为1，相同则为0。
     */
    @Test
    public void t3() {
        //两数交换
        int a = 10;
        int b = 20;

        a = a + b;
        b = a - b;
        a = a - b;
        System.out.println(a);
        System.out.println(b);

        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println(a);
        System.out.println(b);
    }

    /**
     * 按位取反 not ~
     */
    @Test
    public void t4() {
        int a = 2;
        System.out.println(~a);
    }

    /**
     * 左移
     */
    @Test
    public void t5() {
        System.out.println(2 << 3);
    }

    /**
     * 带符号右移
     */
    @Test
    public void t6() {
        System.out.println(-128 >> 3);
    }

    /**
     * 无符号右移
     */
    @Test
    public void t7() {
        System.out.println(128 >>> 3);
        System.out.println(-128 >>> 3);
    }


}
