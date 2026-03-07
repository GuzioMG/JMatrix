package hub.guzio.JMatrix._internal.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.MatrixHandler;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.net.URI;

public class UnknownProtocol extends MatrixHandler {
    public UnknownProtocol(Logger logger) { super(logger); }
    public UnknownProtocol(){}

    @Override
    protected Response onRequest(HttpExchange rq, Headers resp, URI rawPath, String[] processedPath, String[] queryParameters) throws Throwable {
        return new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"ERROR 404: Unknown protocol. Only \\\"minecraft\\\" is supported.\"}");
    }
}
