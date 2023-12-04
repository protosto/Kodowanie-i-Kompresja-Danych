package org.example;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Entropy {
    private static Map<Byte, Integer> bytesCount;
    private static final String[] modes = {"red", "green", "blue", "all"};

    public static float calculateEntropy(Pixel[][] pixelArray, String mode) {
        if(!Arrays.asList(modes).contains(mode)) mode = "all";

        bytesCount = new HashMap<>();
        for(int i = 0; i < TGA.height; i++)
            for(int j = 0; j < TGA.width; j++) {
                if(mode.equals("red") || mode.equals("all")) countByte((byte)(pixelArray[i][j].R & 0xff));
                if(mode.equals("green") || mode.equals("all")) countByte((byte)(pixelArray[i][j].G & 0xff));
                if(mode.equals("blue") || mode.equals("all")) countByte((byte)(pixelArray[i][j].B & 0xff));
            }

        float HX = 0;
        for (byte b : bytesCount.keySet()) {
            float pb = (float) bytesCount.get(b) / (TGA.height*TGA.width);
            if(mode.equals("all")) pb /= 3;
            HX -= pb * (Math.log(pb) / Math.log(2));
        }

        return HX;
    }

    private static void countByte(byte b) {
        if (!bytesCount.containsKey(b)) {
            bytesCount.put(b, 1);
        } else {
            bytesCount.put(b, bytesCount.get(b) + 1);
        }
    }
}