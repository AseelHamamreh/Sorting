package com.atypon;

import edu.princeton.cs.algs4.Stopwatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Stream;

/**
 * Homework - Sorting
 * Sort the list of doubles in the fastest possible way.
 * The only method you can change is the sort() method.
 * You can add additional methods if needed, without changing the load() and test() methods.
 */
public class Sorting {

    protected List list = new ArrayList<Integer>();

    /**
     * Loading the text files with double numbers
     */
    protected void load() {
        try (Stream<String> stream = Files.lines(Paths.get("numbers.txt"))) {
            stream.forEach(x -> list.add(Integer.parseInt(x)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Testing of your solution, using 100 shuffled examples
     *
     * @return execution time
     */
    protected double test() {
        Stopwatch watch = new Stopwatch();
        for (int i = 0; i < 100; i++) {
            Collections.shuffle(list, new Random(100));
            sort(list);
        }
        return watch.elapsedTime();
    }

        /**
         * Sorting method - add your code in here
         *
//       * @param list - list to be sorted
         */


        public static void parallelMergeSort(int[] array) {
            SortTask mainTask = new SortTask(array);
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(mainTask);
        }

        private static class SortTask extends RecursiveAction {
            private int[] array;

            public SortTask(int[] array) {
                this.array = array;
            }
            @Override
            protected void compute() {
                if(array.length > 1) {
                    int mid = array.length/2;

                    int[] firstHalf = new int[mid];
                    System.arraycopy(array, 0, firstHalf, 0, mid);

                    int[] secondHalf = new int[array.length - mid];
                    System.arraycopy(array, mid, secondHalf, 0, array.length - mid);

                    SortTask firstHalfTask = new SortTask(firstHalf);
                    SortTask secondHalfTask = new SortTask(secondHalf);

                    invokeAll(firstHalfTask, secondHalfTask);

                    merge(firstHalf, secondHalf, array);
                }
            }
        }

    public static void merge(int[] firstHalf, int[] secondHalf, int[] array) {
        int firstIdx = 0;
        int secIdx = 0;
        int arrIdx = 0;

        while(firstIdx < firstHalf.length && secIdx < secondHalf.length) {
            if(firstHalf[firstIdx] < secondHalf[secIdx]) {
                array[arrIdx] = firstHalf[firstIdx];
                arrIdx++;
                firstIdx++;
            } else {
                array[arrIdx] = secondHalf[secIdx];
                arrIdx++;
                secIdx++;
            }
        }

        while(firstIdx < firstHalf.length) {
            array[arrIdx] = firstHalf[firstIdx];
            arrIdx++;
            firstIdx++;
        }

        while(secIdx < secondHalf.length) {
            array[arrIdx] = secondHalf[secIdx];
            arrIdx++;
            secIdx++;
        }
    }


        private void sort(List list) {
//            Collections.sort(list);
            int[] arr = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = (int) list.get(i);
            }
           // parallelMergeSort(arr);
            Arrays.parallelSort(arr);
        }


}
