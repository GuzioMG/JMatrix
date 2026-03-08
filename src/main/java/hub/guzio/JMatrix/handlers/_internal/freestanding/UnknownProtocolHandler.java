package hub.guzio.JMatrix.handlers._internal.freestanding;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.Optional;

public class UnknownProtocolHandler extends GuardedMatrixHandler {
    public UnknownProtocolHandler(AppService appservice) {
        super(appservice, 6, "GET", new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"Unknown protocol.\"}"));
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable {
        return Optional.empty();
    }
}
