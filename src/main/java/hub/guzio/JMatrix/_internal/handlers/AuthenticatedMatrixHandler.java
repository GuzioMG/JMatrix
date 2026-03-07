package hub.guzio.JMatrix._internal.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.MatrixHandler;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

public abstract class AuthenticatedMatrixHandler extends MatrixHandler {
    private final String credentials;

    public AuthenticatedMatrixHandler(AppService appService) {
        super(appService.logger);
        credentials = appService.registration.hs_token();
    }

    @Override
    protected Response onRequest(HttpExchange rq, Headers resp, URI rawPath, String[] processedPath, String[] queryParameters) throws Throwable {
        var authHeader = rq.getRequestHeaders().get("Authentication");
        if (Objects.isNull(authHeader) || authHeader.size() != 1) return new Response(401, "json", "{\"errcode\":\"M_MISSING_TOKEN\",\"error\":\"No valid token was provided. Or maybe multiple were, this code doesn't care. The bottom line is that exactly 1 (one) auth token was expected, but that wasn't the case. Treating this request as unauthenticated.\"}");

        var authToken = authHeader.getFirst();
        if (!Objects.equals(authToken, credentials)) return new Response(403, "json", "{\"errcode\":\"M_UNKNOWN_TOKEN\",\"error\":\"The homeserver gave a wrong token.\"}");;


        return null;
    }

    protected abstract Response onRequest(HttpExchange rq, URI path, Headers resp, String pathArg, Map<String, String> queryArgs);
}
