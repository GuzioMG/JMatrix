package hub.guzio.JMatrix.handlers._internal.protocolQuery;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.Optional;

public class ProtocolQueryHandler extends GuardedMatrixHandler {
    public ProtocolQueryHandler(AppService appservice){
        super(appservice, 6, "GET", new Response(500, "json", "{\"errcode\":\"M_UNKNOWN\",\"error\":\"If you're seeing this error message, something went very wrong with the JMatrix library.\"}")); //This class's request handler should never return Optional.empty() (I mean... It does so now, but that's just a placeholder atm.)
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, String[] queryArgs) throws Throwable {
        return Optional.empty();
    }
}