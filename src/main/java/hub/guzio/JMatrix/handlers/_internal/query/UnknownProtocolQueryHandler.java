package hub.guzio.JMatrix.handlers._internal.query;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.Objects;
import java.util.Optional;

public class UnknownProtocolQueryHandler extends GuardedMatrixHandler {
    public final static Response UNKNOWN_PROTOCOL_RESPONSE = new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"Unknown protocol.\"}");

    public UnknownProtocolQueryHandler(AppService appservice){
        super(appservice, 6, "GET", UNKNOWN_PROTOCOL_RESPONSE);
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable {
        return Optional.empty();
    }
}