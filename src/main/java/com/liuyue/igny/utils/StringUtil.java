package com.liuyue.igny.utils;

public class StringUtil {
    public static String reverse(String msg) {
        int[] codePoints = msg.codePoints().toArray();
        StringBuilder sb = new StringBuilder();

        for (int i = codePoints.length - 1; i >= 0; i--) {
            sb.appendCodePoint(codePoints[i]);
        }

        return sb.toString();
    }
}
