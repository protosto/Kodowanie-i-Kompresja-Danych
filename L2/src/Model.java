import java.lang.reflect.Array;

public class Model {

    // counter of every byte
    private final int[] bytesCount = new int[256];
    // ranges of every byte
    private final Tri[] bytesRanges = new Tri[256];
    // current number of bytes+256
    private int numberOfBytes = 256;

    // initialzing []bytesCount
    private void initBytesCount() {
        for (int i = 0; i < 256; i++) bytesCount[i] = 1;
    }
    // initializing Model, we assume that every byte has equal probability at the beginning
    public Model() {
        initBytesCount();
        for (int i = 0; i < 256; i++) bytesRanges[i] = new Tri(i,i+1);
        updateRanges();
    }
    // accesses probability of a certain byte
    public Tri getProbability(byte b) {
        int idx = (int) b + 128;
        return bytesRanges[idx];
    }

    // increments byte counter and numberofBytes
    public void countByte(byte b) {
        int idx = (int) b + 128;
        bytesCount[idx]++;
        numberOfBytes++;
    }
    // updates ranges of every byte, this happens every 128 bytes in both Code in Decoder, however this number is mostly arbitrary
    public void updateRanges() {
        Tri.denominator = numberOfBytes;
        int it = 0;
        int byteNr = 0;
        for (Tri p : bytesRanges) {
            p.rangeL = it;
            p.rangeH = it + bytesCount[byteNr];
            it += bytesCount[byteNr];
            byteNr++;
        }
    }

    // gets the number of bytes
    public int getNumberOfBytes() {
        return numberOfBytes;
    }

    // gets a byte
    public byte getByte(int z) {
        for (int i = 0; i < 256; i++) {
            if (z >= bytesRanges[i].rangeL && z < bytesRanges[i].rangeH) return (byte) (i - 128);
        }
        return -1;
    }
}
