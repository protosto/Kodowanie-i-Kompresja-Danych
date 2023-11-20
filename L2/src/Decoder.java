import java.io.IOException;

import static java.lang.Integer.compareUnsigned;
import static java.lang.Integer.toUnsignedLong;

public class Decoder {
    public static void decode(int byteSize) throws IOException {
    Model model = new Model();

    int high = 0xFFFFFFFF;
    int low = 0;
    long value = 0;
    byte b;

    // getting the initial 32 bits
    for (int i = 0; i < 32; i++) {
        value <<= 1;
        value += BitsHandler.getInputBit() ? 1 : 0;
    }

    while (model.getNumberOfBytes() - 256 < byteSize || BitsHandler.isInput()) {

        long range = toUnsignedLong(high) - toUnsignedLong(low) + 1;
        int byteRange = (int) (((toUnsignedLong((int) value) - toUnsignedLong(low) + 1) * Tri.denominator - 1) / range);
        b = model.getByte(byteRange);
        model.countByte(b);

        Tri p = model.getProbability(b);
        BitsHandler.outputByte(b);
        high = (int) (low + (range * p.rangeH) / (Tri.denominator) - 1);
        low = (int) (low + (range * p.rangeL) / (Tri.denominator));
        while (BitsHandler.isInput()) {
            if (!(compareUnsigned(low, 0x80000000) < 0) || compareUnsigned(high, 0x80000000) < 0) {
                low <<= 1;
                high <<= 1;
                high |= 1;
                value <<= 1;
                value += BitsHandler.getInputBit() ? 1 : 0;
            } else if (!(compareUnsigned(low, 0x40000000) < 0) && compareUnsigned(high, 0xC0000000) < 0) {
                low <<= 1;
                low &= 0x7FFFFFFF;
                high <<= 1;
                high |= 0x80000001;
                value -= 0x40000000;
                value <<= 1;
                value += BitsHandler.getInputBit() ? 1 : 0;

            } else {
                break;
            }
        }
        // updating the ranges every 128(mostly arbitrary number) bytes
        if (model.getNumberOfBytes() % 128 == 0) {
            model.updateRanges();
        }

    }
}
}
