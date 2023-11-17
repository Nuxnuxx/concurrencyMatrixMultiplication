package org.example;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

/**
 * Cette classe démontre la multiplication de matrices en utilisant le framework Fork-Join en Java.
 */
public  class Main {

    /**
     * La méthode principale qui initialise les matrices, permet la saisie de l'utilisateur et effectue la multiplication de matrices.
     *
     * @param args Les arguments en ligne de commande (non utilisés dans ce programme).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Permet à l'utilisateur de choisir entre des valeurs de matrice manuelles (M), automatiques (A) ou aléatoires (R)
        System.out.print("Valeur 'auto' (A), 'manuel' (M) ou 'random' (R) : ");
        String value = scanner.nextLine();

        System.out.print("Entrer la taille des matrices : ");
        int size = scanner.nextInt();

        // Vérifie si la taille est une puissance de 2
        if ((size & (size - 1)) != 0) {
            scanner.close();
            throw new IllegalArgumentException("La taille des matrices doit être une puissance de 2");
        }

        int[][] A = new int[size][size]; // Initialise la matrice A
        int[][] B = new int[size][size]; // Initialise la matrice B

        // En fonction du choix de l'utilisateur, remplit la matrice avec les valeurs correspondantes
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
        printMatrix(A);
        printMatrix(B);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        try {
            // Utilise ForkJoinPool pour effectuer la multiplication de matrices en parallèle
            MatrixMultiplier task = new MatrixMultiplier(A, B, size);
            int[][] result = forkJoinPool.invoke(task);

            System.out.println("Matrice Résultat:");
            printMatrix(result);
        } finally {
            // Ferme ForkJoinPool
            forkJoinPool.shutdown();
        }
    }

    /**
     * Permet à l'utilisateur d'entrer manuellement les valeurs de la matrice.
     *
     * @param matrix La matrice à remplir avec les valeurs saisies par l'utilisateur.
     * @param scanner L'objet Scanner pour prendre les entrées de l'utilisateur.
     */
    private static void enterMatrixValues(int[][] matrix, Scanner scanner) {
        int size = matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print("Entrez la rangée " + (i + 1) + ", colonne " + (j + 1) + " : ");
                matrix[i][j] = scanner.nextInt();
            }
        }
    }

    /**
     * Remplit la matrice avec une valeur automatique spécifiée.
     *
     * @param matrix La matrice à remplir avec la valeur automatique.
     * @param scanner L'objet Scanner pour prendre l'entrée de l'utilisateur pour la valeur automatique.
     */
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

    /**
     * Imprime la matrice donnée.
     *
     * @param matrix La matrice à imprimer.
     */
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Remplit la matrice avec des valeurs aléatoires.
     *
     * @param matrix La matrice à remplir avec des valeurs aléatoires.
     */
    private static void randomMatrix(int[][] matrix) {
        int size = matrix.length;
        System.out.print("Matrice 'random' : " + '\n');
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (int) (Math.random() * 50);
            }
        }
    }
}
