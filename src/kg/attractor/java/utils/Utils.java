package kg.attractor.java.utils;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    private Utils() {
    }

    private static Optional<Map.Entry<String,String>> decode(String kv) {
        if (!kv.contains("=")) return Optional.empty();

        String[] pair = kv.split("=", 2);
        if (pair.length != 2) return Optional.empty();

        Charset utf8 = StandardCharsets.UTF_8;

        String key   = URLDecoder.decode(pair[0].strip(), utf8);
        String value = URLDecoder.decode(pair[1].strip(), utf8);

        return Optional.of(Map.entry(key, value));
    }


    public static Map<String, String> parseUrlEncoded(String input, String delimiter) {
        return Arrays.stream(input.split(delimiter))
                .map(part -> part.split("="))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(
                        p -> URLDecoder.decode(p[0], StandardCharsets.UTF_8),
                        p -> URLDecoder.decode(p[1], StandardCharsets.UTF_8)
                ));
    }
}
