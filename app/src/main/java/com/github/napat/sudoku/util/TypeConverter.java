package com.github.napat.sudoku.util;

import android.util.Log;

public class TypeConverter {
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    static public String intArrayToStringSpace(int[] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int element : arr) {
            stringBuilder.append(element);
            stringBuilder.append(" ");
        }
        return removeLastChar(stringBuilder.toString());
    }

    static public int[] revertIntArrayToStringSpace(String intStrSpace) {
        // Log.d(TAG, "intStrSpace = " + intStrSpace);
        String[] arr = intStrSpace.split(" ");
        int arrLen = arr.length;
        int[] out = new int[arrLen];
        int idx = 0;

        for (String intStr : arr) {
            // Log.d(TAG, "intStr = " + intStr);
            // Log.d(TAG, "intStr parse = " + Integer.parseInt(intStr));
            out[idx++] = Integer.parseInt(intStr);
        }
        return out;
    }
}
