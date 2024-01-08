package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static java.nio.file.StandardOpenOption.APPEND;

public class TGA {

    public static int height;
    public static int width;

    public static Pixel[][] img;
    private static byte[] file;

    // TGA format
    private static final  int headerSize = 18;
    private static final int[] widthBytes = {12, 13};
    private static final int[] heightBytes = {14, 15};
    private static byte[] header;
    private static byte[] footer;
    public static void init(String path) throws IOException {
        file = Files.readAllBytes(Paths.get(path));
        width = (unsignedByte(file[widthBytes[1]]) << 8) | unsignedByte(file[widthBytes[0]]);
        height = (unsignedByte(file[heightBytes[1]]) << 8) | unsignedByte(file[heightBytes[0]]);
        inputImage();

        header = Arrays.copyOfRange(file, 0, headerSize);
        footer = Arrays.copyOfRange(file, headerSize + width*height*3, file.length);
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


    public static void saveTga(String path) throws IOException {

        byte[] newImgBytes = new byte[height*width*3];
        int it = 0;

        for(Pixel[] pxrow : img) {
            for(Pixel px : pxrow){
                newImgBytes[it++] = (byte)px.getB();
                newImgBytes[it++] = (byte)px.getG();
                newImgBytes[it++] = (byte)px.getR();

            }
        }

        Files.write(Path.of(path), header);
        Files.write(Path.of(path), newImgBytes, APPEND);
        Files.write(Path.of(path), footer, APPEND);
    }

}
