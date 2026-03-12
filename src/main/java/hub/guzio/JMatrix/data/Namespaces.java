package hub.guzio.JMatrix.data;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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

    public static String[] stringifyArray(Object[] objects){
        var strings = new String[objects.length];
        int index = 0;
        for (Object obj : objects){
            strings[index] = obj.toString();
            index++;
        }
        return strings;
    }

    public static boolean tryRegex(Optional<List<Namespace>> namespaces, String pattern) throws PatternSyntaxException {
        if (namespaces.isEmpty()) return false;
        for (Namespace ns : namespaces.get()) if (Pattern.compile(ns.regex()).matcher(pattern).find()) return true;
        return false;
    }
}