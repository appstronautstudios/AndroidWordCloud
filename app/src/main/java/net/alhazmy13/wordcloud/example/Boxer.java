package net.alhazmy13.wordcloud.example;

/**
 * Data-binded values that are boxed(such as ones that come from Kotlin,
 * as there's no primitive types in Kotlin..) requires safe unboxing,
 * so there's no NPE(null pointer exception) when binding data.
 * <p>
 * Theres a method for this safeUnbox inside Android SDK,
 * however this doesn't support two-way data binding.
 * <p>
 * Therefore we have to define these two-way safe unboxing methods by ourselves.
 * <p>
 * example usage inside xml
 * <p>
 * <p>
 * <?xml version="1.0" encoding="utf-8"?>
 * <layout xmlns:android="http://schemas.android.com/apk/res/android"
 * <data>
 * <import type="fi.matalamaki.util.Boxer"/>
 * <variable
 * name="intParameter"
 * type="java.lang.Integer"/>
 * </data>
 * <SeekBar
 * android:layout_width="match_parent"
 * android:layout_height="match_parent"
 * android:progress="@={Boxer.unbox(intParameter)}"/>
 * </layout>
 */

public class Boxer {

    public static final char DEAULT_CHAR = ' ';
    private static final int DEFAULT_NUMBER = 0;

    public static boolean unbox(Boolean b) {
        return (b != null) && b;
    }

    public static Boolean boxBoolean(boolean b) {
        return b;
    }

    public static char unbox(Character c) {
        return c != null ? c : DEAULT_CHAR;
    }

    public static Character boxChar(char c) {
        return c;
    }

    public static byte unbox(Byte b) {
        return b != null ? b : DEFAULT_NUMBER;
    }

    public static Byte boxByte(byte b) {
        return b;
    }

    public static short unbox(Short s) {
        return s != null ? s : DEFAULT_NUMBER;
    }

    public static Short boxShort(short s) {
        return s;
    }

    public static int unbox(Integer i) {
        return i != null ? i : DEFAULT_NUMBER;
    }

    public static Integer boxInteger(int i) {
        return i;
    }

    public static long unbox(Long l) {
        return l != null ? l : DEFAULT_NUMBER;
    }

    public static Long boxLong(long l) {
        return l;
    }

    public static float unbox(Float f) {
        return f != null ? f : DEFAULT_NUMBER;
    }

    public static Float boxFloat(float f) {
        return f;
    }

    public static double unbox(Double d) {
        return (d != null) ? d : DEFAULT_NUMBER;
    }

    public static Double boxDouble(double d) {
        return d;
    }


}