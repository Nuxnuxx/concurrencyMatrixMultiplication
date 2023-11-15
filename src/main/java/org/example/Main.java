package org.example;
import java.util.scanner;
import java.util.concurrent.forkjoinpool;

public class main {
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
				MatrixUtils.autoMatrixValues(A, scanner);

        System.out.println("Entrez la Matrice B:");
        // enterMatrixValues(B, scanner);
				MatrixUtils.autoMatrixValues(B, scanner);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        try {
            MatrixMultiplier task = new MatrixMultiplier(A, B, size);
            int[][] result = forkJoinPool.invoke(task);

            System.out.println("Matrice Résultat:");
						MatrixUtils.printMatrix(result);
        } finally {
            forkJoinPool.shutdown();
        }
    }
}
