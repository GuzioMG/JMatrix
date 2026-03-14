package hub.guzio.JMatrix;

import hub.guzio.JMatrix.authProcessors.TestingOrDevelopmentOnlyAuthProcessorUnsuitableForProductionDueToSevereSecurityConsequences;
import hub.guzio.JMatrix.data.Namespace;
import hub.guzio.JMatrix.data.Namespaces;
import hub.guzio.JMatrix.data.RegistrationYaml;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.management.ListenerNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class TestedService extends AppService{
    private static final @NotNull Logger centralLogger = new Logger();

    protected TestedService(String token, String inUrl, String outUrl) throws URISyntaxException {
        super(centralLogger, getRegistration(token, new URI(inUrl)), new URI(outUrl));
    }

    protected TestedService() throws URISyntaxException {
        super(centralLogger, getRegistration("devnull", null), new TestingOrDevelopmentOnlyAuthProcessorUnsuitableForProductionDueToSevereSecurityConsequences(centralLogger), new URI("https://api.chat.guziohub.ovh/"));
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

    private static RegistrationYaml getRegistration(@NotNull String token, @Nullable URI url) throws URISyntaxException {
        Map<String, Protocol> protocols = new HashMap<>();
        new LazyProtocol(centralLogger).append(protocols);
        new AdhdProtocol(centralLogger).append(protocols);

        List<Namespace> aliases = new ArrayList<>();
        aliases.add(new Namespace(true, "^#_jmatrix_test_roomalias\\d+:guziohub\\.ovh$"));

        List<Namespace> rooms = new ArrayList<>();
        rooms.add(new Namespace(false, "^!.{18,}$"));

        List<Namespace> users = new ArrayList<>();
        users.add(new Namespace(true, "^@_jmatrix_test_simuser\\d+:guziohub\\.ovh$"));
        users.add(new Namespace(true, "^@_jmatrix_test_user:guziohub\\.ovh$"));

        return new RegistrationYaml(
                token,
                token,
                "jmatrixtest",
                new Namespaces(Optional.of(aliases), Optional.of(rooms), Optional.of(users)),
                Optional.of(protocols),
                Optional.of(false),
                Optional.of(true),
                "_jmatrixtest_test_user",
                url
        );
    }
}