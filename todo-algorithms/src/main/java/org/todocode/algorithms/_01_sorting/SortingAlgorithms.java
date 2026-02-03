package org.todocode.algorithms._01_sorting;

/**
 * [TodoCode] 排序算法比较
 *
 * <h3>背景:</h3>
 * 理解排序算法有助于:
 * - 分析时间/空间复杂度
 * - 为任务选择正确的算法
 * - 理解分治模式
 *
 * <h3>易错点:</h3>
 * 不要在生产环境使用冒泡排序! 它是 O(n²)。
 * Java 的 Arrays.sort 使用 TimSort (归并-插入混合)。
 *
 * <h3>核心理解:</h3>
 * 快速排序平均 O(n log n)，最坏 O(n²) - 使用随机化基准。
 * 归并排序稳定 O(n log n) - 适合链表。
 * 堆排序 O(n log n) 原地 - 适合内存紧张时。
 */
public class SortingAlgorithms {

    /**
     * 快速排序 - 基于基准的分治算法。
     * 平均: O(n log n), 最坏: O(n²)
     *
     * TODO: 实现随机化基准以避免最坏情况
     */
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    /**
     * 归并排序 - 稳定的分治算法。
     * 时间: O(n log n), 空间: O(n)
     */
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, mid + 1, rightArr, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }

        while (i < n1) arr[k++] = leftArr[i++];
        while (j < n2) arr[k++] = rightArr[j++];
    }

    /**
     * 堆排序 - 原地 O(n log n)
     */
    public static void heapSort(int[] arr) {
        int n = arr.length;

        // 构建最大堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // 从堆中提取元素
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;

        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
