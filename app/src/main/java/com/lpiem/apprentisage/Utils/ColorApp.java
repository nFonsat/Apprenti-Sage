/**
 * Created by iem on 14/01/15.
 */
package com.lpiem.apprentisage.Utils;

import java.util.Random;

public class ColorApp {
    public static int generateColor(){
        Random rnd = new Random();
        return android.graphics.Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}