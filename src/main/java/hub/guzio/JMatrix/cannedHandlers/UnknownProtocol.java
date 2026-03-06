package hub.guzio.JMatrix.cannedHandlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.MatrixHandler;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.net.URI;

public class UnknownProtocol extends MatrixHandler {
    public UnknownProtocol() {
        super(new Logger());
    }

    @Override
    public Response onRequest(HttpExchange rq, URI path, Headers resp) {
        return new Response(404, "json", "{\"errcode\":\"M_NOT_FOUND\",\"error\":\"ERROR 404: Unknown protocol. Only \\\"minecraft\\\" is supported.\"}");
    }
}
