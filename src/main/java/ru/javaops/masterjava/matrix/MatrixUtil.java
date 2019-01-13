package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        List<Future<int[]>> futures = new ArrayList<>();

        for (int i = 0; i < matrixSize; i++) {
            final int index = i;
            futures.add(executor.submit(() -> collectNewRow(matrixA, matrixB, index, matrixSize)));
        }

        for (int i = 0; i < futures.size(); i++) {
            matrixC[i] = futures.get(i).get();
        }

        return matrixC;
    }

    private static int[] collectNewRow(int[][] matrixA, int[][] matrixB, int rowIndex, int size){
        int[] resultRow = new int[size];
        for (int j = 0; j < size; j++) {
            int sum = 0;
            for (int k = 0; k < size; k++) {
                sum += matrixA[rowIndex][k] * matrixB[k][j];
            }
            resultRow[j] = sum;
        }
        return resultRow;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[] column = new int[matrixSize];

        try {
            for (int i = 0; ; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    column[j] = matrixB[j][i];
                }

//            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    int[] row = matrixA[j];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += row[k] * column[k];
                    }
                    matrixC[j][i] = sum;
                }
//            }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }


        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
