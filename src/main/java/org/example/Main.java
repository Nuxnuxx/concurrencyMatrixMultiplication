package org.example;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrer la taille des matrices: ");
        int size = scanner.nextInt();

        // test if the size is a power of 2
        if ((size & (size - 1)) != 0) {
            scanner.close();
            throw new IllegalArgumentException("La taille des matrices doit être une puissance de 2");
        }

        int[][] A = new int[size][size]; // Initialise la matrice A
        int[][] B = new int[size][size]; // Initialise la matrice B

        System.out.println("Entrez la Matrice A:");
        // enterMatrixValues(A, scanner);
        autoMatrixValues(A, scanner);

        System.out.println("Entrez la Matrice B:");
        // enterMatrixValues(B, scanner);
        autoMatrixValues(B, scanner);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        try {
            MatrixMultiplier task = new MatrixMultiplier(A, B, size);
            int[][] result = forkJoinPool.invoke(task);

            System.out.println("Matrice Résultat:");
            printMatrix(result);
        } finally {
            forkJoinPool.shutdown();
        }
    }
    private static void enterMatrixValues(int[][] matrix, Scanner scanner) {
        int size = matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print("Entrez la rangée " + (i + 1) + ", colonne " + (j + 1) + ": ");
                matrix[i][j] = scanner.nextInt();
            }
        }
    }

    private static void autoMatrixValues(int[][] matrix, Scanner scanner) {
        int size = matrix.length;
        System.out.print("Valeur 'auto' : ");
        int value = scanner.nextInt();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = value;
            }
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}