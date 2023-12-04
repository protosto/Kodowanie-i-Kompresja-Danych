package org.example;

import java.util.ArrayList;
import java.util.List;

public class DecodingDictionary implements Dictionary {
    private final List<List<Byte>> dictionary = new ArrayList<>(258);

    @Override
    public void initDictionary() {
        List<Byte> list;
        list = new ArrayList<>(1);
        list.add(0, null);
        dictionary.add(list);
        dictionary.add(list);
        for (int i = 2; i <= 257; i++) {
            list = new ArrayList<>(1);
            list.add(0, (byte) (i - 2));
            dictionary.add(list);
        }
    }

    @Override
    public void addSequence(List<Byte> bytes) {
        dictionary.add(bytes);
    }


    @Override
    public int getIndex(List<Byte> sequence) {
        return dictionary.indexOf(sequence);
    }


    @Override
    public List<Byte> getSequenceAt(int index) {
        if (index < 0 || index >= dictionary.size()) return null;
        return dictionary.get(index);
    }
}