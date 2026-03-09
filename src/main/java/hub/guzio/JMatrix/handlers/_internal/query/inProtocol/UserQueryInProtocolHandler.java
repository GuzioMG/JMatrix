package hub.guzio.JMatrix.handlers._internal.query.inProtocol;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.Protocol;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.Optional;

public class UserQueryInProtocolHandler extends GuardedMatrixHandler {
    public UserQueryInProtocolHandler(AppService appservice, Protocol proto){
        super(appservice, 6, "GET", new Response(200, "json", "[]"));
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable {
        return Optional.empty();
    }
}