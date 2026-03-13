package hub.guzio.JMatrix;

import hub.guzio.JMatrix.data.Namespaces;
import hub.guzio.JMatrix.data.RegistrationYaml;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import javax.management.ListenerNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class TestedService extends AppService{
    protected TestedService(String token) throws URISyntaxException {
        super(new Logger(), getRegistration(token), new URI("https://api.chat.guziohub.ovh/"));
    }

    protected TestedService(String token, AuthProcessor auth) throws URISyntaxException {
        super(new Logger(), getRegistration(token), auth, new URI("https://api.chat.guziohub.ovh/"));
    }

    @Override
    public Optional<Response> onTransaction(String body) {
        System.out.println("--- Got event: ---\n"+body+"\n------------\n");
        return Optional.empty();
    }

    @Override
    public Optional<Response> onUserRequest(String userId) throws ListenerNotFoundException {
        throw new ListenerNotFoundException("This simple test does not have any listeners for the onUserRequest event, as user creation is not implemented by it at the time.");
    }

    @Override
    public Optional<Response> onRoomRequest(String roomAlias) throws ListenerNotFoundException {
        throw new ListenerNotFoundException("This simple test does not have any listeners for the onRoomRequest event, as room creation is not implemented by it at the time.");
    }

    private static RegistrationYaml getRegistration(String token){
        return new RegistrationYaml(
                token,
                token,
                "jmatrixtest",
                new Namespaces(Optional.empty(), Optional.empty(), Optional.empty()),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                "_jmatrixtest_test_user",
                null
        );
    }
}