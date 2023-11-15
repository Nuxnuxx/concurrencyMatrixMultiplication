package org.example;

public class MatrixUtils {

    // Méthode pour multiplier deux matrices de base
    public static int[][] multiplyMatrices(int[][] A, int[][] B) {
        int size = A.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        // printMatrix(result);
        return result;
    }

    // Méthode pour additionner deux matrices
    public static int[][] add(int[][] A, int[][] B) {
        int size = A.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }

    // Méthode pour soustraire deux matrices
    public static int[][] subtract(int[][] A, int[][] B) {
        int size = A.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }

    // Méthode pour diviser une matrice en 4 sous-matrices
    public static void divideMatrix(int[][] matrix, int[][] C11, int[][] C12, int[][] C21, int[][] C22) {
        int newSize = matrix.length / 2;
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                C11[i][j] = matrix[i][j];
                C12[i][j] = matrix[i][j + newSize];
                C21[i][j] = matrix[i + newSize][j];
                C22[i][j] = matrix[i + newSize][j + newSize];
            }
        }
    }

    // Méthode pour copier les sous-matrices dans la matrice résultante
    public static void copyMatrix(int[][] result, int[][] C11, int[][] C12, int[][] C21, int[][] C22) {
        int newSize = result.length / 2;
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                result[i][j] = C11[i][j];
                result[i][j + newSize] = C12[i][j];
                result[i + newSize][j] = C21[i][j];
                result[i + newSize][j + newSize] = C22[i][j];
            }
        }
    }
}
