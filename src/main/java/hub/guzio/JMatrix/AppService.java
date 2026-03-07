package hub.guzio.JMatrix;

import com.sun.net.httpserver.HttpServer;
import hub.guzio.JMatrix._internal.handlers.UnknownEndpoint;
import hub.guzio.JMatrix.setup.RegistrationYaml;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;

public abstract class AppService {
    private boolean consumed = false;

    public final Logger logger;
    public final int backlog;
    public final RegistrationYaml registration;

    protected AppService(Logger logger, int backlog, RegistrationYaml registration){
        this.logger = logger;
        this.backlog = backlog;
        this.registration = registration;
    }

    protected AppService(Logger logger, RegistrationYaml registration){
        this.logger = logger;
        this.backlog = 1024;
        this.registration = registration;
    }

    public HttpServer serve(InetSocketAddress port) throws IllegalStateException, IOException {
        if (consumed) throw new IllegalStateException("Attempted to serve an already-consumed AppService");
        consumed = true;

        HttpServer server = HttpServer.create(port, backlog);

        /*/Core endpoints
        server.createContext("/_matrix/app/v1/transactions/", new MainHandler());
        server.createContext("/_matrix/app/v1/ping", new MainHandler());
        server.createContext("/_matrix/app/v1/users/", new MainHandler());
        server.createContext("/_matrix/app/v1/rooms/", new AliasEndpoint());

        //Protocol endpoints
        server.createContext("/_matrix/app/v1/thirdparty/location/", new AliasEndpoint());
        server.createContext("/_matrix/app/v1/thirdparty/protocol/", new MinecraftProtocol());
        server.createContext("/_matrix/app/v1/thirdparty/user/", new UserlistHandler());

        //Unknown endpoints
        server.createContext("/_matrix/", new UnknownEndpoint());*/

        return server;
    }

    public abstract Optional<Response> onTransaction() throws Throwable;
    public abstract Optional<Response> onUserRequest() throws Throwable;
    public abstract Optional<Response> onRoomRequest() throws Throwable;
}