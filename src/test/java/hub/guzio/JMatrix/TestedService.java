package hub.guzio.JMatrix;

import hub.guzio.JMatrix.data.Namespaces;
import hub.guzio.JMatrix.data.RegistrationYaml;
import hub.guzio.SaneServer.Logger;
import hub.guzio.SaneServer.Response;

import java.util.Optional;

public class TestedService extends AppService{
    protected TestedService(String token) {
        super(new Logger(), new RegistrationYaml(token, token, "jmatrixtest", new Namespaces(), Optional.empty(), Optional.empty(), Optional.empty(), "_jmatrix_test_user", null));
    }

    @Override
    public Optional<Response> onTransaction() throws Throwable {
        return Optional.empty();
    }

    @Override
    public Optional<Response> onUserRequest() throws Throwable {
        return Optional.empty();
    }

    @Override
    public Optional<Response> onRoomRequest() throws Throwable {
        return Optional.empty();
    }
}