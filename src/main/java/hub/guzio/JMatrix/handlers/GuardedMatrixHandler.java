package hub.guzio.JMatrix.handlers;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.SaneServer.Response;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public abstract class GuardedMatrixHandler extends MatrixHandler {
    private final String credentials;
    public final int expectedPathLength;
    public final String expectedMethod;
    public final Response defaultResponse;

    public GuardedMatrixHandler(AppService appService, int expectedPathLength, String expectedMethod, Response defaultResponse) {
        super(appService.logger);
        credentials = appService.registration.hs_token();
        this.expectedPathLength = expectedPathLength;
        this.expectedMethod = expectedMethod;
        this.defaultResponse = defaultResponse;
    }

    @Override
    protected Response onRequest(HttpExchange rq, URI rawPath, String[] processedPath, String[] queryParameters, String body) throws Throwable {
        var msgBase = "Possible break-in attempt (tho, more likely, just a misconfigured registration.yaml): ";

        if(processedPath.length-1 /*-1 is required because SaneServer counts the initial "/" as a path component (but ignores the final "/" if present), eg. a call to "/_matrix/app/" has 3 components: "", "_matrix" and "app" - users, however, will likely expect that to only be 2 (ie. "_matrix" and "app"), so that -1 is there to match said expectation*/ != expectedPathLength) return UnknownEndpoint.getError(404, rawPath, "");
        if(!Objects.equals(rq.getRequestMethod(), expectedMethod)) return UnknownEndpoint.getError(405, rawPath, ", when called by a "+rq.getRequestMethod()+" request");

        var authHeader = rq.getRequestHeaders().get("Authentication");
        if (Objects.isNull(authHeader) || authHeader.size() != 1) {
            var authHeaderStringified = "<NULL>";
            int length = 0;
            if (!Objects.isNull(authHeader)){
                length = authHeader.size();
                authHeaderStringified = "\""+String.join("\", \"", authHeader)+"\"";
            }
            var msg = "No valid token was provided. Or maybe multiple were, this code doesn't care. The bottom line is that exactly 1 (one) auth token was expected, but that wasn't the case (got "+authHeaderStringified+" (ie. something of length "+length+") instead). Treating this request as unauthenticated.";
            l.wrn(msgBase+msg);
            return new Response(401, "json", "{\"errcode\":\"M_MISSING_TOKEN\",\"error\":\""+msg.replaceAll("\"", "\\\"")+"\"}");
        }

        var authToken = authHeader.getFirst();
        if (!Objects.equals(authToken, credentials)) {
            var msg = "The homeserver gave this appservice an invalid token (got \""+authToken+"\" instead of the expected value).";
            l.wrn(msgBase+msg);
            return new Response(403, "json", "{\"errcode\":\"M_UNKNOWN_TOKEN\",\"error\":\""+msg.replaceAll("\"", "\\\"")+"\"}");
        };

        var output = defaultResponse;
        var result = onRequest(rq, body, processedPath[processedPath.length-1], queryParameters);
        if (result.isPresent()) output = result.get();
        return output;
    }

    protected abstract Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, String[] queryArgs) throws Throwable;
}