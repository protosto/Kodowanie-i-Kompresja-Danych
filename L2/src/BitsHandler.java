import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BitsHandler {

    // current byte value
    private static byte currByte = 0;
    // amount of bits in current byte
    private static int bitsInCurrByte = 0;
    // file we decode to
    private static FileOutputStream outputStream;
    // file we decode from
    private static byte[] input;
    // current bit
    private static int inputBitIt = 0;

    // initialize output file
    public static void initOutputFile(String path) throws IOException {
        File outputFile = new File(path);
        outputFile.createNewFile();
        outputStream = new FileOutputStream(outputFile);
    }

    // output bit/byte if we already have 8 bits to work with
    public static void outputBit(boolean bit) throws IOException {
        currByte <<= 1;
        currByte += bit ? 1 : 0;
        bitsInCurrByte++;
        if (bitsInCurrByte == 8) {
            outputByte(currByte);
            currByte = 0;
            bitsInCurrByte = 0;
        }

    }

    // finishing byte with pending bits
    public static void finishByte(int low, int pending) throws IOException {
        boolean msbLow = low < 0;
        outputBit(msbLow);
        while (pending > 0) {
            outputBit(!msbLow);
            pending--;
        }
        for (int i = 1; i < 32; i++) outputBit(1 == ((low >> (31 - i)) & 1));
    }

    // outputs byte
    public static void outputByte(byte b) throws IOException {
        outputStream.write(b);
    }

    // gets the path to file we want to decode
    public static byte[] getInput(String path) throws IOException {
        input = Files.readAllBytes(Paths.get(path));
        return input;
    }

    // gets current bit on input
    public static boolean getInputBit() {
        byte b = input[inputBitIt / 8];
        boolean bit = (((b >> (7 - inputBitIt % 8)) & 1) == 1);
        inputBitIt++;
        return bit;
    }

    // used to check if there is anything more we want to read from
    public static boolean isInput() {
        return input.length * 8 > inputBitIt;
    }
}

