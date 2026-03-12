package hub.guzio.JMatrix;

import com.sun.net.httpserver.HttpServer;
import hub.guzio.JMatrix.authProcessors.DefaultAuth;
import hub.guzio.JMatrix.data.RegistrationYaml;
import hub.guzio.JMatrix.handlers.UnknownEndpoint;
import hub.guzio.JMatrix.handlers._internal.*;
import hub.guzio.JMatrix.handlers._internal.PingHandler;
import hub.guzio.JMatrix.handlers._internal.query.UnknownProtocolQueryHandler;
import hub.guzio.JMatrix.handlers._internal.query.inProtocol.LocationQueryInProtocolHandler;
import hub.guzio.JMatrix.handlers._internal.query.inProtocol.ProtocolQueryHandler;
import hub.guzio.JMatrix.handlers._internal.query.inProtocol.UserQueryInProtocolHandler;
import hub.guzio.JMatrix.handlers._internal.query.LocationQueryHandler;
import hub.guzio.JMatrix.handlers._internal.query.UserQueryHandler;
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
    public final AuthProcessor auth;

    protected AppService(Logger logger, int backlog, RegistrationYaml registration){
        this.logger = logger;
        this.backlog = backlog;
        this.registration = registration;
        this.auth = new DefaultAuth(registration.hs_token(), logger);
    }

    protected AppService(Logger logger, RegistrationYaml registration){
        this.logger = logger;
        this.backlog = 1024;
        this.registration = registration;
        this.auth = new DefaultAuth(registration.hs_token(), logger);
    }

    protected AppService(Logger logger, int backlog, RegistrationYaml registration, AuthProcessor auth){
        this.logger = logger;
        this.backlog = backlog;
        this.registration = registration;
        this.auth = auth;
    }

    protected AppService(Logger logger, RegistrationYaml registration, AuthProcessor auth){
        this.logger = logger;
        this.backlog = 1024;
        this.registration = registration;
        this.auth = auth;
    }

    public HttpServer serve(InetSocketAddress port) throws IllegalStateException, IOException {
        if (consumed) throw new IllegalStateException("Attempted to serve an already-consumed AppService instance.");
        consumed = true;

        HttpServer server = HttpServer.create(port, backlog);

        //Core endpoints
        server.createContext("/_matrix/app/v1/transactions/", new TransactionHandler(this));
        server.createContext("/_matrix/app/v1/ping", new PingHandler(this));
        server.createContext("/_matrix/app/v1/users/", new UserCreateHandler(this));
        server.createContext("/_matrix/app/v1/rooms/", new RoomCreateHandler(this));

        //Static protocol endpoints
        server.createContext("/_matrix/app/v1/thirdparty/user", new UserQueryHandler(this));
        server.createContext("/_matrix/app/v1/thirdparty/protocol/", new UnknownProtocolQueryHandler(this));
        server.createContext("/_matrix/app/v1/thirdparty/location", new LocationQueryHandler(this));

        //Dynamic protocol endpoints
        if (registration.protocols().isPresent()){
            for(var proto : registration.protocols().get().entrySet()){
                server.createContext("/_matrix/app/v1/thirdparty/user/"+proto.getKey(), new UserQueryInProtocolHandler(this, proto.getValue()));
                server.createContext("/_matrix/app/v1/thirdparty/protocol/"+proto.getKey(), new ProtocolQueryHandler(this, proto.getValue()));
                server.createContext("/_matrix/app/v1/thirdparty/location/"+proto.getKey(), new LocationQueryInProtocolHandler(this, proto.getValue()));
            }
        }

        //Unknown endpoints
        server.createContext("/_matrix/", new UnknownEndpoint(logger));

        return server;
    }

    public abstract Optional<Response> onTransaction(String body) throws Throwable;
    public abstract Optional<Response> onUserRequest(String userId) throws Throwable;
    public abstract Optional<Response> onRoomRequest(String roomAlias) throws Throwable;
}