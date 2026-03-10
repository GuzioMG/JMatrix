package hub.guzio.JMatrix.handlers._internal.query;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Response;

import java.util.Objects;
import java.util.Optional;

public class LocationQueryHandler extends GuardedMatrixHandler {
    public LocationQueryHandler(AppService appservice){
        super(appservice, 5, 6, "GET", new Response(200, "json", "[]"));
    }

    @Override
    protected boolean pathIsReallyValid(String pathArg, int pathLength){
        return !(pathLength == 5 && !Objects.equals(pathArg, "location"));
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable {
        if (pathLength == 6) return Optional.of(new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"Unknown protocol.\"}"));
        return Optional.empty(); //TODO
    }
}