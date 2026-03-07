package hub.guzio.JMatrix;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;
import hub.guzio.SaneServer.SmartHandler;

import java.net.URI;

public abstract class MatrixHandler extends SmartHandler {
    protected Logger l;

    public MatrixHandler(Logger logger) {
        super(logger);
        this.l = logger;
    }
    public MatrixHandler(){
        l = new Logger();
    }

    @Override
    protected Response onError(HttpExchange rq, URI path, Headers resp, Throwable e) {
        l.err("Error while handling \""+path+"\": ", e);
        return new Response(500, "json", "{\"errcode\":\"M_UNKNOWN\",\"error\":\"The AppService thrown an unhandled exception while processing your request. Sorry, that's all we know. ;-<\"}");
    }
}
