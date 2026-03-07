package hub.guzio.JMatrix.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;
import hub.guzio.SaneServer.SmartHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public abstract class MatrixHandler extends SmartHandler {
    protected Logger l;

    public MatrixHandler(Logger logger) {
        super(logger);
        this.l = logger;
    }

    @Override
    protected Response onRequest(HttpExchange rq, Headers resp, URI rawPath, String[] processedPath, String[] queryParameters) throws Throwable {
        //thx, https://stackoverflow.com/questions/10393879/how-to-get-an-http-post-request-body-as-a-java-string-at-the-server-side (sweet Jesus Christ, Java is so hopelessly verbose)
        InputStreamReader isr =  new InputStreamReader(rq.getRequestBody(), StandardCharsets.UTF_8); //UTF-8 can be safely assumed, as per https://spec.matrix.org/v1.17/appendices/#canonical-json
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();
        return onRequest(rq, rawPath, processedPath, queryParameters, buf.toString());
    }

    @Override
    protected Response onError(HttpExchange rq, URI path, Headers resp, Throwable e) {
        l.err("Error while handling \""+path+"\": ", e);
        return new Response(500, "json", "{\"errcode\":\"M_UNKNOWN\",\"error\":\"The AppService thrown an unhandled exception while processing your request. Sorry, that's all we know. ;-<\"}");
    }

    protected abstract Response onRequest(HttpExchange rq, URI rawPath, String[] processedPath, String[] queryParameters, String body) throws Throwable;
}
