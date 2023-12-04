package org.example;

import java.util.ArrayList;
import java.util.List;

public class OmegaCoding implements CodingInterface{

    public int toBinary(int number, List<Boolean> bits){
        int x = number;
        int length = 0;
        while(x >= 1){
            bits.add(0,1==x%2);
            x /= 2;
            length++;
        }
        return length;
    }

    public int toDecimal(int start, int stop, List<Boolean> bits){
        int result = 0;
        for( int i = start; i <= stop; i++){
            if(bits.get(i)) result++;
            result *= 2;
        }
        result/=2;
        return result;
    }
    public List<Boolean> code(int n){
        int k = n;
        List<Boolean>bits = new ArrayList<>();
        bits.add(0, false);
        while (k > 1) {
            k = toBinary(k, bits) - 1;
        }
        return bits;
    }

    public int decode(List<Boolean> bits, Index index){
        int n = 1;
        int var;
        while(bits.get(index.getIndex())){
            var = n;
            n = toDecimal(index.getIndex(), index.getIndex()+n, bits);
            index.addInd(var+1);
        }
        index.incInd();
        return n;
    }


}
