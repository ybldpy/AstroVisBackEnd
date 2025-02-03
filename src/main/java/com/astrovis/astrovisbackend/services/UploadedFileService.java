package com.astrovis.astrovisbackend.services;

import com.astrovis.astrovisbackend.Controller.FileController;
import com.astrovis.astrovisbackend.commons.Response;
import com.astrovis.astrovisbackend.commons.Result;
import com.astrovis.astrovisbackend.mappers.UploadedFileMapper;
import com.astrovis.astrovisbackend.model.UploadedFile;
import com.astrovis.astrovisbackend.model.UploadedFileExample;

import com.astrovis.astrovisbackend.model.User;
import com.astrovis.astrovisbackend.model.UserExample;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UploadedFileService extends BaseService<UploadedFileMapper,UploadedFile, UploadedFileExample>{

    public static final int STATUS_FILE_PROCESS_FAILED = -1;
    public static final int STATUS_FILE_CREATED = 0;
    public static final int STATUS_FILE_RECEIVING = 1;
    public static final int STATUS_FILE_PROCESSING = 2;
    public static final int STATUS_FILE_READY = 3;

    public static final int STATUS_FILE_CANCELED = 4;

    private static final int IO_BUFFER_SIZE = 1024*1024*4;


    @Autowired
    FileProcessService fileProcessService;



    private final static String UUID_GENERATE_PREFIX = "file:id:";

    @Autowired
    UserService userService;




    @Value("${tempDir}")
    private String tempDirPath;

    @Value("${uploadedFileDir}")
    private String readyFilesDir;

    private static class FileUploadTask{






        public int getUploadedChunkCount() {
            return uploadedChunkCount;
        }

        public void setUploadedChunkCount(int uploadedChunkCount) {
            this.uploadedChunkCount = uploadedChunkCount;
        }

        public Lock getFileLock() {
            return fileLock;
        }

        public void setFileLock(Lock fileLock) {
            this.fileLock = fileLock;
        }

        public int getTotalChunks() {
            return totalChunks;
        }

        public void setTotalChunks(int totalChunks) {
            this.totalChunks = totalChunks;
        }

        public String getOriginalFileName() {
            return originalFileName;
        }

        public void setOriginalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
        }

        private int uploadedChunkCount;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        private Lock fileLock = new ReentrantLock();
        private int totalChunks;
        private int state;



        private String originalFileName;




    }

    private Map<String,FileUploadTask> fileUploadTaskMap = new ConcurrentHashMap<>();
    private Set<String> retryUpdateStateSet = new HashSet<>();
    private Lock retryLock = new ReentrantLock();



    @Autowired
    @Lazy
    private UploadedFileService uploadedFileService;



    private UploadedFileService getThisProxy(){
        return this.uploadedFileService;
    }

    @Scheduled(fixedDelay = 5000)
    public void retryUpdateState(){
        if (retryUpdateStateSet.size()<1){return;}
        retryLock.lock();
        List<String> successFileList = new ArrayList<>();
        UploadedFileService proxy = getThisProxy();
        for(String fileId: retryUpdateStateSet){
            FileUploadTask fileUploadTask = fileUploadTaskMap.get(fileId);
            if (fileUploadTask==null){
                successFileList.add(fileId);
                continue;
            }
            Lock fileLock = fileUploadTask.getFileLock();
            try {
                fileLock.lock();
                boolean success = proxy.updateFileState(fileId, fileUploadTask.getState());
                if (success){
                    successFileList.add(fileId);
                }
            }
            catch (Exception e){
                getLog().error(e.getMessage());
            }
            finally {
                fileLock.unlock();
            }
        }
        for(String s:successFileList){
            retryUpdateStateSet.remove(s);
        }
        retryLock.unlock();
    }




    public boolean fileUploadTaskExist(String fileId){
         return fileUploadTaskMap.get(fileId)!=null;
    }

    public boolean storeFileChunk(String fileId, int chunkIndex,MultipartFile multipartFile){

        FileUploadTask fileUploadTask = fileUploadTaskMap.get(fileId);
        if (fileUploadTask == null){
            return false;
        }
        fileUploadTask.getFileLock().lock();
        if (fileUploadTask.getState() == STATUS_FILE_CANCELED){
            fileUploadTask.getFileLock().unlock();
            return false;
        }
        if (fileUploadTask.getTotalChunks() <= chunkIndex){return false;}
        String path = tempDirPath+"/"+fileId+"/"+chunkIndex;
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(path));
            outputStream.write(multipartFile.getBytes());

            int count = fileUploadTask.getUploadedChunkCount()+1;
            fileUploadTask.setUploadedChunkCount(count);
            if (count>=fileUploadTask.getTotalChunks()){
                boolean res = getThisProxy().updateFileState(fileId,STATUS_FILE_PROCESSING);
                if (!res){
                    pushToRetryUpdateStateQueue(fileId);
                }

                fileUploadTask.setState(STATUS_FILE_PROCESSING);
            }
            return true;
        }
        catch (IOException e){
            getLog().error(e.getMessage());
            return false;
        }
        finally {
            fileUploadTask.getFileLock().unlock();
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    getLog().error(e.getMessage());
                }
            }
        }
    }


    private String generateFileId(String uploader,String fileName){
        return UUID.nameUUIDFromBytes((UUID_GENERATE_PREFIX+uploader+":"+fileName).getBytes()).toString().replaceAll("-","");
    }

    @Transactional
    public Result<String> createCatalogFileUploadTask(User user, String fileName, String originalFileName,int chunkNums){
        String fileId = generateFileId(user.getUsername(),fileName);
        FileUploadTask fileUploadTask = fileUploadTaskMap.get(fileId);
        Result<String> result = new Result();
        if (fileUploadTask!=null){
            result.setStatus(0);
            result.setMsg("Dataset exist!");
            return result;
        }



        UploadedFileExample uploadedFileExample = new UploadedFileExample();
        uploadedFileExample.createCriteria().andFileidEqualTo(fileId).andDeletedEqualTo(false);

        UploadedFile uploadedFile = selectFirstByExample(uploadedFileExample);
        if (uploadedFile!=null){
            result = new Result();
            result.setStatus(0);
            result.setMsg("Dataset exists!");
            return result;
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername());
        User curUser = userService.selectFirstByExample(userExample);

        int uploaderId = curUser.getUid();
        uploadedFile = new UploadedFile();
        uploadedFile.setFileid(fileId);
        uploadedFile.setFilename(fileName);
        uploadedFile.setCategory(0);
        uploadedFile.setUploader(uploaderId);
        uploadedFile.setFilestatus(STATUS_FILE_RECEIVING);
        uploadedFile.setOriginalfilename(originalFileName);
        uploadedFile.setDeleted(false);
        boolean success = insert(uploadedFile)>0;
        String tempPath = tempDirPath+"/"+fileId;
        File file = new File(tempPath);
        if (file.exists()){
            deleteDirFiles(tempPath);
        }
        else {
            success &= file.mkdirs();
        }
        if (!success){
            result.setStatus(0);
            result.setMsg("Unable to create upload task");
        }
        else {
            fileUploadTask = new FileUploadTask();
            fileUploadTask.setOriginalFileName(originalFileName);
            fileUploadTask.setTotalChunks(chunkNums);
            fileUploadTask.setState(STATUS_FILE_RECEIVING);
            fileUploadTaskMap.put(fileId,fileUploadTask);
            result.setStatus(1);
            result.setData(fileId);
        }

        return result;
    }


    @Transactional
    public boolean deleteFile(String fileId){
        FileUploadTask fileUploadTask = fileUploadTaskMap.get(fileId);

        if (fileUploadTask!=null){
            fileUploadTask.getFileLock().lock();
            if (fileUploadTask.getState()==STATUS_FILE_PROCESSING){
                fileUploadTask.getFileLock().unlock();
                cancelUploadTask(fileId);
                return false;
            }
            else if (fileUploadTask.getState()==STATUS_FILE_RECEIVING){
                getThisProxy().cancelUploadTask(fileId);
                fileUploadTask.getFileLock().unlock();
                return true;
            }
            else {
                UploadedFileExample uploadedFileExample = new UploadedFileExample();
                uploadedFileExample.createCriteria().andFileidEqualTo(fileId);
                UploadedFile uploadedFile = new UploadedFile();
                uploadedFile.setDeleted(true);
                updateByExampleSelective(uploadedFile,uploadedFileExample);
                fileUploadTaskMap.remove(fileId);
                return true;
            }
        }


        UploadedFileExample uploadedFileExample = new UploadedFileExample();
        uploadedFileExample.createCriteria().andFileidEqualTo(fileId).andDeletedEqualTo(false);
        UploadedFile uploadedFile = selectFirstByExample(uploadedFileExample);
        if (uploadedFile==null){
            return true;
        }
        uploadedFile = new UploadedFile();
        uploadedFile.setDeleted(true);
        return updateByExampleSelective(uploadedFile,uploadedFileExample)>0;

    }

    @Transactional
    public void cancelUploadTask(String fileId){
        FileUploadTask fileUploadTask = fileUploadTaskMap.get(fileId);
        if (fileUploadTask==null){
            return;
        }
        fileUploadTask.getFileLock().lock();

        if (fileUploadTask.getState()!=STATUS_FILE_RECEIVING){
            fileUploadTask.getFileLock().unlock();
            return;
        }

        fileUploadTask.setState(STATUS_FILE_CANCELED);
        fileUploadTask.getFileLock().unlock();
        fileUploadTaskMap.remove(fileId);
        UploadedFileExample uploadedFileExample = new UploadedFileExample();
        uploadedFileExample.createCriteria().andFileidEqualTo(fileId);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setDeleted(true);
        updateByExampleSelective(uploadedFile,uploadedFileExample);
    }

    private void deleteDirFiles(String path){

        File dir = new File(path);
        if (!dir.exists()){return;}
        for(File file:dir.listFiles()){
            file.delete();
        }
    }


    public boolean areAllChunksReceived(String fileId){
        FileUploadTask fileUploadTask = fileUploadTaskMap.get(fileId);
        return fileUploadTask!=null && fileUploadTask.getTotalChunks() <= fileUploadTask.getUploadedChunkCount();
    }


    @Transactional
    public boolean updateFileState(String fileId,int state){
        UploadedFileExample uploadedFileExample = new UploadedFileExample();
        uploadedFileExample.createCriteria().andFileidEqualTo(fileId);
        UploadedFile uploadedFile = selectFirstByExample(uploadedFileExample);
        if (uploadedFile == null){return false;}
        uploadedFile.setFilestatus(state);
        int res = updateByExample(uploadedFile,uploadedFileExample);
        return res == 1;
    }


    public void pushToRetryUpdateStateQueue(String fileId){
        retryLock.lock();
        retryUpdateStateSet.add(fileId);
        retryLock.unlock();
    }

    private void merge(OutputStream target,InputStream in) throws IOException {
        byte[] buffer = new byte[IO_BUFFER_SIZE];
        int read = -1;
        while ((read = in.read(buffer))!=-1){
            target.write(buffer,0,read);
        }
    }




    private void onProcessDone(String fileId,int updateState){
        FileUploadTask fileUploadTask = fileUploadTaskMap.get(fileId);
        retryLock.lock();
        retryUpdateStateSet.remove(fileId);
        retryLock.unlock();
        fileUploadTask.getFileLock().lock();
        boolean updateSuccess = updateFileState(fileId,updateState);
        fileUploadTask.getFileLock().unlock();
        if (!updateSuccess){
            fileUploadTask.setState(STATUS_FILE_READY);
            pushToRetryUpdateStateQueue(fileId);
        }
        else {
            fileUploadTaskMap.remove(fileId);
        }
        fileUploadTaskMap.remove(fileId);
    }


    @Async
    public void asyncProcessFile(String fileId){
        FileUploadTask fileUploadTask = fileUploadTaskMap.get(fileId);
        if (fileUploadTask == null){return;}
        String originalFileFormat = fileUploadTask.getOriginalFileName();
        String format = originalFileFormat.substring(originalFileFormat.lastIndexOf(".")+1);
        String mergedFilePath = tempDirPath+"/"+fileId+"/merged";
        File chunksDir = new File(tempDirPath+"/"+fileId);
        File[] chunks = chunksDir.listFiles();
        Arrays.sort(chunks,(f1,f2)->{
            int f1Index = Integer.parseInt(f1.getName());
            int f2Index = Integer.parseInt(f2.getName());
            return f1Index-f2Index;
        });
        OutputStream targetOutputStream = null;
        InputStream in = null;
        boolean mergeSuccess = true;
        try {
            targetOutputStream = new BufferedOutputStream(new FileOutputStream(mergedFilePath));
            for(File chunk:chunks){
                in = new BufferedInputStream(new FileInputStream(chunk));
                merge(targetOutputStream,in);
                in.close();
            }
        }
        catch (IOException e){
            mergeSuccess = false;
        }
        finally {
            if (targetOutputStream!=null){
                try {
                    targetOutputStream.close();
                } catch (IOException e) {
                    getLog().error(e.getMessage());
                }
            }
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    getLog().error(e.getMessage());
                }
            }
        }

        if (!mergeSuccess){
            // onProcessDone(fileId,STATUS_FILE_PROCESS_FAILED);
            UploadedFileExample uploadedFileExample = new UploadedFileExample();
            uploadedFileExample.createCriteria().andFileidEqualTo(fileId);
            deleteByExample(uploadedFileExample);
            deleteDirFiles(tempDirPath+"/"+fileId);
            return;
        }

        String outputDir = readyFilesDir+"/"+fileId;
        File dir = new File(outputDir);
        if (!dir.exists()){
            dir.mkdirs();
        }
        else {
            deleteDirFiles(readyFilesDir+"/"+fileId);
        }
        boolean processResult = fileProcessService.transferDatasetToOctree(mergedFilePath,format,readyFilesDir+"/"+fileId);
        int updateState = processResult?STATUS_FILE_READY:STATUS_FILE_PROCESS_FAILED;
        onProcessDone(fileId,updateState);
        deleteDirFiles(tempDirPath+"/"+fileId);
    }

}
