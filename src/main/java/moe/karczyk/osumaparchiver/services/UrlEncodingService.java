package moe.karczyk.osumaparchiver.services;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Service
public class UrlEncodingService {

    @SneakyThrows
    public String encodePath(Path path) {

        var builder = new StringBuilder(path.getRoot().toUri().toString());
        builder.deleteCharAt(builder.length() - 1);
        for (Path fragment : path) {
            var encodedFragment = URLEncoder.encode(fragment.toString(), StandardCharsets.UTF_8);
            builder.append("/").append(encodedFragment);
        }

        return builder.toString().replace("+", "%20");
    }
}
