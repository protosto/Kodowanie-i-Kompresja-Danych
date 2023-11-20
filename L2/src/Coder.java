import java.io.IOException;

import static java.lang.Integer.compareUnsigned;
import static java.lang.Integer.toUnsignedLong;

public class Coder {


        public static void code(byte[] file) throws IOException {

            Model model = new Model();
            int high = 0xFFFFFFFF;
            int low = 0;

            int storedBits = 0;
            int byteIt = 0;
            byte b;

            while (byteIt < file.length) {
                b = file[byteIt];
                model.countByte(b);

                long range = toUnsignedLong(high) - toUnsignedLong(low) + 1;
                Tri p = model.getProbability(b);
                high = (int) (low + (range * p.rangeH) / Tri.denominator) - 1;
                low = (int) (low + (range * p.rangeL) / Tri.denominator);

                while (true) {

                    if (compareUnsigned(high, 0x80000000) < 0) { // MSB of high = 0, we can already output it
                        outputBitAndStoredBits(false, storedBits);
                        storedBits = 0;
                        low <<= 1;
                        high <<= 1;
                        high |= 1;

                    } else if (!(compareUnsigned(low, 0x80000000) < 0)) { // MSB of low = 0, ready to output
                        outputBitAndStoredBits(true, storedBits);
                        storedBits = 0;
                        low <<= 1;
                        high <<= 1;
                        high |= 1;

                    } else if (!(compareUnsigned(low, 0x40000000) < 0) && compareUnsigned(high, 0xC0000000) < 0) { // low 01, high 10 time to store some bits
                        storedBits++;
                        low <<= 1;
                        low &= 0x7FFFFFFF;
                        high <<= 1;
                        high |= 0x80000001;
                    } else
                        break;

                }
                byteIt++;
                // updating the ranges every 128(mostly arbitrary number) bytes
                if ((byteIt) % 128 == 0) {
                    model.updateRanges();
                }
            }
            BitsHandler.finishByte(low, storedBits);


        }
    // outputting stored bits
    static void outputBitAndStoredBits(boolean bit, int storedBits) throws IOException {
        BitsHandler.outputBit(bit);
        while (storedBits > 0) {
            storedBits--;
            BitsHandler.outputBit(!bit);
        }
    }
}
