package hub.guzio.JMatrix.setup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record RegistrationYaml(
        @NotNull String as_token,
        @NotNull String hs_token,
        @NotNull String id,
        @NotNull Namespaces namespaces,
        @NotNull Optional<Map<String, Protocol>> protocols,
        @NotNull Optional<Boolean> rate_limited,
        @NotNull Optional<Boolean> receive_ephemeral,
        @NotNull String sender_localpart,
        @Nullable URI url
) {

    @Override
    public @NotNull String toString() {
        String uriString;
        if(Objects.isNull(url)) uriString = "null";
        else uriString = "\""+url+"\"";

        var output = "as_token: \"" + as_token + "\"\nhs_token: \"" + hs_token + "\"\nid: " + id + "\nnamespaces:\n" + namespaces.serialize();

        if (protocols.isPresent()) output += "protocols:\n  - \"" + String.join("\"\n  - \"", protocols.get().keySet()) + "\"\n";
        if (rate_limited.isPresent()) output += "rate_limited: " + rate_limited.get() + "\n";
        if (receive_ephemeral.isPresent()) output += "receive_ephemeral: " + receive_ephemeral.get() + "\n";

        return output + "sender_localpart: \"" + sender_localpart + "\"\nurl: " + uriString;
    }
}