package org.example;

import java.util.concurrent.RecursiveTask;

/**
 * Classe représentant un multiplieur de matrices utilisant le framework Fork-Join en Java.
 */
public class MatrixMultiplier extends RecursiveTask<int[][]> {
    /**
     * Matrice A.
     */
    private final int[][] A;
    /**
     * Matrice B.
     */
    private final int[][] B;
    /**
     * Matrice résultante.
     */
    private final int[][] C;
    /**
     * Taille des matrices.
     */
    private final int size;

    /**
     * Seuil pour la condition de base (à ajuster en fonction de la taille de la matrice).
     */
    private static final int SEUIL = 64;

    /**
     * Constructeur de la classe MatrixMultiplier.
     *
     * @param A    Matrice A à multiplier.
     * @param B    Matrice B à multiplier.
     * @param size Taille des matrices.
     */
    public MatrixMultiplier(int[][] A, int[][] B, int size) {
        this.A = A;
        this.B = B;
        this.size = size;
        this.C = new int[size][size]; // Initialise la matrice résultante
    }

    /**
     * Méthode principale pour effectuer la multiplication de matrices.
     *
     * @return Matrice résultante de la multiplication.
     */
    @Override
    protected int[][] compute() {

        if (size < SEUIL) {
            // Condition de base : multiplication de matrices de petite taille
            return multiplyMatrices(A, B);
        }

        int newSize = size / 2;
        int[][] A11 = new int[newSize][newSize];
        int[][] A12 = new int[newSize][newSize];
        int[][] A21 = new int[newSize][newSize];
        int[][] A22 = new int[newSize][newSize];
        int[][] B11 = new int[newSize][newSize];
        int[][] B12 = new int[newSize][newSize];
        int[][] B21 = new int[newSize][newSize];
        int[][] B22 = new int[newSize][newSize];

        // Divisez les matrices A et B en sous-matrices
        divideMatrix(A, A11, A12, A21, A22);
        divideMatrix(B, B11, B12, B21, B22);

        // Créez des tâches pour les multiplications des sous-matrices
        MatrixMultiplier m1 = new MatrixMultiplier(add(A11, A22), add(B11, B22), newSize);
        MatrixMultiplier m2 = new MatrixMultiplier(add(A21, A22), B11, newSize);
        MatrixMultiplier m3 = new MatrixMultiplier(A11, subtract(B12, B22), newSize);
        MatrixMultiplier m4 = new MatrixMultiplier(A22, subtract(B21, B11), newSize);
        MatrixMultiplier m5 = new MatrixMultiplier(add(A11, A12), B22, newSize);
        MatrixMultiplier m6 = new MatrixMultiplier(subtract(A21, A11), add(B11, B12), newSize);
        MatrixMultiplier m7 = new MatrixMultiplier(subtract(A12, A22), add(B21, B22), newSize);

        // Lancez les tâches en parallèle (multithreading)
        m1.fork();
        m2.fork();
        m3.fork();
        m4.fork();
        m5.fork();
        m6.fork();
        m7.fork();

        // Joignez et combinez les résultats
        int[][] C11 = add(subtract(add(m1.join(), m4.join()), m5.join()), m7.join());
        int[][] C12 = add(m3.join(), m5.join());
        int[][] C21 = add(m2.join(), m4.join());
        int[][] C22 = add(subtract(m1.join(), m2.join()), add(m3.join(), m6.join()));

        // Copiez les sous-matrices dans C
        copyMatrix(C, C11, C12, C21, C22);

        // printMatrix(C);
        return C; // Renvoie la matrice résultante
    }

    /**
     * Méthode pour multiplier deux matrices de base.
     *
     * @param A Matrice A.
     * @param B Matrice B.
     * @return Matrice résultante de la multiplication.
     */
    private int[][] multiplyMatrices(int[][] A, int[][] B) {
        int size = A.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    /**
     * Méthode pour additionner deux matrices.
     *
     * @param A Matrice A.
     * @param B Matrice B.
     * @return Matrice résultante de l'addition.
     */
    private int[][] add(int[][] A, int[][] B) {
        int size = A.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }

    /**
     * Méthode pour soustraire deux matrices.
     *
     * @param A Matrice A.
     * @param B Matrice B.
     * @return Matrice résultante de la soustraction.
     */
    private int[][] subtract(int[][] A, int[][] B) {
        int size = A.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }

    /**
     * Méthode pour diviser une matrice en 4 sous-matrices.
     *
     * @param matrix Matrice à diviser.
     * @param C11    Sous-matrice en haut à gauche.
     * @param C12    Sous-matrice en haut à droite.
     * @param C21    Sous-matrice en bas à gauche.
     * @param C22    Sous-matrice en bas à droite.
     */
    private void divideMatrix(int[][] matrix, int[][] C11, int[][] C12, int[][] C21, int[][] C22) {
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

    /**
     * Méthode pour copier les sous-matrices dans la matrice résultante.
     *
     * @param result Matrice résultante.
     * @param C11    Sous-matrice en haut à gauche.
     * @param C12    Sous-matrice en haut à droite.
     * @param C21    Sous-matrice en bas à gauche.
     * @param C22    Sous-matrice en bas à droite.
     */
    private void copyMatrix(int[][] result, int[][] C11, int[][] C12, int[][] C21, int[][] C22) {
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
