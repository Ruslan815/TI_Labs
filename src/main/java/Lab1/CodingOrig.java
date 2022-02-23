package Lab1;

import java.io.*;
import java.util.LinkedHashMap;

/**
 * Немного другой алгоритм, чем в классе Coding
 */

public class CodingOrig {

    private static int n;

    public static void startFano(String file, int strLen) throws IOException {
        int N = 3;
        LinkedHashMap<String, Float> P = Entropy.calculateCharsFrequency(file, strLen);
        File fileOut = new File(file + ".enc");
        BufferedWriter fout = new BufferedWriter(new FileWriter(fileOut));

        n = P.keySet().size();
        int[][] codes = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                codes[i][j] = -999;
            }
        }
        int[] len = new int[n];
        int charCount = Entropy.getCharCount();
        for (String i : P.keySet()) {
            float ver = P.get(i) / charCount;
            P.put(i, ver);
        }
        System.out.println("Файл: " + file);
        System.out.println(P);

        fano(P, 0, P.size() - 1, 0, codes, len);    // Кодирование для 4 лабы
        printLab4Fano(P, file, fout, codes, len);

        /*int[] m = new int[N - 1];
        ternaryEncoding1(P, 0, P.size() - 1, 0, codes, len, N, m);  // Кодирование для 5 лабы
        printTable(P, fout, codes, len);*/

    }

    public static void printTable(LinkedHashMap<String, Float> P, BufferedWriter fout, int[][] codes, int[] len) throws IOException {
        System.out.println("\nСимвол \t Вероятность \t  Длина \t Кодовое слово");
        for (int i = 0; i < n; i++) {
            System.out.format("%3s%16s%10s", P.keySet().toArray()[i], P.values().toArray()[i], len[i]);
            System.out.print("\t\t\t");
            for (int j = 0; j < n; j++) {
                if (j < len[i] && codes[i][j] != -999) {
                    fout.write(codes[i][j] + "");
                    System.out.print(codes[i][j] + " ");
                } else break;
            }
            System.out.println();
        }
        fout.flush();
        fout.close();
    }

    public static void printLab4Fano(LinkedHashMap<String, Float> P, String file, BufferedWriter fout, int[][] codes, int[] len) throws IOException {
        printTable(P, fout, codes, len);

        float Lmid = middleLengthCodeword(P, len);
        float entropy = Entropy.Shanon(String.valueOf(file), 1);

        System.out.println("H = " + entropy);

        float r = redundancy(Lmid, entropy);
        System.out.printf("r = %.7f\n", r);
        System.out.println();
    }

    public static int Med(LinkedHashMap<String, Float> P, int L, int R) {
        float sl = 0;
        for (int i = L; i <= R; i++) {
            sl += P.get(P.keySet().toArray()[i]);
        }
        float sr = P.get(P.keySet().toArray()[R]);
        int m = R;
        while (sl >= sr) {
            m--;
            sl -= P.get(P.keySet().toArray()[m]);
            sr += P.get(P.keySet().toArray()[m]);
        }
        return m;
    }

    public static void fano(LinkedHashMap<String, Float> P, int L, int R, int k, int[][] codes, int[] len) {
        if (L < R) {
            int m = Med(P, L, R);
            for (int i = L; i <= R; i++) {
                if (i <= m) {
                    codes[i][k] = 0;
                } else
                    codes[i][k] = 1;
                len[i]++;
            }

            k++;
            fano(P, L, m, k, codes, len);
            fano(P, m + 1, R, k, codes, len);
        }
    }

    public static void ternaryEncodingMed(LinkedHashMap<String, Float> P, int L, int R, int N, int[] m) {
        float sl = 0, sr = 0;
        int temp_R = R, del = N - 1;
        for (int k = N - 2; k >= 0; k--) {
            sl = 0;
            sr = 0;
            for (int i = L; i < temp_R; i++)
                sl += P.get(P.keySet().toArray()[i]);
            sr = P.get(P.keySet().toArray()[temp_R]);
            m[k] = temp_R;

            while (sl / del >= sr) {
                m[k]--;
                sl -= P.get(P.keySet().toArray()[m[k]]);
                sr += P.get(P.keySet().toArray()[m[k]]);
                if (m[k] < 0) {
                    m[k] = 0;
                    break;
                }
            }
            del--;
            if (m[k] == 0)
                temp_R = 1;
            else
                temp_R = m[k] - 1;
        }
    }

    public static void ternaryEncoding1(LinkedHashMap<String, Float> P, int L, int R, int k, int[][] codes, int[] len, int N, int[] m) {
        if (L < R) {
            ternaryEncodingMed(P, L, R, N, m);

            int[] m1 = new int[N - 1];
            if (N - 1 >= 0) System.arraycopy(m, 0, m1, 0, N - 1);

            for (int i = L; i <= R; i++) {
                codes[i][k] = get_code(i, N, m);
                len[i]++;
            }
            k++;

            int l = L, r;
            for (int j = 0; j < N - 1; j++) {
                r = m1[j];
                ternaryEncoding1(P, l, r, k, codes, len, N, m);
                l = r + 1;
            }
            ternaryEncoding1(P, m1[N - 2] + 1, R, k, codes, len, N, m);
        }

    }

    public static int get_code(int i, int N, int[] mid) {
        for (int j = 0; j < N - 1; j++) {
            if (i <= mid[j])
                return j;
        }
        return N - 1;
    }

    public static float middleLengthCodeword(LinkedHashMap<String, Float> P, int[] len) {
        float sum = 0, ver;
        for (int i = 0; i < n; i++) {
            ver = (float) P.values().toArray()[i];
            sum += len[i] * ver;
        }
        System.out.println("Lср = " + sum);
        return sum;
    }

    public static float redundancy(float Lmid, float entropy) {
        return (Lmid - entropy);
    }
}
