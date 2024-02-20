
package MultiplicacionDeMatrices;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiplicacionDeMatrices {

    public static void main(String[] args) {
        int numFilasA = 1000;
        int numColumnasA = 1000;
        int numFilasB = 1000;
        int numColumnasB = 1000;

        double[][] matrizA = generateRandomMatriz(numFilasA, numColumnasA);
        double[][] matrizB = generateRandomMatriz(numFilasB, numColumnasB);

        if (numColumnasA != numFilasB) {
            System.out.println("No se puede multiplicar matrices de estas dimensiones.");
            return;
        }

        long sequentialTime = multiplicacionSecuencial(matrizA, matrizB);
        System.out.println("Tiempo Secuencial: " + sequentialTime + " ms");

        int numThreads = 4; // Número de hilos
        long parallelTime = multiplicacionParalela(matrizA, matrizB, numThreads);
        System.out.println("Tiempo Paralelo: " + parallelTime + " ms");
    }

    private static double[][] generateRandomMatriz(int numFilas, int numColumnas) {
        double[][] matriz = new double[numFilas][numColumnas];
        Random random = new Random();

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                matriz[i][j] = random.nextDouble() * 1000000000; // Números aleatorios en punto flotante
            }
        }

        return matriz;
    }

    private static long multiplicacionSecuencial(double[][] matrizA, double[][] matrizB) {
        int numFilasA = matrizA.length;
        int numColumnasA = matrizA[0].length;
        int numColumnasB = matrizB[0].length;

        double[][] result = new double[numFilasA][numColumnasB];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numFilasA; i++) {
            for (int j = 0; j < numColumnasB; j++) {
                for (int k = 0; k < numColumnasA; k++) {
                    result[i][j] += matrizA[i][k] * matrizB[k][j];
                }
            }
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static long multiplicacionParalela(double[][] matrizA, double[][] matrizB, int numThreads) {
        int numFilasA = matrizA.length;
        int numColumnasA = matrizA[0].length;
        int numColumnasB = matrizB[0].length;

        double[][] result = new double[numFilasA][numColumnasB];

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final int[] threadCount = {0};

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numFilasA; i++) {
            final int fila = i;
            executor.submit(() -> {
                for (int j = 0; j < numColumnasB; j++) {
                    for (int k = 0; k < numColumnasA; k++) {
                        result[fila][j] += matrizA[fila][k] * matrizB[k][j];
                    }
                }
                threadCount[0]++;
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Número de hilos utilizados: " + threadCount[0]);

        return endTime - startTime;
    }
}


