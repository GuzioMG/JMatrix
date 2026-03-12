package hub.guzio.JMatrix;

import hub.guzio.JMatrix.authProcessors.TestingOrDevelopmentOnlyAuthProcessorUnsuitableForProductionDueToSevereSecurityConsequences;
import hub.guzio.SaneServer.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Scanner;

public class ManualTest {
    static void main(String[] args) throws IOException {
        String token;
        AppService appservice;

        if(args.length > 1){
            System.out.println("Expected at most one argument!");
            return;
        } else if (args.length == 0) {
            token = "devnull";
            appservice = new TestedService(token, new TestingOrDevelopmentOnlyAuthProcessorUnsuitableForProductionDueToSevereSecurityConsequences(new Logger()));
        } else {
            token = args[0];
            appservice = new TestedService(token);
        }

        var sv = appservice.serve(new InetSocketAddress(8080));
        HttpRequest.Builder rq;

        try { rq = HttpRequest.newBuilder(new URI("https://api.chat.guziohub.ovh/_matrix/client/v1/appservice/javatest/ping")).POST(HttpRequest.BodyPublishers.ofString("{}")); }
        catch (URISyntaxException e){
            System.err.print("Somehow, this previously-correct URI was wrong?");
            return;
        }

        sv.createContext("/testurl/", new TestEndpoint());
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
                } else if (Objects.equals(next, "p")) {
                    HttpResponse<String> body = appservice.sendAuthenticated(rq);
                    System.out.println("Sent request.");
                    System.out.print(body.body());
                }
            }
            catch (Throwable e) {
                e.printStackTrace(System.err);
            }
        }
    }
}