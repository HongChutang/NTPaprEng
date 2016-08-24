package com.weapes.ntpaprseng.crawler.util;

import java.text.DecimalFormat;

/**
 * Created by 不一样的天空 on 2016/8/18.
 */
public final class Match {
    private static final double DEFAULT_SIMILARITY = 0.40;

    private static int compute(char[] str1, char[] str2) {
        int size1 = str1.length;
        int size2 = str2.length;
        if (size1 == 0 || size2 == 0) return 0;

        // the start position of substring in original string
        int start1 = -1;
        int start2 = -1;
        // the longest length of common substring
        int longest = 0;

        // record how many comparisons the solution did;
        // it can be used to know which algorithm is better
        int comparisons = 0;

        for (int i = 0; i < size1; ++i) {
            int m = i;
            int n = 0;
            int length = 0;
            while ((n < size2) && (m < size1)) {
                ++comparisons;
                if (str1[m] != str2[n]) {
                    length = 0;
                } else {
                    ++length;
                    if (longest < length) {
                        longest = length;
                        start1 = m - longest + 1;
                        start2 = n - longest + 1;
                    }
                }
                ++m;
                ++n;
            }
        }
        // System.out.printf("from %d of str1 and %d of str2, compared for %d times\n", start1, start2, comparisons);
        return longest;
    }

    public static boolean isMatching(String str1, String sub) {
        int seq = compute(str1.toLowerCase().toCharArray(), sub.toLowerCase().toCharArray());
        double similarity = seq / (double) sub.length();
        DecimalFormat decimalFormat = new DecimalFormat("###.00");
        similarity = Double.parseDouble(decimalFormat.format(similarity));
//        System.out.println(similarity);
        return similarity > DEFAULT_SIMILARITY;
    }
}
