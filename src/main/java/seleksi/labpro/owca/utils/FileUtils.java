package seleksi.labpro.owca.utils;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class FileUtils {

    private static final Path MEDIA_ROOT = Paths.get("media");

    public static String getFileAsBase64String(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (path.startsWith(MEDIA_ROOT)) {
            path = MEDIA_ROOT.relativize(path);
        }

        Path fullPath = MEDIA_ROOT.resolve(path);
        byte[] fileBytes = Files.readAllBytes(fullPath);
        return Base64.getEncoder().encodeToString(fileBytes);
    }
}