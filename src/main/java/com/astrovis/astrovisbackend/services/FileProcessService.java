package com.astrovis.astrovisbackend.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


@Service
public class FileProcessService {




    @Value("${fileProcessService.octreeConverterRequestUrl}")
    private String octreeConverterRequestUrl;
    @Value("${fileProcessService.convertionTaskQueryUrl}")
    private String convertionTaskQueryUrl;
    private static class OctreeConverter implements Runnable{




        private String filePath;
        private String fileFormat;
        private String outputDir;

        private boolean success;

        private Map<String,String> attributeMapping;
        private static final String[] attributeNames = new String[]{"bv","lum","absMag","vx","vy","vz","speed"};

        private final RestTemplate restTemplate = new RestTemplate();

        private final CountDownLatch latch = new CountDownLatch(1);

        private String convertionTaskQueryUrl;
        private String octreeConverterRequestUrl;

        public OctreeConverter(String filepath,String fileFormat,Map<String,String> attributeMapping,String outputDir,String octreeConverterRequestUrl,String convertionTaskQueryUrl){
            this.fileFormat = fileFormat;
            this.filePath = filepath;
            this.outputDir = outputDir;
            this.convertionTaskQueryUrl = convertionTaskQueryUrl;
            this.octreeConverterRequestUrl = octreeConverterRequestUrl;

            this.attributeMapping = new HashMap<>();
            for(String attribute:attributeNames){
                this.attributeMapping.put(attribute,attributeMapping.get(attribute));
            }
        }



        private String createConvertTask(String filePath,String fileFormat, Map<String,String> attributeMapping,String outputDir){
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("filePath", filePath);
            requestParams.put("attributeMapping", attributeMapping);
            requestParams.put("fileFormat", fileFormat);
            requestParams.put("outputDir", outputDir);
            // 构造请求头（可选）
            Map<String,String> response = sendPostRequest(octreeConverterRequestUrl,requestParams);
            if (response == null){return null;}
            return response.get("taskId");
        }


        private void waitTask(long mills){

            try {
                Thread.sleep(mills);
            } catch (InterruptedException e) {

            }
        }

        private Map sendPostRequest(String url,Map<String,Object> params){

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json"); // 设置为 JSON
            // 构造请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
            try {
                // 发送 POST 请求
                ResponseEntity<Map> response = restTemplate.postForEntity(url,requestEntity, Map.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    return response.getBody();
                } else {
                    return null; // 请求失败
                }
            } catch (Exception e) {
                e.printStackTrace(); // 日志记录
                return null; // 请求异常处理
            }
        }



        private int queryStatus(String taskId){
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("taskId",taskId);
            Map<String,Object> response = sendPostRequest(convertionTaskQueryUrl,requestParams);
            if (response == null || response.get("status") == null){
                return -1;
            }
            return (Integer) response.get("status");
        }
        private boolean getConvertResult(String taskId){
            while (true){
                waitTask(10000);
                int status = queryStatus(taskId);
                if (status<0){
                    return false;
                }
                boolean completed = (status&1)==1;
                boolean error = ((status>>1)&1)==1;
                if (error){return false;}
                if (completed){
                    return true;
                }
            }
        }

        @Override
        public void run() {
            try {
                String taskId = createConvertTask(filePath, fileFormat, attributeMapping,outputDir);
                if (taskId != null) {
                    success = getConvertResult(taskId);
                }
            }
            catch (Exception e){
                success = false;
            }
            finally {
                latch.countDown();
            }
        }



        public void covert(){
            Thread convertThread = new Thread(this);
            convertThread.start();
            try {
                latch.await();
            } catch (InterruptedException e) {

            }

        }

        public boolean getResult(){
            return success;
        }
    }

    public boolean transferDatasetToOctree(String filePath, String fileFormat, String outputDir,Map<String,String> attributeMapping){

        OctreeConverter octreeConverter = new OctreeConverter(filePath,fileFormat,attributeMapping,outputDir,octreeConverterRequestUrl,convertionTaskQueryUrl);
        octreeConverter.covert();
        return octreeConverter.getResult();
    }
}
