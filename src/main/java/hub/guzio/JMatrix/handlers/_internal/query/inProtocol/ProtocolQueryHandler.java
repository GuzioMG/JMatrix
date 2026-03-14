package hub.guzio.JMatrix.handlers._internal.query.inProtocol;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.Protocol;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.JMatrix.handlers._internal.query.UnknownProtocolQueryHandler;
import hub.guzio.SaneServer.Response;

import java.util.Objects;
import java.util.Optional;

public class ProtocolQueryHandler extends GuardedMatrixHandler {
    final Protocol proto;

    public ProtocolQueryHandler(AppService appservice, Protocol proto){
        super(appservice, 6, "GET", new Response(500, "json", "{\"errcode\":\"M_UNKNOWN\",\"error\":\"If you're seeing this error message, something went very wrong with the JMatrix library.\"}")); //This class's request handler should never return Optional.empty()
        this.proto = proto;
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) {
        if(!Objects.equals(pathArg, proto.name)) return Optional.of(UnknownProtocolQueryHandler.UNKNOWN_PROTOCOL_RESPONSE);
        return Optional.of(new Response(200, "json", proto.toString()));
    }
}