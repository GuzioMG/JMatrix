package hub.guzio.JMatrix.handlers;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.AuthProcessor;
import hub.guzio.SaneServer.Response;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public abstract class GuardedMatrixHandler extends MatrixHandler {
    private final AuthProcessor auth;
    public final int minPathLength;
    public final int maxPathLength;
    public final String expectedMethod;
    public final Response defaultResponse;

    public GuardedMatrixHandler(AppService appService, int expectedPathLength, String expectedMethod, Response defaultResponse) {
        super(appService.logger);
        auth = appService.auth;

        this.minPathLength = expectedPathLength;
        this.maxPathLength = expectedPathLength;
        this.expectedMethod = expectedMethod;
        this.defaultResponse = defaultResponse;
    }

    public GuardedMatrixHandler(AppService appService, int minPathLength, int maxPathLength, String expectedMethod, Response defaultResponse) {
        super(appService.logger);
        auth = appService.auth;

        this.minPathLength = minPathLength;
        this.maxPathLength = maxPathLength;
        this.expectedMethod = expectedMethod;
        this.defaultResponse = defaultResponse;
    }

    @Override
    protected Response onRequest(HttpExchange rq, URI rawPath, String[] processedPath, String[] queryParameters, String body) throws Throwable {
        var realLength = processedPath.length-1; //-1 is required because SaneServer counts the initial "/" as a path component (but ignores the final "/" if present), eg. a call to "/_matrix/app/" has 3 components: "", "_matrix" and "app" - users, however, will likely expect that to only be 2 (ie. "_matrix" and "app"), so that -1 is there to match said expectation.
        var pathArg = processedPath[realLength];
        if(realLength < minPathLength || realLength > maxPathLength || !pathIsReallyValid(pathArg, realLength)) return UnknownEndpoint.getError(404, rawPath, "");
        if(!Objects.equals(rq.getRequestMethod(), expectedMethod)) return UnknownEndpoint.getError(405, rawPath, ", when called by a "+rq.getRequestMethod()+" request");

        var authFailure = auth.onAuth(rq.getRequestHeaders().get("Authentication"));
        if(authFailure.isPresent()) return authFailure.get();

        var output = defaultResponse;
        var result = onRequest(rq, body, pathArg, realLength, queryParameters);
        if (result.isPresent()) output = result.get();
        return output;
    }

    protected boolean pathIsReallyValid(String pathArg, int pathLength){
        return true;
    }

    protected abstract Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) throws Throwable;
}