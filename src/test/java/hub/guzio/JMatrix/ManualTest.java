package hub.guzio.JMatrix;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

public class ManualTest {
    static void main(String[] args) throws IOException, URISyntaxException {
        var appservice = new TestedService();

        if (args.length == 3) appservice = new TestedService(args[0], args[1], args[2]);
        else if (args.length != 0) {
            System.out.println("Expected exactly 3 arguments!");
            return;
        }

        var sv = appservice.serve(new InetSocketAddress(8080));
        sv.start();

        var in = new Scanner(System.in);

        System.out.println("Started with YAML:\n"+appservice.registration.toString());
        while (true){
            try {
                String next = in.next();
                if (Objects.equals(next, "q")) {
                    System.out.print("Exiting...");
                    appservice.close();
                    return;
                }
                else if (Objects.equals(next, "p")) appservice.ping();
            }
            catch (Throwable e) {
                e.printStackTrace(System.err);
            }
        }
    }
}