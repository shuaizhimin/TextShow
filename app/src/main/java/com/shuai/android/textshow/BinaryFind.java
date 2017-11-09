package com.shuai.android.textshow;

/**
 * 作者: shuaizhimin
 * 描述: 查找数组数字出现次数
 * 日期: 2017-11-08
 * 时间: 19:13
 * 版本:
 */
public class BinaryFind {
    public static int[] a = new int[]{1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 8, 8, 9, 10, 11, 12, 13, 13, 13, 13, 14, 15, 16, 17,17,17};

    public static void main(String args[]) {
        System.out.println(findCount(a, 17));
    }

    /**
     *  首先查找第一次出现的位置,然后左右遍历取得左右两边位置
     * @param arr
     * @param x
     * @return
     */
    public static int findCount(int[] arr, int x) {
        int mid = binaryFind(arr, x);
        int start, end;
        start = end = mid;
        if (mid > 0) {
            while (start >= 0 && arr[start] == x) start--;
            while (end < arr.length && arr[end] == x) end++;
            return end - start - 1;
        }
        return -1;
    }

    public static int binaryFind(int[] arr, int x) {
        //开始位置
        int start = 0;
        //结束位置
        int end = arr.length - 1;
        while (start <= end) {
            int middle = (start + end) / 2;
            if (x < arr[middle]) {
                //从左侧查找
                end = middle - 1;
            } else if (x > arr[middle]) {
                //从右侧查找
                start = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

}
