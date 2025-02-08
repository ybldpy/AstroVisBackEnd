package com.astrovis.astrovisbackend.Controller;



import com.astrovis.astrovisbackend.commons.Response;
import com.astrovis.astrovisbackend.commons.Result;
import com.astrovis.astrovisbackend.exception.IllegalParamException;
import com.astrovis.astrovisbackend.model.UploadedFile;
import com.astrovis.astrovisbackend.model.UploadedFileExample;
import com.astrovis.astrovisbackend.model.User;
import com.astrovis.astrovisbackend.model.UserExample;
import com.astrovis.astrovisbackend.services.UploadedFileService;
import com.astrovis.astrovisbackend.services.UserService;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileController {

    private static class FileUploadTask{
        private String originalFileName;
        private String datasetName;
        private Integer type;
        private Map<String,Object> parameters;

        private Integer chunkNums;

        public Integer getChunkNums() {
            return chunkNums;
        }

        public void setChunkNums(Integer chunkNums) {
            this.chunkNums = chunkNums;
        }

        public String getOriginalFileName() {
            return originalFileName;
        }

        public void setOriginalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
        }

        public String getDatasetName() {
            return datasetName;
        }

        public void setDatasetName(String datasetName) {
            this.datasetName = datasetName;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
    }

    private final static String FILE_TYPE_CATELOG = "catelog";
    private final static String FILE_TYPE_LAYER = "layer";
    private final static String UUID_GENERATE_PREFIX = "file:id:";
    private final static Map<String,Integer> TYPES;

    static {
        TYPES = new HashMap<>();
        TYPES.put(FILE_TYPE_CATELOG,0);
        TYPES.put(FILE_TYPE_LAYER,1);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UploadedFileService uploadedFileService;


    @Value("${uploadedFileDir}")
    private String uploadedFileDir;






    @GetMapping("/cancelUploadTask")
    public Response<Result> cancelUploadTask(@RequestParam String fileId){
        uploadedFileService.cancelUploadTask(fileId);
        Result result = new Result();
        result.setStatus(1);
        result.setMsg("ok");
        return Response.ok(result);
    }


    @GetMapping("/get/{fileId}/{file}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileId,@PathVariable String file) throws IOException {
        Path filePath = Paths.get(uploadedFileDir+"/"+fileId+"/"+file);
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }
        Resource fileResource = new UrlResource(filePath.toUri());
        String contentType = Files.probeContentType(filePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileResource);
    }



    private boolean checkUploadTaskParameters(FileUploadTask fileUploadTask){
        if (fileUploadTask == null || StringUtils.isAnyBlank(fileUploadTask.datasetName,fileUploadTask.datasetName)){return false;}
        for(Map.Entry<String,Integer> pair:TYPES.entrySet()){
            if (pair.getValue() == fileUploadTask.type){return true;}
        }
        return false;
    }


    private String generateFileId(String uploader,String fileName){
        return UUID.nameUUIDFromBytes((UUID_GENERATE_PREFIX+uploader+":"+fileName).getBytes()).toString();
    }

    @PostMapping("/createFileUploadTask")
    @ResponseBody
    @Transactional
    public Response<Result<String>> initFileUploadTask(@RequestBody @Validated FileUploadTask fileUploadTask){



        // for test
//        if (true){
//            Result<String> result = new Result<>();
//            result.setStatus(1);
//            result.setData("123456");
//            return Response.ok(result);
//        }

        if (!checkUploadTaskParameters(fileUploadTask)){
            throw new IllegalParamException();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        return Response.ok(uploadedFileService.createCatalogFileUploadTask(user,fileUploadTask.datasetName,fileUploadTask.originalFileName,fileUploadTask.getChunkNums(),(Map<String,String>)fileUploadTask.getParameters().get("catalogAttributeMapping")));

    }


    private static class FileChunkUploadParams{
        private String fileId;
        private Integer chunkIndex;

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public Integer getChunkIndex() {
            return chunkIndex;
        }

        public void setChunkIndex(Integer chunkIndex) {
            this.chunkIndex = chunkIndex;
        }

        public boolean isLastChunk() {
            return isLastChunk;
        }

        public void setLastChunk(boolean lastChunk) {
            isLastChunk = lastChunk;
        }

        public MultipartFile getChunk() {
            return chunk;
        }

        public void setChunk(MultipartFile chunk) {
            this.chunk = chunk;
        }

        private boolean isLastChunk;
        private MultipartFile chunk;
    }





    @GetMapping("/delete")
    @ResponseBody
    public Response<Result> deleteUserFile(@RequestParam(required = true)String fileId){


        boolean res = uploadedFileService.deleteFile(fileId);
        Result result = new Result();
        if (!res){
            result.setStatus(0);
            result.setMsg("Unable to delete");
        }
        else {
            result.setStatus(1);
        }
        return Response.ok(result);
    }





    @GetMapping("/queryFiles")
    @ResponseBody
    public Response<Result<List<UploadedFile>>> queryUserFiles(@RequestParam(required = false) String fileId,@RequestParam(required = false) Integer status,@RequestParam(required = false) Integer category,Authentication authentication){


        List<UploadedFile> files = new ArrayList<>();
        Result<List<UploadedFile>> result = new Result<>();
        result.setStatus(1);
        result.setData(files);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        User user = userService.selectFirstByExample(userExample);
        if (user==null){
            return Response.ok(result);
        }



        UploadedFileExample uploadedFileExample = new UploadedFileExample();

        UploadedFileExample.Criteria criteria = uploadedFileExample.createCriteria();
        criteria.andUploaderEqualTo(user.getUid()).andDeletedEqualTo(false);
        if (!StringUtils.isBlank(fileId)){
            criteria.andFileidEqualTo(fileId);
        }
        if (status!=null){
            criteria.andFilestatusEqualTo(status);
        }
        if (category!=null){
            criteria.andCategoryEqualTo(category);
        }


        List<UploadedFile> userFiles = uploadedFileService.selectByExample(uploadedFileExample);
        if (userFiles == null){
            return Response.ok(result);
        }
        for(UploadedFile uploadedFile:userFiles){
            files.add(uploadedFile);
        }
        return Response.ok(result);
    }

    @PostMapping("/chunkUpload")
    @ResponseBody
    public Response<Result> fileChunkUpload(@RequestParam(required = true) String fileId, @RequestParam(required = true) Integer chunkIndex,
                                            @RequestParam(required = true) MultipartFile chunk) {





        if (chunkIndex<0){throw new IllegalParamException();}


//        if (true){
//            Result result = new Result();
//            result.setStatus(1);
//            return Response.ok(result);
//        }

        Result result = new Result();
        if (!uploadedFileService.fileUploadTaskExist(fileId)){
            result.setStatus(0);
            result.setMsg("File does not exist");
            return Response.ok(result);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean storeResult = uploadedFileService.storeFileChunk(fileId,chunkIndex,chunk);

        if (!storeResult){
            result.setStatus(0);
            result.setMsg("Unable to store chunk");
            return Response.ok(result);
        }

        if (uploadedFileService.areAllChunksReceived(fileId)){
            uploadedFileService.asyncProcessFile(fileId);
        }
        result.setStatus(1);
        return Response.ok(result);
    }

}
