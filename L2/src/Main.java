import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {


    public static void main(String[] args) throws IOException {

        // file we want to work on
        String path = args[0];
        // file we will code to
        String output = args[1];
        // file we will decode to, note that after decoding is done it should come out identical as the one we initially coded
        String decoded = args[2];

        byte[] file = Files.readAllBytes(Paths.get(path));

        // coding
        BitsHandler.initOutputFile(output);
        Coder.code(file);

        // decoding
        BitsHandler.initOutputFile(decoded);
        BitsHandler.getInput(output);
        Decoder.decode(file.length);
    }

}