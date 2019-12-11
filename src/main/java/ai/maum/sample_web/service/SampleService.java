package ai.maum.sample_web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SampleService {

    @Value("${api.id}")
    String API_ID;

    @Value("${api.key}")
    String API_KEY;

    @Value("${upload.dir}")
    String UPLOAD_DIR;

    public String runMrc(String lang, String context, String question) {
        try {
            String url = "https://api.maum.ai/api/bert.mrc/";
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);

            // 데이터
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("apiId", API_ID);
            paramMap.put("apiKey", API_KEY);
            paramMap.put("lang", lang);
            paramMap.put("context", context);
            paramMap.put("question", question);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(paramMap);
            log.info("REQUEST = {}", json);
            post.setHeader("Content-Type", "application/json");
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            post.setEntity(stringEntity);

            HttpResponse response = client.execute(post);

            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("response = {}", result);
                return result;
            }

            log.error("Response Code : {}", responseCode);
        } catch(Exception e) {
            log.error(e.toString());
        }

        String resultStr = "{ \"status\": \"error\" }";
        return resultStr;
    }


    public ResponseEntity<byte[]> runTts(String text, String voiceName) {
        try {
            String url = "https://api.maum.ai/tts/stream";

            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("text", new StringBody(text, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            builder.addPart("apiId", new StringBody(API_ID, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            builder.addPart("apiKey",new StringBody(API_KEY, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            builder.addPart("voiceName", new StringBody(voiceName, ContentType.TEXT_PLAIN.withCharset("UTF-8")));

            HttpEntity entity = builder.build();

            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();

            HttpEntity responseEntity = response.getEntity();

            InputStream in = responseEntity.getContent();
            byte[] audioArray = IOUtils.toByteArray(in);

            ResponseEntity<byte[]> resultEntity = new ResponseEntity<byte[]>(audioArray, HttpStatus.OK);
            return resultEntity;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String runStt(MultipartFile file, String lang, String level, String sampling) {
        try {
            String url = "https://api.maum.ai/api/stt/";

            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);

            /* 음원 데이타를 Multipart로 등록하기 위한 준비 */
            String uploadPath = UPLOAD_DIR + "/stt";
            File varfile = new File(uploadPath);
            if (varfile.exists() == false) {
                varfile.mkdirs();
            }
            varfile = new File((uploadPath +"/"+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("\\") + 1)));
            file.transferTo(varfile);
            FileBody fileBody = new FileBody(varfile);

            /* Multipart 등록 */
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("file", fileBody);
            builder.addPart("lang", new StringBody(lang, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("level", new StringBody(level, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("sampling", new StringBody(sampling, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("ID", new StringBody(API_ID, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("key", new StringBody(API_KEY, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("cmd", new StringBody("runFileStt", ContentType.MULTIPART_FORM_DATA));
            HttpEntity entity = builder.build();
            post.setEntity(entity);

            /* STT API 실행 */
            HttpResponse response = client.execute(post);

            int responseCode = response.getStatusLine().getStatusCode();
            /* 정상 응답 수신 */
            if (responseCode == 200) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                return result;

            }
            /* 에러 응답 수신 */
            else {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");

                System.out.println("Response Result : " + result.replace('+', ' '));
                System.out.println("API 호출 에러 발생 : 에러코드=" + responseCode);
                return "{ \"status\": \"error\" }";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"status\": \"error\" }";
        }
    }
}
