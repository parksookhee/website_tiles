package com.site.commons.lib.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


public class FileUpload {

    /**
     * @brief   업로드 처리 메서드  
     * @details 지정된 물리적 경로(savePath)에 파일을 저장하고 그 결과를 JSONObject로 리턴합니다.
     * @param   MultipartHttpServletRequest MultipartHttpServletRequest
     * @param   HttpServletResponse         HttpServletResponse
     * @param   String                      파일이 저장될 서버의 물리적 경로, 읽기 쓰기가 모두 가능해야 합니다.
     * @param   Boolean                     파일을 저장할 때 임의의 유일 이름으로 변경한 뒤 돌려주는 여부 설정. true 면 유일 이름 사용, false면 원래 이름 사용
     * @param   String                      파일을 삭제하는 기능을 넣고 싶을 때, 해당 함수를 호출하는 문자열을 연결한다.
     * @return  JSONObject
     */
    public static JSONObject uploadProcess(MultipartHttpServletRequest request, HttpServletResponse response, String savePath) {
        return uploadProcess(request, response, savePath, false);
    }
    public static JSONObject uploadProcess(MultipartHttpServletRequest request, HttpServletResponse response, String savePath, Boolean useUniqueName) {
        return uploadProcess(request, response, savePath, useUniqueName, "");
    }
    public static JSONObject uploadProcess(MultipartHttpServletRequest request, HttpServletResponse response, String savePath, Boolean useUniqueName, String callbackDeleteFunc) {
        JSONObject JSONfiles = new JSONObject();
        
        //1. 파일 이름의 목록을 보관하기 위한 iterator 생성
         Iterator<String> itr =  request.getFileNames();
         MultipartFile mpf = null;
         
         JSONArray JSONfilesArray = new JSONArray();
         //2. 개별 파일을 처리한다
         while(itr.hasNext()){
             // 원본 파일 이름과 유일 파일 이름을 보관할 변수 선언
             String originalFileName = "";
             String uniqueFileName = "";
 
             //2.1 다음 파일 정보를 얻는다
             mpf = request.getFile(itr.next());
             originalFileName = mpf.getOriginalFilename();
             // 2.1.1 이름이 변경될 때를 대비해서 확장자를 얻는다.
             String fileExtension = FilenameUtils.getExtension(originalFileName); // File확장자를 가져와 UUID에 붙여줍니다.
             uniqueFileName = (useUniqueName) ? UUID.randomUUID().toString() + "." + fileExtension : originalFileName;
             //2.2 파일 메타 정보를 리턴값에 포함시킨다
             JSONObject JSONFileMeta = new JSONObject();
             JSONFileMeta.put("name", originalFileName);
             JSONFileMeta.put("type", mpf.getContentType());
             JSONFileMeta.put("nameUnique", uniqueFileName);
             JSONFileMeta.put("size", mpf.getSize()/1024);
             JSONFileMeta.put("deleteUrl", callbackDeleteFunc);
             JSONFileMeta.put("fileExtension", fileExtension);  
             
             try {
                 // 업로드된 파일의 임시 데이터를 실제 파일로 작성한다.            
                 // 업로드될 파일의 경로를 지정해 주어야 하고, 쓰기 권한이 미리 주어져야만 한다.
                 
                 //FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(savePath + "/" + uniqueFileName));
                 mpf.transferTo(new File(savePath , uniqueFileName));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             //2.3 파일의 메타 정보를 배열에 추가한다
             JSONfilesArray.add(JSONFileMeta);
         }
         
        // 리턴값의 내용은 다음 샘플과 같은 배열로 이루어져 있다 
        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
         
        //3 JSON 리턴을 위해 배열의 상위에 키를 추가한다.
        JSONfiles.put("files", JSONfilesArray);
         
        //4 JSON 데이터를 문자열로 바꾸어 리턴한다
        return JSONfiles;
    }
    
    /**
     * @brief   다운로드 처리 메서드  
     * @details 지정된 물리적 경로(savePath)의 파일을 읽어서 다운로드 처리를 합니다
     * @param   HttpServletRequest      HttpServletRequest
     * @param   HttpServletResponse     HttpServletResponse
     * @param   String                  서버의 물리적 경로
     * @param   String                  서버의 물리적 파일 이름
     * @return  없음
     */
    public static void getFile(HttpServletRequest request, HttpServletResponse response, String savePath, String fileName) throws IOException{
        //1 경로를 합쳐서 전체 경로를 만든다
        String fullPath = savePath + "/" + fileName;
        
        //2 파일 객체를 생성
        File file = new File(fullPath);

        response.setContentType("application/download; utf-8");
        response.setContentLength((int)file.length());
        
        //3 요청한 브라우저가 IE이면 파일 이름의 인코딩 방법이 다르다
        String userAgent = request.getHeader("User-Agent");
        boolean ie = userAgent.indexOf("MSIE") > -1;
             
        if(ie){
            fileName = URLEncoder.encode(file.getName(), "utf-8");
        } else {
            fileName = new String(file.getName().getBytes("utf-8"),"iso-8859-1");
        }
        
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        OutputStream out = response.getOutputStream();
        
        //4 스트림에 파일을 기록한다. response에 대한 스트림이므로 바로 파일 다운로드로 이어진다.
        FileInputStream fis = null;
        try {
            if ( file.exists() ) {
                fis = new FileInputStream(file);
                FileCopyUtils.copy(fis, out);
            }
            else {
                out.write("".getBytes());
            }
         } catch(Exception e){
            e.printStackTrace();
         }finally{
            if(fis != null){
                try{
                    fis.close();
                }catch(Exception e){}
            }
         }
         out.flush();
     }

    /**
     * @brief   다운로드 처리 메서드  ( 경로 및 파일명이 분리되어 있는 경우 )
     * @details 지정된 물리적 경로(savePath)의 파일을 읽어서 다운로드 처리를 합니다
     * @param   HttpServletRequest      HttpServletRequest
     * @param   HttpServletResponse     HttpServletResponse
     * @param   String                  서버의 물리적 경로
     * @param   String                  서버의 물리적 파일 이름
     * @param   String                  사용자의 PC에 저장될 파일명 
     * @return  없음
     */
    public static void getFile(HttpServletRequest request, HttpServletResponse response, String savePath, String fileName, String saveFileName) throws IOException{
 
        //1 경로를 합쳐서 전체 경로를 만든다
        StringBuffer buffer = new StringBuffer();
        buffer.append(savePath);
        buffer.append("/");
        buffer.append(fileName);
        
        String fullPath = buffer.toString();
        
        //2 파일 객체를 생성
        File file = new File(fullPath);
        
        // 파일을 읽어올 FileInputStream과 출력할 OutputStream 을 생성한다. 
        OutputStream out = response.getOutputStream();
        FileInputStream fis = new FileInputStream(file);
        
        try {

            // 다운로드를 위한 속성 설정 ( 파일다운로드 / 파일 사이즈 ) 
            response.setContentType("application/download; utf-8");
            response.setContentLength((int)file.length());
            
            //3 요청한 브라우저가 IE이면 파일 이름의 인코딩 방법이 다르다
            String userAgent = request.getHeader("User-Agent");
            boolean ie = userAgent.indexOf("MSIE") > -1;
             
            // 브라우저가 IE인지 확인하여 파일명 지정 형태 변경 
            if(ie){
                saveFileName = URLEncoder.encode(saveFileName, "utf-8");
            } else {
                saveFileName = new String(saveFileName.getBytes("utf-8"),"iso-8859-1");
            }
            
            // 응답헤더에 첨부파일명 전달
            response.setHeader("Content-Disposition", "attachment; filename=\"" + saveFileName + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");
            
            // 임시사용을 위한 바이트 배열 선언 
            byte[] outByte = new byte[4096];
            
            // 선언된 바이트 배열 사이즈만큼씩 읽어서 외부 출력으로 보냄.
            //4 스트림에 파일을 기록한다. response에 대한 스트림이므로 바로 파일 다운로드로 이어진다.
            while(fis.read(outByte, 0, 4096) != -1){
                out.write(outByte, 0, 4096);
            }
        
        } catch (Exception e) {
            throw new IOException();
        } finally {
            out.flush();
            out.close();
            fis.close();
        }

     }
    
    /**
     * @brief   다운로드 처리 메서드  ( 경로 및 파일명이 포함되어 있는 경우  )
     * @details 지정된 물리적 경로(savePath)의 파일을 읽어서 다운로드 처리를 합니다
     * @param   HttpServletRequest      HttpServletRequest
     * @param   HttpServletResponse     HttpServletResponse
     * @param   String                  서버의 물리적 경로( 저장된 파일명까지 포함 )
     * @param   String                  사용자의 PC에 저장될 파일명 
     * @return  없음
     */
    public static void getFilePathOnly(HttpServletRequest request, HttpServletResponse response, String savePath, String saveFileName) throws IOException{
 
        //1 경로를 합쳐서 전체 경로를 만든다 ( 경로에 파일명 포함 )
        String fullPath = savePath;
        
        //2 파일 객체를 생성
        File file = new File(fullPath);
        
        // 파일을 읽어올 FileInputStream과 출력할 OutputStream 을 생성한다. 
        OutputStream out = response.getOutputStream();
        FileInputStream fis = new FileInputStream(file);
        
        try {

            // 다운로드를 위한 속성 설정 ( 파일다운로드 / 파일 사이즈 ) 
            response.setContentType("application/download; utf-8");
            response.setContentLength((int)file.length());
            
            //3 요청한 브라우저가 IE이면 파일 이름의 인코딩 방법이 다르다
            String userAgent = request.getHeader("User-Agent");
            boolean ie = userAgent.indexOf("MSIE") > -1;
             
            // 브라우저가 IE인지 확인하여 파일명 지정 형태 변경 
            if(ie){
                saveFileName = URLEncoder.encode(saveFileName, "utf-8");
            } else {
                saveFileName = new String(saveFileName.getBytes("utf-8"),"iso-8859-1");
            }
            
            // 응답헤더에 첨부파일명 전달
            response.setHeader("Content-Disposition", "attachment; filename=\"" + saveFileName + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");
            
            // 임시사용을 위한 바이트 배열 선언 
            byte[] outByte = new byte[4096];
            
            // 선언된 바이트 배열 사이즈만큼씩 읽어서 외부 출력으로 보냄.
            //4 스트림에 파일을 기록한다. response에 대한 스트림이므로 바로 파일 다운로드로 이어진다.
            while(fis.read(outByte, 0, 4096) != -1){
                out.write(outByte, 0, 4096);
            }
        
        } catch (Exception e) {
            throw new IOException();
        } finally {
            out.flush();
            out.close();
            fis.close();
        }
    }    
    
    /**
     * @brief   파일 삭제 메서드
     * @details 지정된 물리적 경로(savePath)의 파일을 삭제합니다.
     * @param   String  서버의 물리적 경로( 저장된 파일명까지 포함 )
     * @param   String  사용자의 PC에 저장된 파일명 
     * @return  Int     0 : 성공 / 1 : 실패 / 2 : 파일이 존재하지 않음                
     */
    public static int deleteFile(String savePath, String saveFileName)
    {
        File file = new File(savePath + saveFileName);
        if(file.exists()) 
        {
            boolean delFlag = file.delete();
            
            if(delFlag) {
                return 0;
            } else {
                return 1;
            }                                            
        } else 
        {
            return 2;
        }
    }
    
    /**
     * @brief   파일 이동 메서드
     * @details 
     * @param   String  서버의 물리적 경로( 저장된 파일명까지 포함 )
     * @param   String  사용자의 PC에 저장된 파일명 
     * @return  Int     0 : 성공 / 1 : 실패 / 2 : 파일이 존재하지 않음                
     * @details 지정된 파일을 이동합니다. 대상 폴더는 1 단계까지만 자동으로 생성해 줍니다. tempFileName이 saveFile로 이름이 변경됩니다.
     * @param   String tempPath  서버의 물리적 경로( 저장된 파일명까지 포함 )
     * @param   String tempFileName  물리적 파일 이름 
     * @param   String destinationPath 대상 경로 
     * @param   String saveFile 대상 파일 이름 
     * @return  없음                
     * @exception  IOException                
     */
    public static void moveFile(String tempPath, String tempFileName, String destinationPath, String saveFile) throws IOException
    {
        File sourceFile = new File(tempPath + "/" + tempFileName);
        File targetFile = new File(destinationPath + "/" + saveFile);
         
        moveFile(sourceFile, targetFile);
    }
    
    /**
     * @brief   파일 이동 메서드
     * @details 지정된 파일을 이동합니다. 대상 폴더는 1 단계까지만 자동으로 생성해 줍니다. tempFileName이 saveFile로 이름이 변경됩니다.
     * @param   File sourceFile  원본 파일 객체, 실제 존재하는 파일이어야 합니다
     * @param   File targetFile  대상 파일 객체, 실제 존재하지 않는 파일이어야 합니다. 같은 이름의 파일이 있을 경우 대상이 삭제됩니다.
     * @return  없음                
     * @exception  IOException                
     */
    public static void moveFile(File sourceFile, File targetFile) throws IOException
    {
        // 대상이 폴더이면
        if (!targetFile.isFile())
        {
            // 상위 폴더를 만듭니다
            File parentFile = new File (targetFile.getParent());
            if ( !parentFile.exists())
            {
                parentFile.mkdir();
            }
        }
     
        // 소스와 원본이 모두 존재하면
        if ( sourceFile.exists() && targetFile.exists())
        {
            if (!sourceFile.getAbsolutePath().equals(targetFile.getAbsolutePath()))
            {
                // 대상 파일을 삭제합니다
                targetFile.delete();
            }
        }
        // 파일의 이름을 변경합니다 -> 이동 기능
        sourceFile.renameTo( targetFile);
    }

    /**
     * @brief   파일 압축 메서드
     * @details 지정된 디렉토리에 지정된 파일들의 목록을 던지면 대상 디렉토리로 압축해서 던져준다.
     * SAMPLE
     *      ArrayList<String> files = new ArrayList<String>();
     *      files.add("한글.jpg");
     *      files.add("608f0d36-6678-4c76-b4af-729e061829e2.pdf");
     *      String resultFile = FileUpload.zip("/data/upload/sample", files, "/data/upload/sample", "", true);
     *          or
     *      String resultFile = FileUpload.zip("/data/upload/sample", files, "/data/upload/sample", "20130501", false);
     *
     * @param   String          sourceDirectory     원본이 존재하는 디렉토리
     * @param   List<String>    sourceFiles         대상 파일들의 List (ArrayList등)
     * @param   String          targetDirectory     압축 파일을 저장할 디렉토리
     * @param   String          targetFilename      압축 파일 이름, useUniqueName이 true인 경우 자동으로 UUID 파일 이름으로 리턴한다.
     * @return  String          압축된 결과 파일의 이름
     * @exception  IOException
     */
//    public static String zip(String sourceDirectory, List<String> sourceFiles, String targetDirectory, String targetFilename, Boolean useUniqueName ) throws IOException {
//        // 압축 결과 파일이 비어있거나 useUniqueName가 true인 경우 UUID로 이름을 만든다.
//        if (useUniqueName || targetFilename == "") targetFilename = UUID.randomUUID().toString() + ".zip";
//
//        // 대상 디렉토리와 결과 파일의 File 객체를 생성한다.
//        File destinationDirectory = new File(targetDirectory);
//        File zippedFile = new File (destinationDirectory, targetFilename);
//        // 파일이 없으면 새로 생성한다.
//        if ( !zippedFile.exists() ) zippedFile.createNewFile();
//        // 압축을 시작한다. charsetName은 UTF-8로 설정한다.
//        zip(sourceDirectory, sourceFiles, new FileOutputStream(zippedFile), "UTF-8");
//        // 결과를 리턴
//        return zippedFile.getName();
//    }

//    /**
//     * @brief   파일 압축 메서드
//     * @details 지정된 디렉토리에 지정된 파일들의 목록을 던지면 대상 디렉토리로 압축해서 던져준다.
//     * @param   String          sourceDirectory     원본이 존재하는 디렉토리
//     * @param   List<String>    sourceFiles         대상 파일들의 List (ArrayList등)
//     * @param   OutputStream    os                  대상 스트림
//     * @param   String          charsetName         문자 인코딩 셋
//     * @return  없음
//     * @exception  IOException
//     */
//    public static void zip(String sourceDirectory, List<String> sourceFiles, OutputStream os, String charsetName) throws IOException
//    {
//        ZipArchiveOutputStream zos = new ZipArchiveOutputStream(os);
//        zos.setEncoding(charsetName);
//        FileInputStream fis ;
//
//        int length ;
//        ZipArchiveEntry ze ;
//        byte [] buf = new byte[8 * 1024];
//        String name ;
//
//        Stack<File> stack = new Stack<File>();
//        for(String sourceFile : sourceFiles)
//        {
//            File file = new File(sourceDirectory + "/" + sourceFile);
//            stack.push(file);
//        }
//
//        while ( !stack.isEmpty() ){
//            File f = stack.pop();
//
//            name = toPath(f.getParentFile(), f);
//            ze = new ZipArchiveEntry(name);
//            zos.putArchiveEntry(ze);
//            fis = new FileInputStream(f);
//            while ( (length = fis.read(buf, 0, buf.length)) >= 0 ){
//                zos.write(buf, 0, length);
//            }
//            fis.close();
//            zos.closeArchiveEntry();
//        }
//        zos.close();
//    }

    /**
     * @brief   파일 압축 메서드에서 사용하는 결과 디렉토리를 리턴하는 메서드
     * @details 결과를 저장할 경로 리턴
     * @param   File    root        상위 경로
     * @param   File    dir         파일 경로
     * @return  String  경로 문자열
     */
    private static String toPath(File root, File dir){
        String path = dir.getAbsolutePath();
        path = path.substring(root.getAbsolutePath().length()).replace(File.separatorChar, '/');
        if ( path.startsWith("/")) path = path.substring(1);
        if ( dir.isDirectory() && !path.endsWith("/")) path += "/" ;
        return path ;
    }
}
