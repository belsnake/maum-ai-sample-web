package ai.maum.sample_web.controller;

import ai.maum.sample_web.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Controller
public class SampleController {

    @Autowired
    SampleService sampleService;

    /*
    ** HOME
    */
    @RequestMapping(value = "/")
    public String home() throws IOException {
        return "home";
    }

}
