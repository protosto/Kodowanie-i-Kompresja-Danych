package org.example;

import java.util.List;

interface CodingInterface {
    int toDecimal(int start, int stop, List<Boolean> bits);

    int toBinary(int n, List<Boolean> bits);

    List<Boolean> code(int n);

    int decode(List<Boolean> bits, Index index);
}
