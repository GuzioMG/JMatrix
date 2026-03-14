package hub.guzio.JMatrix.handlers;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.net.URI;

public class UnknownEndpoint extends MatrixHandler {
    public UnknownEndpoint(Logger logger) { super(logger); }

    @Override
    protected Response onRequest(HttpExchange rq, URI path, String[] pathButUseless, String[] qp, String body) {
        return getError(404, path, "");
    }

    public static Response getError(int code, URI path, String context) {
        return new Response(code, "json", "{\"errcode\":\"M_UNRECOGNIZED\",\"error\":\"ERROR "+code+": \\\""+path+"\\\" is not a valid Matrix AppService V1 endpoint"+context+".\"}");
    }
}