package org.example;

import java.util.ArrayList;
import java.util.List;

public class DeltaCoding implements CodingInterface{

     public int toBinary(int number, List<Boolean> bits){
        int x = number;
        int length = 0;
        while(x > 1){
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
        result /= 2;
        return result;
    }

    public int toDecimalV2(int start, int stop, List<Boolean> bits){
        int result = 2;
        for( int i = start; i <= stop; i++){
            if(bits.get(i)) result++;
            result *= 2;
        }
        result /= 2;
        return result;
    }
    public List<Boolean> code(int n){
        int k = n;
        List<Boolean>bits = new ArrayList<>();
        CodingInterface throwaway = new GammaCoding();

        k = toBinary(k, bits)+1;
        bits.addAll(0, throwaway.code(k));


        return bits;
    }

    public int decode(List<Boolean> bits, Index index){
        int n = 0;
        int var;
        while(!bits.get(index.getIndex() )){
            n++;
            index.incInd();
            if(index.getIndex() >= bits.size()) return 0;
        }
        var = n;
        n = toDecimal(index.getIndex(), index.getIndex()+n, bits);
        index.addInd(var+1);
        var = n;
        n = toDecimalV2(index.getIndex(), index.getIndex()+n-2, bits);
        index.addInd(var-1);
        return n;
    }

}
