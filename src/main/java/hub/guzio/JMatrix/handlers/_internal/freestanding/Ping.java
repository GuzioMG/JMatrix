package hub.guzio.JMatrix.handlers._internal.freestanding;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.Optional;

public class Ping extends GuardedMatrixHandler {
    public Ping(AppService appservice){
        super(appservice, 4, "POST", new Response(200, "json", "{}"));
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, String[] queryArgs) throws Throwable {
        return Optional.empty();
    }
}