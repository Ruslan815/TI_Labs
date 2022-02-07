package Lab1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String file1 = "first.txt";
        String file2 = "second.txt";
        int textSizeInChars = 10_250;
        char[] uniqueChars = new char[]{'1', '2', '3', '4', '5'};
        int[] p1 = new int[]{20, 20, 20, 20, 20};
        int[] p2 = new int[]{30, 30, 20, 15, 5};

        FileGenerator.generateFile(file1, uniqueChars, p1, textSizeInChars);
        FileGenerator.generateFile(file2, uniqueChars, p2, textSizeInChars);
// file1: oneChar = 2.321928, twoChars =
// file2: oneChar = 2.1332061, twoChars =
        /*int var1 = 20;
        float sum = 0;
        for (int i = 1; i <= 5; i++) {
            sum += -1 * ((p2[i - 1] / 100.0) * Entropy.log2((p2[i - 1] / 100.0f)));
        }
        System.out.println(sum);*/

        displayLab1();
    }

    public static void displayLab1() throws IOException {
        System.out.println("Экспериментальная оценка энтропии (частоты отдельных символов):");
        System.out.print("Файл №1: ");
        System.out.println(Entropy.Shanon("first.txt", 1));
        System.out.print("Файл №2: ");
        System.out.println(Entropy.Shanon("second.txt", 1));
        System.out.println();

        System.out.println("Экспериментальная оценка энтропии (частоты пар символов):");
        System.out.print("Файл №1: ");
        System.out.println(Entropy.Shanon("first.txt", 2));
        System.out.print("Файл №2: ");
        System.out.println(Entropy.Shanon("second.txt", 2));
    }

    /*public static void PrintLab2() throws IOException {
        System.out.println("\nЛаб.раб. №2:");
        System.out.println("\tПроверка на отрывке книги:");
        System.out.println("Текст\t\t\t1 символ \t\t 2 символа \t 3 символа \t 4 символв \t 5 символв");
        FileGenerator.generateText("littlePrince.txt");
        FileGenerator.generateText("mumu.txt");

        System.out.println("littlePrince\t"
                + Entropy.Shanon("littlePrince.txt.out", 1) + "\t\t "
                + Entropy.Shanon("littlePrince.txt.out", 2) + " \t "
                + Entropy.Shanon("littlePrince.txt.out", 3) + " \t "
                + Entropy.Shanon("littlePrince.txt.out", 4) + " \t "
                + Entropy.Shanon("littlePrince.txt.out", 5));
        System.out.println("mumu\t\t\t"
                + Entropy.Shanon("mumu.txt.out", 1) + " \t\t "
                + Entropy.Shanon("mumu.txt.out", 2) + " \t "
                + Entropy.Shanon("mumu.txt.out", 3) + " \t "
                + Entropy.Shanon("mumu.txt.out", 4) + " \t "
                + Entropy.Shanon("mumu.txt.out", 5));
    }*/
}
