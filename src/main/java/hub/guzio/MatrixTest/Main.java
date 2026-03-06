package hub.guzio.MatrixTest;

import com.sun.net.httpserver.HttpServer;
import hub.guzio.MatrixTest.cannedHandlers.AliasEndpoint;
import hub.guzio.MatrixTest.cannedHandlers.MinecraftProtocol;
import hub.guzio.MatrixTest.cannedHandlers.UnknownEndpoint;
import hub.guzio.MatrixTest.cannedHandlers.UnknownProtocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static void main() throws IOException {
        var sv = HttpServer.create(new InetSocketAddress(8080), 1000);
        var in = new Scanner(System.in);
        System.out.print("Enter AppService Token: ");
        var cli = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest rq;

        try { rq = HttpRequest.newBuilder(new URI("https://api.chat.guziohub.ovh/_matrix/client/v1/appservice/javatest/ping"))
                .header("Authorization", "Bearer "+in.next())
                .POST(HttpRequest.BodyPublishers.ofString("{}")).build();
        } catch (Throwable e){
            System.err.print("Wrong token!"); //This is absolutely not what happens, but it's a nice'n'simple error message.
            cli.close();
            return;
        }

        //Core endpoints
        sv.createContext("/_matrix/app/v1/transactions/", new MainHandler());
        sv.createContext("/_matrix/app/v1/ping", new MainHandler());
        sv.createContext("/_matrix/app/v1/users/", new MainHandler());

        //Protocol endpoints
        sv.createContext("/_matrix/app/v1/thirdparty/protocol/minecraft", new MinecraftProtocol());
        sv.createContext("/_matrix/app/v1/thirdparty/user", new UserlistHandler());

        //Unknown endpoints
        sv.createContext("/_matrix/app/v1/rooms/", new AliasEndpoint());
        sv.createContext("/_matrix/app/v1/thirdparty/location", new AliasEndpoint());
        sv.createContext("/_matrix/app/v1/thirdparty/protocol/", new UnknownProtocol());
        sv.createContext("/", new UnknownEndpoint());

        sv.start();
        System.out.println("Started!");
        while (true){
            try {
                if (Objects.equals(in.next(), "q")) {
                    System.out.print("Exiting...");
                    sv.stop(5);
                    cli.shutdownNow();
                    return;
                }
                HttpResponse<String> body = cli.send(rq, HttpResponse.BodyHandlers.ofString());
                System.out.println("Sent request.");
                System.out.print(body.body());
            }
            catch (Throwable e) {
                e.printStackTrace(System.err);
            }
        }
    }
}