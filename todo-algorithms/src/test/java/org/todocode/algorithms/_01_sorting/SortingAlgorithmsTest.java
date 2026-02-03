package org.todocode.algorithms._01_sorting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * [TodoCode] 排序算法测试
 *
 * 这些测试验证不同排序算法的正确性
 * 并演示参数化测试模式。
 */
class SortingAlgorithmsTest {

    @Test
    @DisplayName("快速排序应该正确排序数组")
    void testQuickSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};

        SortingAlgorithms.quickSort(arr, 0, arr.length - 1);

        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("归并排序应该正确排序数组")
    void testMergeSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};

        SortingAlgorithms.mergeSort(arr, 0, arr.length - 1);

        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("堆排序应该正确排序数组")
    void testHeapSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};

        SortingAlgorithms.heapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("所有算法应该处理空数组")
    void testEmptyArray() {
        int[] empty1 = {};
        int[] empty2 = {};
        int[] empty3 = {};

        SortingAlgorithms.quickSort(empty1, 0, -1);
        SortingAlgorithms.mergeSort(empty2, 0, -1);
        SortingAlgorithms.heapSort(empty3);

        assertEquals(0, empty1.length);
        assertEquals(0, empty2.length);
        assertEquals(0, empty3.length);
    }

    @Test
    @DisplayName("所有算法应该处理单元素数组")
    void testSingleElement() {
        int[] arr1 = {42};
        int[] arr2 = {42};
        int[] arr3 = {42};

        SortingAlgorithms.quickSort(arr1, 0, 0);
        SortingAlgorithms.mergeSort(arr2, 0, 0);
        SortingAlgorithms.heapSort(arr3);

        assertArrayEquals(new int[]{42}, arr1);
        assertArrayEquals(new int[]{42}, arr2);
        assertArrayEquals(new int[]{42}, arr3);
    }

    @Test
    @DisplayName("算法应该处理已排序数组")
    void testAlreadySorted() {
        int[] expected = {1, 2, 3, 4, 5};

        int[] arr1 = {1, 2, 3, 4, 5};
        int[] arr2 = {1, 2, 3, 4, 5};
        int[] arr3 = {1, 2, 3, 4, 5};

        SortingAlgorithms.quickSort(arr1, 0, arr1.length - 1);
        SortingAlgorithms.mergeSort(arr2, 0, arr2.length - 1);
        SortingAlgorithms.heapSort(arr3);

        assertArrayEquals(expected, arr1);
        assertArrayEquals(expected, arr2);
        assertArrayEquals(expected, arr3);
    }

    @Test
    @DisplayName("算法应该处理逆序数组")
    void testReverseSorted() {
        int[] expected = {1, 2, 3, 4, 5};

        int[] arr1 = {5, 4, 3, 2, 1};
        int[] arr2 = {5, 4, 3, 2, 1};
        int[] arr3 = {5, 4, 3, 2, 1};

        SortingAlgorithms.quickSort(arr1, 0, arr1.length - 1);
        SortingAlgorithms.mergeSort(arr2, 0, arr2.length - 1);
        SortingAlgorithms.heapSort(arr3);

        assertArrayEquals(expected, arr1);
        assertArrayEquals(expected, arr2);
        assertArrayEquals(expected, arr3);
    }

    @Test
    @DisplayName("算法应该处理有重复元素的数组")
    void testWithDuplicates() {
        int[] expected = {1, 2, 2, 3, 3, 3, 4};

        int[] arr1 = {3, 2, 3, 1, 2, 4, 3};
        int[] arr2 = {3, 2, 3, 1, 2, 4, 3};
        int[] arr3 = {3, 2, 3, 1, 2, 4, 3};

        SortingAlgorithms.quickSort(arr1, 0, arr1.length - 1);
        SortingAlgorithms.mergeSort(arr2, 0, arr2.length - 1);
        SortingAlgorithms.heapSort(arr3);

        assertArrayEquals(expected, arr1);
        assertArrayEquals(expected, arr2);
        assertArrayEquals(expected, arr3);
    }

    @Test
    @DisplayName("所有算法对随机数组应产生相同结果")
    void testAllAlgorithmsProduceSameResult() {
        Random random = new Random(42); // 固定种子以保证可重复性
        int[] original = random.ints(100, 0, 1000).toArray();

        int[] arr1 = Arrays.copyOf(original, original.length);
        int[] arr2 = Arrays.copyOf(original, original.length);
        int[] arr3 = Arrays.copyOf(original, original.length);

        SortingAlgorithms.quickSort(arr1, 0, arr1.length - 1);
        SortingAlgorithms.mergeSort(arr2, 0, arr2.length - 1);
        SortingAlgorithms.heapSort(arr3);

        assertArrayEquals(arr1, arr2, "快速排序和归并排序应产生相同结果");
        assertArrayEquals(arr2, arr3, "归并排序和堆排序应产生相同结果");
    }
}
