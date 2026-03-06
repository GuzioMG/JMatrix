package hub.guzio.MatrixTest.sensibleServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

public abstract class SmartHandler implements HttpHandler {

    public SmartHandler(){}
    public SmartHandler(Logger lg) {this.lg = lg;}
    Logger lg = new Logger();

    @Override
    public void handle(HttpExchange exchange) {
        //Setup
        var path = exchange.getRequestURI();
        var head = exchange.getResponseHeaders();
        lg.log("Handling: "+path);

        //Responding
        Response resp;
        try {
            //Getting response data
            try {
                resp = onRequest(exchange, path, head);
            } catch (Throwable e) {
                head.clear();
                try { resp = onError(exchange, path, head, e); }
                //Always retry the onError
                catch (Throwable ex) {
                    head.clear();
                    resp = onError(exchange, path, head, ex);
                }
            }
            head.add("Content-Type", "text/"+resp.format()+"; charset=UTF-8");

            //Sending headers (feat. a retry)
            try{
                exchange.sendResponseHeaders(resp.code(), resp.length());
            } catch (IOException e){
                head.clear();
                try { resp = onError(exchange, path, head, e); }
                //Always retry the onError
                catch (Throwable ex) {
                    head.clear();
                    resp = onError(exchange, path, head, ex);
                }
                head.add("Content-Type", "text/"+resp.format()+"; charset=UTF-8");
                exchange.sendResponseHeaders(resp.code(), resp.length());
            }

            //Sending body
            try{
                exchange.getResponseBody().write(resp.getBytes());
            } catch (IOException e) {
                lg.wrn("Client dropped out while receiving \""+path+"\": ", e);
            }
        } catch (Throwable e) {
            lg.err("2nd attempt to onError() or sendResponseHeaders() failed while handling \""+path+"\": ", e);
        }

        //Cleanup
        exchange.close();
    }

    public abstract Response onRequest(HttpExchange rq, URI path, Headers resp) throws Throwable;
    public Response onError(HttpExchange rq, URI path, Headers resp, Throwable e) throws Throwable {
        lg.err("Error while handling \""+path+"\": ", e);
        return new Response(500, "plain", "500 Unhandled server exception");
    }
}