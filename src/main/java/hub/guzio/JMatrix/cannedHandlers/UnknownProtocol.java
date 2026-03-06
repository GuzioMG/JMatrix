package hub.guzio.JMatrix.cannedHandlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.SaneServer.Response;
import hub.guzio.SaneServer.SmartHandler;

import java.net.URI;

public class UnknownProtocol extends SmartHandler{
    @Override
    public Response onRequest(HttpExchange rq, URI path, Headers resp) {
        return new Response(404, "json", "{\"errcode\":\"M_UNRECOGNIZED\",\"error\":\"ERROR 404: Unknown protocol. Only \\\"minecraft\\\" is supported.\"}");
    }
}
