package com.mydemo.ProjectGeneratorApplication.service;
import com.mydemo.ProjectGeneratorApplication.model.ProjectConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ProjectGeneratorService {

    public byte[] generateProject(ProjectConfig config) throws IOException {
        // Create temporary directories
        String tempDir = Files.createTempDirectory("project-").toString();
        File projectDir = new File(tempDir, config.getArtifactId());
        projectDir.mkdirs();

        // Create basic project structure
        File srcMainJavaDir = new File(projectDir, "src/main/java");
        File srcMainResourcesDir = new File(projectDir, "src/main/resources");
        File srcTestJavaDir = new File(projectDir, "src/test/java");

        srcMainJavaDir.mkdirs();
        srcMainResourcesDir.mkdirs();
        srcTestJavaDir.mkdirs();

        // Create basic files
        File pomFile = new File(projectDir, "pom.xml");
        File dockerFile = new File(projectDir, "docker");
        File appFile = new File(srcMainJavaDir, config.getArtifactId() + ".java");

        String pomContent = generatePomContent(config);
        String appContent = generateAppContent(config);

        FileUtils.writeStringToFile(pomFile, pomContent, "UTF-8");
        FileUtils.writeStringToFile(appFile, appContent, "UTF-8");

        // Zip the project directory
        File zipFile = new File(tempDir, config.getArtifactId() + ".zip");
        ZipUtil.pack(projectDir, zipFile);

        // Read zip file to byte array
        byte[] zipBytes = Files.readAllBytes(zipFile.toPath());

        // Clean up temporary files
        FileUtils.deleteDirectory(new File(tempDir));

        return zipBytes;
    }

    private String generatePomContent(ProjectConfig config) {
        return "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>" + config.getGroupId() + "</groupId>\n" +
                "    <artifactId>" + config.getArtifactId() + "</artifactId>\n" +
                "    <version>" + config.getVersion() + "</version>\n" +
                "    <packaging>" + config.getPackaging() + "</packaging>\n" +
                "    <dependencies>\n" +
                "        <!-- Add dependencies here -->\n" +
                "    </dependencies>\n" +
                "</project>";
    }

    private String generateAppContent(ProjectConfig config) {
        return "package " + config.getGroupId() + ";\n\n" +
                "import org.springframework.boot.SpringApplication;\n" +
                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n\n" +
                "@SpringBootApplication\n" +
                "public class " + config.getArtifactId() + " {\n" +
                "    public static void main(String[] args) {\n" +
                "        SpringApplication.run(" + config.getArtifactId() + ".class, args);\n" +
                "    }\n" +
                "}";
    }
}
