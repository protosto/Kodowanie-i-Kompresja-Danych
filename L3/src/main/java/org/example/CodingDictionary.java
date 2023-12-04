package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CodingDictionary implements Dictionary {
    private final HashMap<List<Byte>, Integer> dictionary = new HashMap<>(258);
    public int index = 258;

    @Override
    public void initDictionary() {
        List<Byte> list;
        list = new ArrayList<>(1);
        list.add(0, null);
        dictionary.put(list, 0);
        dictionary.put(list, 1);
        for (int i = 2; i <= 257; i++) {
            list = new ArrayList<>(1);
            list.add(0, (byte) (i - 2));
            dictionary.put(list, i);
        }
    }

    @Override
    public void addSequence(List<Byte> sequence) {
        dictionary.put(sequence, index);
        index++;
    }

    @Override
    public int getIndex(List<Byte> sequence) {
        return (dictionary.get(sequence) != null) ? dictionary.get(sequence) : -1;
    }

    @Override
    public List<Byte> getSequenceAt(int index) {
        for (List<Byte> l : dictionary.keySet())
            if (dictionary.get(l) == index)
                return l;
        return null;
    }
}
