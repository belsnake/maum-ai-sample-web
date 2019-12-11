package ai.maum.sample_web.controller;

import ai.maum.sample_web.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Value("${api.id}")
    private String API_ID;

    @Value("${api.key}")
    private String API_KEY;

    /*
    ** HOME
    */
    @RequestMapping(value = "/")
    public String home() throws IOException {
        return "home";
    }

    /*
     ** MRC
     */
    @RequestMapping(value = "/sampleMrc")
    public String sampleMrc() throws IOException {
        return "sampleMrc";
    }

    /*
     ** TTS
     */
    @RequestMapping(value = "/sampleTts")
    public String sampleTts() throws IOException {
        return "sampleTts";
    }

    /*
     ** FileSTT
     */
    @RequestMapping(value = "/sampleFileStt")
    public String sampleFileStt() throws IOException {
        return "sampleFileStt";
    }

    /*
     ** EngEduSTT
     */
    @RequestMapping(value = "/sampleEngEduStt")
    public String sampleEngEduStt(Model model) throws IOException {

        model.addAttribute("apiId", API_ID);
        model.addAttribute("apiKey", API_KEY);
        return "sampleEngEduStt";
    }
}
