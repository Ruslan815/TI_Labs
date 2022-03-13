package lab5;

import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Пример файла
 * 3 5
 * 1 0 1 1 1
 * 0 1 0 1 0
 * 0 0 1 1 1
 *
 * По заданной порождающей матрице определить характеристики
 * линейного кода: размерность кода, количество кодовых слов,
 * минимальное кодовое расстояние.
 */

public class Main {

    private static int n, m;
    private static int[][] matrix;

    private static int codewordsCount;
    private static String[] allMessages;
    private static int[][] codewordsMatrix;

    public static void readMatrixFromFile(String fileName) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();

            String[] matrixSize = line.split(" ");
            n = Integer.parseInt(matrixSize[0]);
            m = Integer.parseInt(matrixSize[1]);
            if (n < 1 || m < 2) {
                System.err.println("Некорректные размеры порождающей матрицы!");
                return;
            }
            if (n >= m) {
                System.err.println("Число N должно быть меньше M!");
                return;
            }

            matrix = new int[n][m];

            line = br.readLine();
            String[] someRow;
            for (int i = 0; i < n; i++) {
                someRow = line.split(" ");
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = Integer.parseInt(someRow[j]);
                }
                line = br.readLine();
            }
        }
    }

    public static void displayMatrix() {
        System.out.println("Матрица:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int[] multiply(int[] someVector, int[][] someMatrix) {
        int n = someMatrix.length;
        int m = someMatrix[0].length;
        if (someVector.length != n) {
            System.err.println("Количество столбцов вектора должно быть равно количеству строк матрицы!");
        }
        int[] resultVector = new int[m];

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                resultVector[j] += someMatrix[i][j] * someVector[i];
                if (resultVector[j] == 2) {
                    resultVector[j] = 0;
                }
            }
        }
        return resultVector;
    }

    public static void createCodewordsMatrix() {
        codewordsCount = (int) Math.pow(2, n);
        allMessages = new String[codewordsCount];
        for (int i = 0; i < codewordsCount; i++) {
            StringBuilder tempBinary = new StringBuilder(Integer.toBinaryString(i));
            int zerosCount = n - tempBinary.length();
            if (zerosCount > 0) {
                for (int j = 0; j < zerosCount; j++) {
                    tempBinary.insert(0, "0");
                }
            }
            // System.out.println(tempBinary);
            allMessages[i] = tempBinary.toString();
        }

        int[] someVector = new int[n];
        codewordsMatrix = new int[codewordsCount][m];

        System.out.println("Таблица кодовых слов:");
        for (int i = 0; i < codewordsCount; i++) { // Проходимся по всем информационным сообщениям
            for (int j = 0; j < someVector.length; j++) { // Заполняем вектор информационным сообщением
                someVector[j] = Integer.parseInt(allMessages[i].substring(j, j + 1));
            }

            codewordsMatrix[i] = multiply(someVector, matrix); // Умножаем вектор на порождающую матрицу
            System.out.print(allMessages[i] + " - ");
            for (int j = 0; j < codewordsMatrix[i].length; j++) { // Выводим полученное кодовое слово
                System.out.print(codewordsMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int getDistance(String firstCode, String secondCode) {
        if (firstCode.length() != secondCode.length()) {
            System.err.println("Длины кодовых слов должны быть одинаковы!");
            return -1;
        }

        int count = 0;
        for (int i = 0; i < firstCode.length(); i++) {
            if (firstCode.charAt(i) != secondCode.charAt(i)) count++;
        }
        return count;
    }

    public static int findMinimalCodeDistance() {
        int minimalDistance = Integer.MAX_VALUE;
        int tempDistance;
        for (int i = 0; i < codewordsCount - 1; i++) {
            for (int j = i + 1; j < codewordsCount; j++) {
                tempDistance = getDistance(Arrays.toString(codewordsMatrix[i]), Arrays.toString(codewordsMatrix[j]));
                // System.out.println(i + "," + j + ": " + tempDistance);
                if (tempDistance < minimalDistance) {
                    minimalDistance = tempDistance;
                }
            }
        }
        return minimalDistance;
    }

    public static void displayCodeParams() {
        createCodewordsMatrix();
        System.out.println("Размерность кода (длина информационных слов): " + n);
        System.out.println("Длина кода (длина кодовых слов): " + m);
        System.out.println("Количество кодовых слов: " + codewordsCount);
        System.out.println("Минимальное кодовое расстояние: " + findMinimalCodeDistance());
        System.out.println("Избыточность кода (m/n): " + (double) m / n);
        System.out.println("Скорость кода (n/m): " + (double) n / m);
    }

    public static void main(String[] args) throws IOException {
        readMatrixFromFile("matrix2.txt");
        displayMatrix();
        displayCodeParams();

    }
}
