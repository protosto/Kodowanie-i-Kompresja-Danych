import java.util.Objects;

public class Tri {
    public static int denominator;
    // lower bound of a range
    int rangeL;
    // upper bound of a range
    int rangeH;

    public Tri(int b, int c){
        this.rangeL = b;
        this.rangeH = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tri tri = (Tri) o;
        return  rangeL == tri.rangeL && rangeH == tri.rangeH;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rangeL, rangeH);
    }
}