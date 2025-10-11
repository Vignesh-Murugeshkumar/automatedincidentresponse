package com.team.incidentresponse.util;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class YamlPlaybookLoader {

    public static Map<String, Object> loadPlaybook(String path) {
        Yaml yaml = new Yaml();
        try (InputStream input = YamlPlaybookLoader.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IllegalArgumentException("Playbook not found: " + path);
            }
            return yaml.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load playbook: " + path, e);
        }
    }
}
