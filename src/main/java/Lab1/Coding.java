package Lab1;

import java.io.*;
import java.util.*;

public class Coding {

    private static int uniqueChars; // Count of unique characters

    public static void prepareFano(String file, int strLen) throws IOException {
        LinkedHashMap<String, Float> charsFreq = Entropy.calculateCharsFrequency(file, strLen);
        System.out.println("SIZE: " + charsFreq.size());
        System.out.println(Arrays.toString(charsFreq.entrySet().toArray()));
        File fileOut = new File(file + ".enc2");
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

        fano(charsFreq, 0, charsFreq.size() - 1, 0, codes, len);    // Кодирование для 3 лабы
        printLab3Fano(charsFreq, file, codes, len);
        encodeFile(file, fout, charsFreq, codes);

        /*int base = 3;
        int[] medians = new int[base - 1];
        ternaryFano(charsFreq, 0, charsFreq.size() - 1, 0, codes, len, base, medians);  // Кодирование для 4 лабы
        printLab4Fano(charsFreq, file, codes, len);
        encodeFile(file, fout, charsFreq, codes);*/
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

    public static void printLab3Fano(LinkedHashMap<String, Float> charsFreq, String file, int[][] codes, int[] len) throws IOException {
        printTable(charsFreq, codes, len);

        float middleLengthCodeword = middleLengthCodeword(charsFreq, len);
        float entropy = Entropy.Shanon(String.valueOf(file), 1);

        System.out.println("Энтропия исходного файла:     " + entropy);

        float r = redundancy(middleLengthCodeword, entropy);
        System.out.printf("Избыточность кодирования: %12.6f\n", r);
        System.out.println();
    }

    public static void printTable(LinkedHashMap<String, Float> charsFreq, int[][] codes, int[] len) throws IOException {
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

    public static float redundancy(float middleLen, float entropy) {
        return (middleLen - entropy);
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



    public static void ternaryFano(LinkedHashMap<String, Float> charsFreq, int L, int R, int currCodewordLen, int[][] codes, int[] len, int base, int[] medians) {
        if (L < R) {
            ternaryMedian(charsFreq, L, R, base, medians);

            int[] newMedians = new int[base - 1];
            if (base - 1 >= 0) System.arraycopy(medians, 0, newMedians, 0, base - 1);

            for (int i = L; i <= R; i++) {
                codes[i][currCodewordLen] = getCode(i, base, medians);
                len[i]++;
            }
            currCodewordLen++;

            int l = L, r;
            for (int j = 0; j < base - 1; j++) {
                r = newMedians[j];
                ternaryFano(charsFreq, l, r, currCodewordLen, codes, len, base, medians);
                l = r + 1;
            }
            ternaryFano(charsFreq, newMedians[base - 2] + 1, R, currCodewordLen, codes, len, base, medians);
        }

    }

    public static void ternaryMedian(LinkedHashMap<String, Float> charsFreq, int L, int R, int base, int[] medians) {
        float sl, sr;
        int temp_R = R, del = base - 1;
        for (int k = base - 2; k >= 0; k--) {
            sl = 0;
            for (int i = L; i < temp_R; i++) {
                sl += charsFreq.get(charsFreq.keySet().toArray()[i]);
            }

            sr = charsFreq.get(charsFreq.keySet().toArray()[temp_R]);
            medians[k] = temp_R;

            while (sl / del >= sr) {
                medians[k]--;
                sl -= charsFreq.get(charsFreq.keySet().toArray()[medians[k]]);
                sr += charsFreq.get(charsFreq.keySet().toArray()[medians[k]]);
                if (medians[k] < 0) {
                    medians[k] = 0;
                    break;
                }
            }
            del--;

            if (medians[k] == 0) {
                temp_R = 1;
            } else {
                temp_R = medians[k] - 1;
            }
        }
    }

    // Returns 0, 1, 2 depends on element position relative median position
    public static int getCode(int i, int base, int[] medians) {
        for (int j = 0; j < base - 1; j++) {
            if (i <= medians[j]) {
                return j;
            }
        }
        return base - 1;
    }

    public static void printLab4Fano(LinkedHashMap<String, Float> charsFreq, String file, int[][] codes, int[] len) throws IOException {
        printTable(charsFreq, codes, len);
        middleLengthCodeword(charsFreq, len);
        float entropy = Entropy.Shanon(String.valueOf(file), 1);
        System.out.println("Энтропия исходного файла:     " + entropy);
        System.out.println();
    }
}
