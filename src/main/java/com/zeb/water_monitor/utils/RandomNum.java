package com.zeb.water_monitor.utils;

import java.util.Random;

/**
 * @author zeb
 * @Date 2023-05-11 18:43
 */
public class RandomNum {

    public static int generateRandomNum() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000000);
        return randomNumber;
    }

}
