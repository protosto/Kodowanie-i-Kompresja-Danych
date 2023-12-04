package org.example;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Encode {
    public static void encode(byte[] input) throws IOException {
        Dictionary dictionary = new CodingDictionary();
        dictionary.initDictionary();

        List<Byte> sequence = new ArrayList<>();
        sequence.add(input[0]);
        byte next = 0;
        for (int i = 0; i < input.length-1; i++) {
            if (i != input.length - 1) {
                next = input[i + 1];
            }
            List<Byte> combined = new ArrayList<>(sequence.size()+1);
            combined.addAll(sequence);
            combined.add(next);
            if (dictionary.getIndex(combined) != -1) {
                sequence.add(next);
            }
            else {
                BitsHandler.outputIdx(dictionary.getIndex(sequence));
                dictionary.addSequence(combined);
                sequence.clear();
                sequence.add(next);
            }
            next = 0;
        }
        BitsHandler.outputIdx(dictionary.getIndex(sequence));
    }
}