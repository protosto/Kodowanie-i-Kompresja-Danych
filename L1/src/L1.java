import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class L1 {
    public static void main(String[] args) throws IOException {
        String path = args[0];

        byte[] file = Files.readAllBytes(Paths.get(path));

        Map<Byte, Integer> symbol = new HashMap<>();
        Map<Pair, Integer> pairs = new HashMap<>();
        byte prevByte = 0;
        Pair currPair;

        for (byte b : file) {
            if (symbol.containsKey(b)) {
                symbol.put(b, symbol.get(b) + 1);
            } else {
                symbol.put(b, 1);
            }

            currPair = new Pair(prevByte, b);
            prevByte = b;

            if (pairs.containsKey(currPair)) {
                pairs.put(currPair, pairs.get(currPair) + 1);
            } else {
                pairs.put(currPair, 1);
            }
        }

        double HX = 0;
        double prob;
        int sum = symbol.values().stream().mapToInt(i -> i).sum();

        for (int b : symbol.values()) {
            prob = ((double)b / sum);
            HX -= prob * (Math.log(prob) / Math.log(2));
        }

        double HYX = 0;


        for(Pair b: pairs.keySet()) {
            double probx = ((double)symbol.get(b.second)/file.length);
            double probxy = ((double)pairs.get(b)/file.length);
            HYX -= probxy*(Math.log(probxy/probx)/Math.log(2));
        }

        System.out.println("Entropy, H(X) = " + HX);
        System.out.println("Conditional entropy, H(Y|X) = " + HYX);
    }

}


