package org.example;

import java.io.IOException;

import static org.example.Entropy.calculateEntropy;

public class Main {

    private static void printEntropy(Pixel[][] img) {
        System.out.printf("all = %.6f", calculateEntropy(img, "all"));
        System.out.printf("\t| red = %.6f", calculateEntropy(img, "red"));
        System.out.printf("\t| green = %.6f", calculateEntropy(img, "green"));
        System.out.printf("\t| blue = %.6f\n", calculateEntropy(img, "blue"));
    }

    public static void main(String[] args) throws IOException {

        String path = args[0];

        TGA.init(path);

        printEntropy(TGA.img);
        System.out.println("---------------------------------------------------------------");


        Pixel[][] predictedImg = new Pixel[TGA.height][TGA.width];
        for (int pred = 1; pred <= 8; pred++) {
            for (int i = 0; i < TGA.height; i++)
                for (int j = 0; j < TGA.width; j++) {
                    predictedImg[i][j] = new Pixel(0, 0, 0);
                    Pixel.add(predictedImg[i][j], TGA.img[i][j]);
                    Pixel.sub(predictedImg[i][j], Predictors.predict(pred, i, j));
                }
            printEntropy(predictedImg);
        }



    }
}
