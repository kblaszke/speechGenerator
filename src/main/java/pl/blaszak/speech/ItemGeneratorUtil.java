package pl.blaszak.speech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ItemGeneratorUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGeneratorUtil.class);

    public String[] loadFile(String filename) {
        try {
            URL resource = getClass().getClassLoader().getResource(filename);
            Path path = Paths.get(resource.toURI());
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            return lines.toArray(new String[0]);
        } catch (Exception e) {
            LOGGER.error("Can not load the file " + filename, e);
            return new String[0];
        }
    }

}
