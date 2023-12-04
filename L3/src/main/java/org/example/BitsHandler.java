package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    private static CodingInterface code = new OmegaCoding();

    private static FileOutputStream outputCodedStream;
    private static FileOutputStream outputDecodedStream;

    // initialize output file
    public static void initCodedOutputFile(String path) throws IOException {
        File outputFile = new File(path);
        outputFile.createNewFile();
        outputCodedStream = new FileOutputStream(outputFile);
    }

    public static byte[] getCodingInput(String path) throws IOException {
        input = Files.readAllBytes(Paths.get(path));
        return input;
    }

    public static void initDecodedOutputFile(String path) throws IOException {
        File outputFile = new File(path);
        outputFile.createNewFile();
        outputDecodedStream = new FileOutputStream(outputFile);
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

    // finishing byte
    public static void finishByte() throws IOException {
        currByte <<= (8 - bitsInCurrByte);
        outputByte(currByte);
    }

    // outputs byte
    public static void outputByte(byte b) throws IOException {
        outputCodedStream.write(b);
    }

    public static void setCode(String codeName) {
        switch (codeName) {
            case "delta" -> code = new DeltaCoding();
            case "gamma" -> code = new GammaCoding();
            case "fib" -> {
                code = new FibonacciCoding();
                FibonacciCoding.initializeFibo(40);
            }
        }
    }


    // gets the path to file we want to decode
    public static byte[] getInput(String path) throws IOException {
        input = Files.readAllBytes(Paths.get(path));
        return input;
    }

    public static ArrayList<Boolean> getDecodingInput(String path) throws IOException {
        input = Files.readAllBytes(Paths.get(path));
        ArrayList<Boolean> bits = new ArrayList<>(8 * input.length);
        for (byte value : input) {
            for (int i = 0; i < 8; i++) {
                bits.add(((value >> (7 - i)) & 1) == 1);
            }
        }
        return bits;
    }

    public static ArrayList<Integer> translateToDecimal(ArrayList<Boolean> bits){
        Index i = new Index();
        ArrayList<Integer> decArray = new ArrayList<>();
        while(i.getIndex() < bits.size()){
            decArray.add(code.decode(bits, i));
        }
        return decArray;
    }
    public static void outputIdx(int idx) throws IOException {
        List<Boolean> bits = code.code(idx);
        for (boolean bit : bits) {
            outputBit(bit);
        }
    }

    public static void outputSeq(List<Byte> seq) throws IOException {
        byte[] bytes = new byte[seq.size()];
        for (int i = 0; i < seq.size(); i++) bytes[i] = seq.get(i);
        outputDecodedStream.write(bytes);
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



    public static void reset() {
        currByte = 0;
        bitsInCurrByte = 0;
    }

    public static CodingInterface getCode(){
        return code;
    }

}

