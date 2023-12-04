package org.example;

import java.util.ArrayList;
import java.util.List;

public class Temp {
    public static void main(String[] args) {

        BitsHandler.setCode("delta");
        CodingInterface coding = BitsHandler.getCode();

        List<Boolean> code = coding.code(137);
        List<Boolean> code2 = coding.code(137);
        List<Boolean> code3 = coding.code(137);
        code.addAll(code2);
        code.addAll(code3);
        code.add(false);
        code.add(false);
        code.add(false);
        code.add(false);
        code.add(false);
        code.add(false);

        List<Integer> decode = BitsHandler.translateToDecimal(new ArrayList<>(code));
        System.out.println(decode);

        System.out.println(code);

    }
}
