package com.mydemo.ProjectGeneratorApplication.controller;
import com.mydemo.ProjectGeneratorApplication.model.ProjectConfig;
import com.mydemo.ProjectGeneratorApplication.service.ProjectGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProjectController {

    @Autowired
    private ProjectGeneratorService projectGeneratorService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("projectConfig", new ProjectConfig());
        return "index";
    }

   /* @PostMapping(value = "/generate", consumes =  "application/x-www-form-urlencoded")
    public ResponseEntity<byte[]> generateProject(@RequestBody ProjectConfig config) {
        try {
            byte[] projectZip = projectGeneratorService.generateProject(config);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + config.getArtifactId() + ".zip");

            return new ResponseEntity<>(projectZip, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/

    @PostMapping("/generate")
    @ResponseBody
    public ResponseEntity<byte[]> generateProject(@RequestBody ProjectConfig config) {
        try {
            byte[] projectZip = projectGeneratorService.generateProject(config);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + config.getArtifactId() + ".zip");

            return new ResponseEntity<>(projectZip, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
