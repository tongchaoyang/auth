package home.work.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) throws IOException
    {
        if (args.length < 1) {
            System.err.println("Usage: java auth-0.0.1-SNAPSHOT.jar filepath");
            return;
        }
        
        Stream<String> stream = Files.lines(Paths.get(args[0]));
        stream.forEach(e -> {
            Response rsp;
            try {
                rsp = Auth.process(new Request(e));
            } catch(IllegalArgumentException exp) {
                // invalid message
                rsp = new Response(e);
            }
            System.out.println(rsp);
        });
    }
}
