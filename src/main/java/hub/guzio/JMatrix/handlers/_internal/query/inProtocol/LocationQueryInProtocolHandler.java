package hub.guzio.JMatrix.handlers._internal.query.inProtocol;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.Protocol;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.JMatrix.handlers._internal.query.UnknownProtocolQueryHandler;
import hub.guzio.SaneServer.Response;

import java.util.Objects;
import java.util.Optional;

public class LocationQueryInProtocolHandler extends GuardedMatrixHandler {
    final Protocol proto;
    final String protoName;

    public LocationQueryInProtocolHandler(AppService appservice, Protocol proto, String protoName){
        super(appservice, 6, "GET", new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"The requested protocol was found, but there were no rooms found as part of said protocol, that would match the specified query.\"}"));
        this.proto = proto;
        this.protoName = protoName;
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable {
        if(!Objects.equals(pathArg, protoName)) return Optional.of(UnknownProtocolQueryHandler.UNKNOWN_PROTOCOL_RESPONSE);
        return proto.onLocationQueryByProtocol(queryArgs);
    }
}