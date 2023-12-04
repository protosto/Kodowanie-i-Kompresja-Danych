package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TGA {

    public static int height;
    public static int width;

    public static Pixel[][] img;
    private static byte[] file;

    // TGA format
    private static final  int headerSize = 18;
    private static final int[] widthBytes = {12, 13};
    private static final int[] heightBytes = {14, 15};

    public static void init(String path) throws IOException {
        file = Files.readAllBytes(Paths.get(path));
        width = (unsignedByte(file[widthBytes[1]]) << 8) | unsignedByte(file[widthBytes[0]]);
        height = (unsignedByte(file[heightBytes[1]]) << 8) | unsignedByte(file[heightBytes[0]]);
        inputImage();
    }

    public static void inputImage() {
        img = new Pixel[height][width];
        int fileIt = headerSize;
        int r, g, b;
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++) {
                b = unsignedByte(file[fileIt++]);
                g = unsignedByte(file[fileIt++]);
                r = unsignedByte(file[fileIt++]);
                img[height - i - 1][j] = new Pixel(r, g, b);
            }
    }

    public static Pixel getPixel(int i, int j) {
        if(i < 0 || j < 0 || i > height || j > width) return new Pixel(0, 0, 0);
        else return img[i][j];
    }

    private static int unsignedByte(byte b) {
        return b & 0xFF;
    }

}
