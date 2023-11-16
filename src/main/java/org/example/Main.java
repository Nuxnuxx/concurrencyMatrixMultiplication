package org.example;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // let the user chose between manual (M) , auto (A) or random (R) matrix values

        System.out.print("Valeur 'auto' (A), 'manuel' (M) ou 'random' (R) : ");
        String value = scanner.nextLine();

        System.out.print("Entrer la taille des matrices: ");
        int size = scanner.nextInt();

        if ((size & (size - 1)) != 0) {
            scanner.close();
            throw new IllegalArgumentException("La taille des matrices doit être une puissance de 2");
        }

        int[][] A = new int[size][size]; // Initialise la matrice A
        int[][] B = new int[size][size]; // Initialise la matrice B

        //based on the user choice, fill the matrix with the corresponding values
        switch (value) {
            case "M":
                enterMatrixValues(A, scanner);
                enterMatrixValues(B, scanner);
                break;
            case "A":
                autoMatrixValues(A, scanner);
                autoMatrixValues(B, scanner);
                break;
            case "R":
                randomMatrix(A);
                randomMatrix(B);
                break;
            default:
                scanner.close();
                throw new IllegalArgumentException("Valeur invalide");
        }

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

    private static void randomMatrix(int[][] matrix) {
        int size = matrix.length;
        System.out.print("Matrice 'random' : " + '\n');
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (int)(Math.random() * 50);
            }
        }
    }
}