package ai.maum.sample_web.controller;

import ai.maum.sample_web.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class SampleRestController {

    @Autowired
    SampleService sampleService;

    /*
    ** MRC
    */
    @RequestMapping(value = "/api/sampleMrc")
    public String sampleMrc(@RequestParam(value = "lang") String lang,
                            @RequestParam(value = "context") String context,
                            @RequestParam(value = "question") String question) throws IOException {

        return sampleService.runMrc(lang, context, question);
    }

    /*
    ** TTS
    */
    @RequestMapping(value = "/api/sampleTts")
    public ResponseEntity<byte[]> sampleTts(@RequestParam(value = "text") String text,
                                            @RequestParam(value = "voiceName") String voiceName) {
        return sampleService.runTts(text, voiceName);
    }

    /*
    ** STT
    */
    @RequestMapping(value = "/api/sampleStt")
    public String sampleStt( @RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "lang") String lang,
                                             @RequestParam(value = "level") String level,
                                             @RequestParam(value = "sampling") String sampling ) {

        return sampleService.runStt(file, lang, level, sampling);
    }
}
