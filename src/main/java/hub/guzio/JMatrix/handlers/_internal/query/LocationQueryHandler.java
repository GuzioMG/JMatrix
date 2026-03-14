package hub.guzio.JMatrix.handlers._internal.query;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.Protocol;
import hub.guzio.JMatrix._internal.Utils;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.*;

public class LocationQueryHandler extends GuardedMatrixHandler {
    final Collection<Protocol> protocols;

    public LocationQueryHandler(AppService appservice){
        super(appservice, 5, 6, "GET", new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"Found no matching location(s) in any available protocol.\"}"));
        protocols = appservice.registration.protocols().orElse(Map.of()).values();
    }

    @Override
    protected boolean pathIsReallyValid(String pathArg, int pathLength){
        return !(pathLength == 5 && !Objects.equals(pathArg, "location"));
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable {
        if (pathLength == 6) return Optional.of(UnknownProtocolQueryHandler.UNKNOWN_PROTOCOL_RESPONSE);
        for(var proto : protocols) {
            var thisWillBeUsedAtSomePointForCompletingThatTodo = proto.onLocationQueryByAlias(Utils.extractArg(queryArgs, "alias"));
        }
        return Optional.empty(); //TODO
    }
}