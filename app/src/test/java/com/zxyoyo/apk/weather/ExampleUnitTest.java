package com.zxyoyo.apk.weather;

import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public  void main() {
        long time = System.currentTimeMillis();
        long beforeTime = time - 24*60*60*1000;
        System.out.println("start-time:"+time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(time);
        String format2= format.format(beforeTime);
        System.out.println("format:"+format1);
        System.out.println("format2:"+format2);
    }
}