package org.example;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;

public class ColorQuantization {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java ColorQuantization input.tga output.tga num_colors");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args[1];
        int numColors = Integer.parseInt(args[2]);

        if( numColors < 0 || numColors > 24 ){
            System.out.println("MASZ ZLA LICZBE KOLOROW");
        }

        numColors = (int)Math.pow(2, numColors);

        // Load the original image using TGA class
        TGA.init(inputPath);

        // Apply vector quantization
        Pixel[][] initial = quantization(TGA.img, numColors);

        // Save the quantized image
        TGA.saveTga(outputPath);

        // Calculate metrics
        double mse = calculateMSE(initial, TGA.img);
        double snr = calculateSNR(initial, TGA.img);

        System.out.println("Mean Squared Error: " + mse);
        System.out.println("Signal-to-Noise Ratio: " + snr + " dB");
    }

    private static Pixel[][] quantization(Pixel[][] img, int numColors) {
        int height = img.length;
        int width = img[0].length;
        Pixel[][] initial = Arrays.stream(img).map(Pixel[]::clone).toArray(Pixel[][]::new);

        // Convert pixels to colors
        Color[] colors = new Color[height * width];
        int index = 0;
        for (Pixel[] pixels : img) {
            for (int j = 0; j < width; j++) {
                colors[index++] = new Color(pixels[j].getR(), pixels[j].getG(), pixels[j].getB());
            }
        }

        // Create a color palette using the Linde-Buzo-Gray algorithm
        Color[] colorPalette = generateColorPalette(colors, numColors);

        // Apply color quantization
        for (int i = 0; i < colors.length; i++) {
            colors[i] = findClosestColor(colors[i], colorPalette);
        }

        // Save the modified pixels back to the image
        index = 0;
        for (int i = height-1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                img[i][j] = new Pixel(colors[index].getRed(), colors[index].getGreen(), colors[index].getBlue());
                index++;
            }
        }
        return initial;
    }

    private static Color[] generateColorPalette(Color[] colors, int numColors) {
        List<Color> colorList = Arrays.asList(colors);
        List<Color> uniqueColors = new ArrayList<>(new HashSet<>(colorList));

        if (uniqueColors.size() <= numColors) {
            return uniqueColors.toArray(new Color[0]);
        }

        // Algorytm Lindego-Buza-Graya z metodą podziałów

        // Inicjalizacja punktu startowego
        Color[] centroids = new Color[]{computeCentroid(uniqueColors)};
        double distortion = computeDistortion(uniqueColors, centroids);

        while (centroids.length < numColors) {
            // Podział punktów
            List<List<Color>> partitions = splitPoints(uniqueColors, centroids);

            // Aktualizacja centroidów
            centroids = new Color[2 * centroids.length];
            int index = 0;

            for (List<Color> partition : partitions) {
                if(!partition.isEmpty()) {
                    centroids[index++] = computeCentroid(partition);
                }
            }
            int centroidsLen = index;
            for(; index < centroidsLen*2; index++){
                if( centroids.length < numColors){
                    centroids[index] = new Color((int) ((centroids[index-centroidsLen].getRed()+epsilon())%255), (int) ((centroids[index-centroidsLen].getGreen()+epsilon())%255), (int) ((centroids[index-centroidsLen].getBlue()+epsilon())%255));
                }
            }

        }

        return centroids;
    }

    private static List<List<Color>> splitPoints(List<Color> points, Color[] centroids) {
        int k = centroids.length;
        List<List<Color>> partitions = new ArrayList<>(k);

        for (int i = 0; i < k; i++) {
            partitions.add(new ArrayList<>());
        }

        for (Color point : points) {
            int closestCentroidIndex = findClosestCentroid(point, centroids);
            partitions.get(closestCentroidIndex).add(point);
        }

        return partitions;
    }

    private static Color computeCentroid(List<Color> points) {
        if (points.isEmpty()) {
            return null;
        }

        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;

        for (Color point : points) {
            totalRed += point.getRed();
            totalGreen += point.getGreen();
            totalBlue += point.getBlue();
        }

        int averageRed = totalRed / points.size();
        int averageGreen = totalGreen / points.size();
        int averageBlue = totalBlue / points.size();

        return new Color(averageRed, averageGreen, averageBlue);
    }

    private static double computeDistortion(List<Color> points, Color[] centroids) {
        double distortion = 0;

        for (Color point : points) {
            int closestCentroidIndex = findClosestCentroid(point, centroids);
            Color closestCentroid = centroids[closestCentroidIndex];
            distortion += colorDistance(point, closestCentroid);
        }

        return distortion;
    }

    private static int findClosestCentroid(Color targetColor, Color[] centroids) {
        int closestCentroidIndex = 0;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < centroids.length; i++) {
            if(centroids[i] == null) break;
            int distance = colorDistance(targetColor, centroids[i]);
            if (distance < minDistance) {
                minDistance = distance;
                closestCentroidIndex = i;
            }
        }

        return closestCentroidIndex;
    }

    private static Color mergeColors(Color color1, Color color2) {
        int red = (color1.getRed() + color2.getRed()) / 2;
        int green = (color1.getGreen() + color2.getGreen()) / 2;
        int blue = (color1.getBlue() + color2.getBlue()) / 2;

        return new Color(red, green, blue);
    }

    private static Color findClosestColor(Color targetColor, Color[] colorPalette) {
        // Find the closest color in the palette using Manhattan distance
        Color closestColor = colorPalette[0];
        int minDistance = colorDistance(targetColor, closestColor);

        for (int i = 1; i < colorPalette.length; i++) {
            if(colorPalette[i] == null) break;
            int distance = colorDistance(targetColor, colorPalette[i]);
            if (distance < minDistance) {
                minDistance = distance;
                closestColor = colorPalette[i];
            }
        }

        return closestColor;
    }

    private static int colorDistance(Color color1, Color color2) {
        return Math.abs(color1.getRed() - color2.getRed()) +
                Math.abs(color1.getGreen() - color2.getGreen()) +
                Math.abs(color1.getBlue() - color2.getBlue());
    }

    private static int pixelDistance(Pixel px1, Pixel px2) {
        return Math.abs(px1.getR() - px2.getR()) +
                Math.abs(px1.getG() - px2.getG()) +
                Math.abs(px1.getB() - px2.getB());
    }
    private static double calculateMSE(Pixel[][] originalImage, Pixel[][] quantizedImage) {
        int width = originalImage[0].length;
        int height = originalImage.length;
        double mse = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mse += Math.pow(pixelDistance(originalImage[y][x], quantizedImage[height-y-1][x]), 2);
            }
        }

        return mse / (width * height);
    }

    private static double calculateSNR(Pixel[][] originalImage, Pixel[][] quantizedImage) {
        double mse = calculateMSE(originalImage, quantizedImage);
        int width = originalImage[0].length;
        int height = originalImage.length;

        if (mse == 0) {
            return Double.POSITIVE_INFINITY;
        }

        double originalEnergy = calculateEnergy(originalImage);
        return originalEnergy/mse/width/height;
    }

    private static double calculateEnergy(Pixel[][] image) {
        int width = image[0].length;
        int height = image.length;
        double energy = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                energy += Math.pow(image[y][x].getR()+image[y][x].getG()+image[y][x].getB(), 2);

            }
        }

        return energy;
    }

    private static int epsilon(){
        return (int)(Math.random()*5+7);
    }
}
