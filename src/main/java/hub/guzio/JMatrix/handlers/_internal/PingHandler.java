package hub.guzio.JMatrix.handlers._internal;

import com.sun.net.httpserver.HttpExchange;
import hub.guzio.JMatrix.AppService;
import hub.guzio.JMatrix.handlers.GuardedMatrixHandler;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.util.*;

public class PingHandler extends GuardedMatrixHandler {
    int count = 0;
    List<String> received = Collections.synchronizedList(new ArrayList<>());
    Logger logger;

    public PingHandler(AppService appservice){
        super(appservice, 4, "POST", new Response(200, "json", "{}"));
        logger = appservice.logger;
    }

    @Override
    protected boolean pathIsReallyValid(String pathArg, int pathLength){
        return Objects.equals(pathArg, "ping");
    }

    @Override
    protected Optional<Response> onRequest(HttpExchange rq, String body, String pathArg, int pathLength, String[] queryArgs) {
        received.add(body);
        logger.log("Responding to ping "+body);
        return Optional.empty();
    }

    public boolean hasReceived(String txId){
        for (var received : this.received) if (received.contains(txId)) return true;
        return false;
    }

    public String pullTransactionId(){
        count++;
        if (count == 9) return "\"meow\"";
        else return "\"_jmatrix_ping_transaction_no"+count+"\"";
    }
}