package pl.blaszak.speech;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ItemGeneratorUtil {

    public String[] loadFile(String filename) throws Exception{
        URL resource = getClass().getClassLoader().getResource(filename);
        Path path = Paths.get(resource.toURI());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return lines.toArray(new String[0]);
    }

}
