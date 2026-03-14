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
    final String protoName;

    public ProtocolQueryHandler(AppService appservice, Protocol proto, String protoName){
        super(appservice, 6, "GET", new Response(500, "json", "{\"errcode\":\"M_UNKNOWN\",\"error\":\"If you're seeing this error message, something went very wrong with the JMatrix library.\"}")); //This class's request handler should never return Optional.empty()
        this.proto = proto;
        this.protoName = protoName;
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable {
        if(!Objects.equals(pathArg, protoName)) return Optional.of(UnknownProtocolQueryHandler.UNKNOWN_PROTOCOL_RESPONSE);
        return Optional.of(new Response(200, "json", proto.toString()));
    }
}