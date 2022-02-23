package Lab1;

import java.io.*;
import java.util.*;

public class Coding {

    private static int uniqueChars; // Count of unique characters

    public static void prepareFano(String file, int strLen) throws IOException {
        // int N = 3; // For Lab4
        LinkedHashMap<String, Float> charsFreq = Entropy.calculateCharsFrequency(file, strLen);
        File fileOut = new File(file + ".enc");
        BufferedWriter fout = new BufferedWriter(new FileWriter(fileOut));

        // Filling matrix of codes with default value
        uniqueChars = charsFreq.keySet().size();
        int[][] codes = new int[uniqueChars][uniqueChars];
        for (int i = 0; i < uniqueChars; i++) {
            for (int j = 0; j < uniqueChars; j++) {
                codes[i][j] = -999;
            }
        }

        int[] len = new int[uniqueChars]; // Array of codewords lengths

        int charCountInText = Entropy.getCharCount();
        for (String i : charsFreq.keySet()) {
            float ver = charsFreq.get(i) / charCountInText;
            charsFreq.put(i, ver);
        }

        // сортировка мапы по значению (по убыванию)
        LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<>();
        charsFreq.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        charsFreq = sortedMap;
        System.out.println("Файл: " + file);
        System.out.println(charsFreq);

        fano(charsFreq, 0, charsFreq.size() - 1, 0, codes, len);    // Кодирование для 4 лабы
        printLab4Fano(charsFreq, file, fout, codes, len);
        encodeFile(file, fout, charsFreq, codes);

        /*int[] m = new int[N - 1];
        ternaryEncoding1(charsFreq, 0, charsFreq.size() - 1, 0, codes, len, N, m);  // Кодирование для 5 лабы
        printTable(charsFreq, fout, codes, len);*/

    }

    public static void fano(LinkedHashMap<String, Float> charsFreq, int L, int R, int currCodewordLen, int[][] codes, int[] len) {
        if (L < R) {
            int median = findMedian(charsFreq, L, R);
            for (int i = L; i <= R; i++) {
                if (i < median) {
                    codes[i][currCodewordLen] = 0;
                } else {
                    codes[i][currCodewordLen] = 1;
                }
                len[i]++;
            }

            currCodewordLen++;
            fano(charsFreq, L, median - 1, currCodewordLen, codes, len);
            fano(charsFreq, median, R, currCodewordLen, codes, len);
        }
    }

    public static int findMedian(LinkedHashMap<String, Float> charsFreq, int L, int R) {
        Object[] freqArr = charsFreq.keySet().toArray();
        float leftSum = 0;
        for (int i = L; i < R; i++) {
            leftSum += charsFreq.get(freqArr[i]);
        }

        float rightSum = charsFreq.get(freqArr[R]);
        int median = R;

        float prevDiff = 0, currDiff = 0;
        while (leftSum > rightSum) {
            prevDiff = Math.abs(leftSum - rightSum);
            median--;
            leftSum -= charsFreq.get(freqArr[median]);
            rightSum += charsFreq.get(freqArr[median]);
            currDiff = Math.abs(leftSum - rightSum);
        }

        if (prevDiff != 0 && currDiff != 0 && currDiff > prevDiff) {
            median++;
        }

        return median;
    }

    public static void printLab4Fano(LinkedHashMap<String, Float> charsFreq, String file, BufferedWriter fout, int[][] codes, int[] len) throws IOException {
        printTable(charsFreq, fout, codes, len);

        float middleLengthCodeword = middleLengthCodeword(charsFreq, len);
        float entropy = Entropy.Shanon(String.valueOf(file), 1); // ??? 5 ???

        System.out.println("Энтропия исходного файла:     " + entropy);

        float r = redundancy(middleLengthCodeword, entropy);
        System.out.printf("Избыточность кодирования: %12.6f\n", r);
        System.out.println();
    }

    public static void printTable(LinkedHashMap<String, Float> charsFreq, BufferedWriter fout, int[][] codes, int[] len) throws IOException {
        System.out.println("\nСимвол \t Вероятность \t  Длина \t Кодовое слово");
        for (int i = 0; i < uniqueChars; i++) {
            System.out.format("%3s%16.6f%10s", charsFreq.keySet().toArray()[i], charsFreq.values().toArray()[i], len[i]);
            System.out.print("\t\t\t");
            for (int j = 0; j < uniqueChars; j++) {
                if (j < len[i] && codes[i][j] != -999) {
                    System.out.print(codes[i][j] + " ");
                } else break;
            }
            System.out.println();
        }
        //fout.flush();
        //fout.close();
    }

    public static float middleLengthCodeword(LinkedHashMap<String, Float> P, int[] len) {
        float sum = 0, ver;
        for (int i = 0; i < uniqueChars; i++) {
            ver = (float) P.values().toArray()[i];
            sum += len[i] * ver;
        }
        System.out.println("Средняя длина кодового слова: " + sum);
        return sum;
    }

    public static float redundancy(float Lmid, float entropy) {
        return (Lmid - entropy);
    }

    public static void encodeFile(String inFile, BufferedWriter fout, LinkedHashMap<String, Float> charsFreq, int[][] codes) throws IOException {
        // Fill list of codewords for characters
        ArrayList<String> binaryCharsCodes = new ArrayList<>();
        for (int i = 0; i < codes.length; i++) {
            StringBuilder tempBinaryCode = new StringBuilder();
            for (int j = 0; j < codes[i].length; j++) {
                if (codes[i][j] != -999) {
                    tempBinaryCode.append(codes[i][j]);
                }
            }
            binaryCharsCodes.add(tempBinaryCode.toString());
        }

        // Read source file
        File readFile = new File(inFile);
        FileInputStream fin = new FileInputStream(readFile);
        BufferedReader buf = new BufferedReader(new InputStreamReader((fin)));
        String str = buf.readLine();

        List<Object> listOfUniqueCharacters = Arrays.asList(charsFreq.keySet().toArray());

        // Read character from source file and write codeword to result file
        for (int i = 0; i < str.length(); i++) {
            int characterIndex = listOfUniqueCharacters.indexOf(String.valueOf(str.charAt(i)));
            fout.write(binaryCharsCodes.get(characterIndex));
        }
        fout.flush();
        fout.close();
    }

    /*public static void ternaryEncodingMed(LinkedHashMap<String, Float> P, int L, int R, int N, int[] m) {
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
    }*/

    /*public static void ternaryEncoding1(LinkedHashMap<String, Float> P, int L, int R, int k, int[][] codes, int[] len, int N, int[] m) {
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

    }*/

    /*public static int get_code(int i, int N, int[] mid) {
        for (int j = 0; j < N - 1; j++) {
            if (i <= mid[j])
                return j;
        }
        return N - 1;
    }*/
}
