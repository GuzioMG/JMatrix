package hub.guzio.JMatrix._internal;

import java.util.Objects;
import java.util.Optional;

public class Utils {
    public static String[] stringifyArray(Object[] objects){
        var strings = new String[objects.length];
        int index = 0;
        for (Object obj : objects){
            strings[index] = obj.toString();
            index++;
        }
        return strings;
    }

    public static Optional<String> extractArg(String[] query, String param){
        for(var pairRaw : query) {
            var pair = pairRaw.split("=", 2);
            if (!Objects.equals(pair[0], param)) continue;

            if (pair.length == 1) return Optional.of("\b"); //Basically, we signal that the = got „eaten”.
            else return Optional.of(pair[1]);
        }

        return Optional.empty();
    }
}