package org.example;

import java.util.ArrayList;
import java.util.List;

public class FibonacciCoding implements CodingInterface{

    private static final List<Integer> fiboNumbers = new ArrayList<>();

    public static void initializeFibo(int n){
        fiboNumbers.add(1);
        fiboNumbers.add(2);
        for( int i = 2; i < n; i++){
            fiboNumbers.add(fiboNumbers.get(i-1)+fiboNumbers.get(i-2));
        }
    }

    static int findMaxFib(int n){
        int i = 0;

        while( n >= fiboNumbers.get(i) ) i++;

        return i-1;
    }
    public int toDecimal(int start, int stop, List<Boolean> bits){
        int result = 0;
        for( int i = start; i <= stop; i++){
            if(bits.get(i)) result++;
            result *= 2;
        }
        return result;
    }

    @Override
    public int toBinary(int n, List<Boolean> bits) {
        return 0;
    }

    public List<Boolean> code(int n){
        List<Boolean>bits = new ArrayList<>();
        int x = n;
        int index = findMaxFib(x);
        bits.add(0, true);

        while(index >= 0){
            if( x >= fiboNumbers.get(index) ){
                x -= fiboNumbers.get(index);
                bits.add(0, true);
            }else{
                bits.add(0, false);
            }
            index--;
        }
        return bits;
    }

    public int decode(List<Boolean> bits, Index index){
        int result = 0;
        int i = 0;

        while( !(bits.get(index.getIndex()) && bits.get(index.getIndex()+1)) ){
            if(bits.get(index.getIndex())) result += fiboNumbers.get(i);
            i++;
            index.incInd();
            if(index.getIndex() >= bits.size()) return 0;
        }
        index.addInd(2);
        return result+fiboNumbers.get(i);
    }


}
