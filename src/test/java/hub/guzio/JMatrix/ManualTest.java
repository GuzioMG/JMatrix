package hub.guzio.JMatrix;

import hub.guzio.JMatrix.authProcessors.TestingOrDevelopmentOnlyAuthProcessorUnsuitableForProductionDueToSevereSecurityConsequences;
import hub.guzio.SaneServer.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
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
        var cli = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest rq;

        try { rq = HttpRequest.newBuilder(new URI("https://api.chat.guziohub.ovh/_matrix/client/v1/appservice/javatest/ping"))
                .header("Authorization", "Bearer "+token)
                .POST(HttpRequest.BodyPublishers.ofString("{}")).build();
        } catch (Throwable e){
            System.err.print("Wrong token!"); //This is absolutely not what happens, but it's a nice'n'simple error message.
            cli.close();
            return;
        }

        sv.createContext("/testurl/", new TestEndpoint());
        sv.start();
        var in = new Scanner(System.in);
        System.out.println("Started!");

        while (true){
            try {
                String next = in.next();
                if (Objects.equals(next, "q")) {
                    System.out.print("Exiting...");
                    sv.stop(5);
                    cli.shutdownNow();
                    return;
                } else if (Objects.equals(next, "p")) {
                    HttpResponse<String> body = cli.send(rq, HttpResponse.BodyHandlers.ofString());
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