package org.example;

import java.util.List;

public interface Dictionary {
    void initDictionary();

    void addSequence(List<Byte> sequence);

    int getIndex(List<Byte> sequence);

    List<Byte> getSequenceAt(int index);
}
