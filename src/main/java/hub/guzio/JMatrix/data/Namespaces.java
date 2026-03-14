package hub.guzio.JMatrix.data;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static hub.guzio.JMatrix._internal.Utils.stringifyArray;

public record Namespaces(
        @NotNull Optional<List<Namespace>> aliases,
        @NotNull Optional<List<Namespace>> rooms,
        @NotNull Optional<List<Namespace>> users
) {
    @Override
    public @NotNull String toString() {
        var output = "";
        if (aliases.isPresent()) output += "  aliases:\n" + String.join("", stringifyArray(aliases.get().toArray()));
        if (rooms.isPresent())   output += "  rooms:\n"  +  String.join("", stringifyArray(rooms.get().toArray()));
        if (users.isPresent())   output += "  users:\n"  +  String.join("", stringifyArray(users.get().toArray()));
        return output;
    }

    public static boolean notFoundInAnyNamespace(Optional<List<Namespace>> namespaces, String pattern) throws PatternSyntaxException {
        if (namespaces.isEmpty()) return true;
        for (Namespace ns : namespaces.get()) if (Pattern.compile(ns.regex()).matcher(pattern).find()) return false;
        return true;
    }
}