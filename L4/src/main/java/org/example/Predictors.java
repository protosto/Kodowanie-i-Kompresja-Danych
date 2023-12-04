package org.example;

public class Predictors {

    private static Pixel pred1(int i, int j) {
        Pixel px = new Pixel(TGA.getPixel(i - 1, j));
        return px;
    }

    private static Pixel pred2(int i, int j) {
        Pixel px = new Pixel(TGA.getPixel(i, j - 1));
        return px;
    }

    private static Pixel pred3(int i, int j) {
        Pixel px = new Pixel(TGA.getPixel(i - 1, j - 1));
        return px;
    }

    private static Pixel pred4(int i, int j) {
        Pixel px = new Pixel(TGA.getPixel(i, j - 1));
        Pixel.add(px, TGA.getPixel(i - 1, j));
        Pixel.sub(px, TGA.getPixel(i - 1, j - 1));
        return px;
    }

    private static Pixel pred5(int i, int j) {
        Pixel px = new Pixel(TGA.getPixel(i - 1, j));
        Pixel.sub(px, TGA.getPixel(i - 1, j - 1));
        Pixel.div(px);
        Pixel.add(px, TGA.getPixel(i, j - 1));
        return px;
    }

    private static Pixel pred6(int i, int j) {
        Pixel px = new Pixel(TGA.getPixel(i, j - 1));
        Pixel.sub(px, TGA.getPixel(i - 1, j - 1));
        Pixel.div(px);
        Pixel.add(px, TGA.getPixel(i - 1, j));
        return px;
    }

    private static Pixel pred7(int i, int j) {
        Pixel px = new Pixel(TGA.getPixel(i - 1, j));
        Pixel.add(px, TGA.getPixel(i, j - 1));
        Pixel.div(px);
        return px;
    }

    private static Pixel pred8(int i, int j) {
        Pixel px = new Pixel(0, 0, 0);
        Pixel N = TGA.getPixel(i - 1, j);
        Pixel W = TGA.getPixel(i, j - 1);
        Pixel NW = TGA.getPixel(i - 1, j - 1);

        px.R = pred8_comp(N.R, W.R, NW.R);
        px.G = pred8_comp(N.G, W.G, NW.G);
        px.B = pred8_comp(N.B, W.B, NW.B);

        return px;
    }

    private static int pred8_comp(int N, int W, int NW) {
        int X;
        if(NW >= Math.max(W, N))
            X = Math.max(W, N);
        else {
            if(NW <= Math.min(W, N))
                X = Math.min(W, N);
            else
                X = (W + N - NW);
        }
        return X;
    }

    public static Pixel predict(int mode, int i, int j) {
        switch(mode) {
            case 1 -> {
                return pred1(i, j);
            }
            case 2 -> {
                return pred2(i, j);
            }
            case 3 -> {
                return pred3(i, j);
            }
            case 4 -> {
                return pred4(i, j);
            }
            case 5 -> {
                return pred5(i, j);
            }
            case 6 -> {
                return pred6(i, j);
            }
            case 7 -> {
                return pred7(i, j);
            }
            case 8 -> {
                return pred8(i, j);
            }
        }
        return TGA.getPixel(-1, -1);
    }
}