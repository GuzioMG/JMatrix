package hub.guzio.JMatrix;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;
import hub.guzio.SaneServer.SmartHandler;

import java.net.URI;

public abstract class MatrixHandler extends SmartHandler {

    public MatrixHandler(Logger logger) {this.l = logger;}
    protected Logger l;

    @Override
    public Response onError(HttpExchange rq, URI path, Headers resp, Throwable e) throws Throwable {
        l.err("Error while handling \""+path+"\": ", e);
        return new Response(500, "json", "{\"errcode\":\"M_UNKNOWN\",\"error\":\"The AppService thrown an unhandled exception while processing your request. Sorry, that's all we know. ;-<\"}");
    }
}
