package hub.guzio.JMatrix.handlers._internal;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.data.Namespace;
import hub.guzio.JMatrix.data.Namespaces;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.Optional;

public class RoomCreateHandler extends GuardedMatrixHandler {
    final AppService as;

    public RoomCreateHandler(AppService appservice){
        super(appservice, 5, "GET", new Response(200, "json", "{}"));
        as = appservice;
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String roomAlias, int pathLength, String[] queryArgs) throws Throwable {
        if (!Namespaces.tryRegex(as.registration.namespaces().aliases(), roomAlias)) return Optional.of(new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"Room did not match this appservice's „aliases:” namespace - assuming it's certainly not gonna be found.\"}"));
        return as.onRoomRequest(roomAlias);
    }
}