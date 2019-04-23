package com.example.test;

import org.junit.Test;

/**
 * 数组
 */
public class ArrayTest {

    /**
     * 二维数组中的查找
     * 在一个二维数组中（每个一维数组的长度相同），每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
     * 请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     */
    @Test
    public void t() {
        int len = 10000;
        int[][] array = new int[len][len];
        for (int i = 0; i < len; i++) {
            int[] arr = new int[len];
            for (int j = 0; j < len; j++) {
                arr[j] = j + i;
            }
            array[i] = arr;
        }
        long start = System.currentTimeMillis();
        boolean t = this.find(array, 19004);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(t);
    }

    /* 思路
     * 矩阵是有序的，从左下角来看，向上数字递减，向右数字递增，
     * 因此从左下角开始查找，当要查找数字比左下角数字大时。右移
     * 要查找数字比左下角数字小时，上移
     */
    public boolean find(int[][] array, int target) {
        int rowCount = array.length;
        int colCount = array[0].length;
        int i, j;
        for (i = rowCount - 1, j = 0; i >= 0 && j < colCount; ) {
            if (target == array[i][j])
                return true;
            if (target < array[i][j]) {
                i--;
                continue;
            }
            if (target > array[i][j]) {
                j++;
                continue;
            }
        }
        return false;
    }

    /*
     * 暴力遍历
     */
    public boolean find2(int[][] array, int target) {
        int i, j;
        boolean temp = false, flag = false;
        for (i = 0; i < array.length; i++) {
            for (j = 0; j < array[0].length; j++) {
                if (array[i][j] == target) {
                    temp = true;
                    flag = true;
                    break;
                }
            }
            if (flag == true) break;
        }
        return temp;
    }

}
