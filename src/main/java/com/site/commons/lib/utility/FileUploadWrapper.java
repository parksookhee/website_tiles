package com.site.commons.lib.utility;

import com.site.commons.lib.utility.FileUpload;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUploadWrapper extends FileUpload {
	
	/**
	 * @brief downFile - 파일 다운로드 부라우저 인코딩 처리
	 * @details
	 * @param response HttpServletResponse
	 * @param request HttpServletRequest
	 * @param uploadRootPath 업로드될 루트 패스
	 * @param uploadSubPath 업로드될 서브 패스
	 * @param uploadFileName 저장된 파일 이름
	 * @param downloadFilename 다운로드 될 파일 이름
	 * @throws java.io.IOException
	 */
	public static void downFile(HttpServletResponse response,HttpServletRequest request, String uploadRootPath,String uploadSubPath, String uploadFileName, String downloadFilename) throws IOException{

		File file = new File(uploadRootPath+ uploadSubPath + uploadFileName);

		if (!file.isFile()) {
			return ;
		}


		if (file.exists()) {
			String browser=getBrowser(request);

			if ("Opera".equals(browser)){
			    response.setContentType("application/octet-stream;charset=UTF-8");
			}else{
				response.setContentType("application/x-msdownload");
			}

			response.setContentLength((int)file.length());
			response.setHeader(	"Content-Disposition",	"attachment;filename=\""+ getDownFileNames(browser,downloadFilename) + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");

			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(file));
				out = new BufferedOutputStream(response.getOutputStream());

				FileCopyUtils.copy(in, out);
				out.flush();
			} catch (Exception ex) {
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ignore) {

					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (Exception ignore) {
					}
				}
			}
		}
	}


  //***************************************************파일 규칙 시작*********************************************//
    public static final String FILE_SEPARATOR			= System.getProperty("file.separator"); //폴더 구분자

    /**
	 * createFolder - 전달된 파라미터대로 디렉토리 생성
	 * @param realFolder : 생성될 폴더명
	 * @throws java.io.IOException
	 */
	public static boolean createFolder(String realFolder)
    {
		boolean bReturn = true;

        try{
        	File dir = new File(realFolder);
        	
        	if(!dir.exists())
        	{
        		dir.mkdirs();
        		
        		try
        		{
        			String cmd = "chmod 777 " + realFolder; 
        			Runtime rt = Runtime.getRuntime(); 
        			Process p = rt.exec(cmd); 
        			p.waitFor(); 
        		}
        		
        		catch(Exception e){}
        	}
        	
        	bReturn = true;
        }
        catch (Exception e){
        	bReturn = false;
            e.printStackTrace();
        }
        
        return bReturn;
    }
  	
  	/**
  	 * getServerSubPath - 파일 저장 디랙토리 생성(연월/일)
  	 * @return String ex)2013/02/
  	 */
	public static String getServerSubPath() {
  		//return new SimpleDateFormat("yyyy"+FILE_SEPARATOR+"MM"+"dd", Locale.getDefault()).format(new Date())+FILE_SEPARATOR;
		return new SimpleDateFormat("yyyy"+FILE_SEPARATOR+"MM", Locale.getDefault()).format(new Date())+FILE_SEPARATOR;
  	}

	/**
	 * getBrowser - 브라우저 정보 체크
	 * @param request
	 * @return
	 */
	public static String getBrowser(HttpServletRequest request) {
		
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		} else if (header.indexOf("Safari") > -1) {
			return "Safari";
		}
		return "Firefox";
	}
	
	/**
	 * getDownFileNames - 다운로드 파일 생성
	 * @param browser
	 * @param fileName
	 * @return
	 */
	public static String getDownFileNames(String browser,String fileName) 
	{
		if ( fileName == null || fileName.equals("") ) {
			fileName = "UnKnownFileName";
		}
	
		String resultName = "";
	
		try{
			// Explorer
			if ( browser.indexOf("MSIE") != -1 ) { 
				resultName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
			}
			// Opera
			else if ( browser.indexOf("Opera") != -1 ) {
				resultName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			}
			// Chrome
			else if ( browser.indexOf("Chrome") != -1 ) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < fileName.length(); i++) {
					char c = fileName.charAt(i);
					if (c > '~') {
						sb.append(URLEncoder.encode("" + c, "UTF-8"));
					} else {
						sb.append(c);
					}
				}
				resultName = sb.toString();
			}
			// Safari
			else if ( browser.indexOf("Safari") != -1 ) {
				resultName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
			}
			// FireFox
			else if ( browser.indexOf("Firefox") != -1 ) {
				resultName = new String( fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			// Other
			else{
				resultName = new String( fileName.getBytes("EUC-KR"), "ISO-8859-1");
			}
		} catch (Exception ex) {
			resultName = fileName; 
		}
		return resultName;
	}
	

  	//***************************************************파일 규칙 끝*********************************************//
    
	
	/**
	 * 
	 * @brief 파일 복사
	 * @details 여러 파일들을 복사한다.
	 * @param String
	 *            root : 저장 루트 (영업관리는 /data/upload/marketing)
	 * @param ArrayList
	 *            <HashMap<String, String>> files : 복사할 파일들의 경로와 파일명
	 * @return ArrayList<HashMap<String, String>> : 복사 된 파일들의 경로와 파일명
	 */
	public static ArrayList<HashMap<String, String>> copyFiles(String root,
			ArrayList<HashMap<String, String>> files) throws Exception {
		ArrayList<HashMap<String, String>> outFiles = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> outFile = null;
		// 날짜에 맞는 디렉토리 생성
		String outPath = makeDateDir(root);

		StringBuffer inBf = new StringBuffer();
		StringBuffer outBf = new StringBuffer();
		for (HashMap<String, String> file : files) {
			outFile = new HashMap<String, String>();
			outFile.put("path", outPath);
			outFile.put("show_file", file.get("show_file"));
			// timestamp + 파일명
			String fileExt = StringUtils.getFilenameExtension(file
					.get("show_file"));
			outFile.put("real_file", UUID.randomUUID().toString() + "."
					+ fileExt);
			outFiles.add(outFile);
			// 복사
			copyFile(
					inBf.append(file.get("path")).append("/")
							.append(file.get("real_file")).toString(),
					outBf.append(outPath).append("/")
							.append(outFile.get("real_file")).toString());
			inBf.setLength(0);
			outBf.setLength(0);
		}
		return outFiles;
	}

	/**
	 * 
	 * @brief 날짜 디렉토리 생성
	 * @details 날짜에 맞는 구조의 디렉토리를 생성한다. ( ex - 2014년 4월 28일 - root/2014/4/18 )
	 * @param String
	 *            root : 폴더 루트
	 * @return String : 최종 생성 된 디렉토리 경로
	 */
	public static String makeDateDir(String root) {
		GregorianCalendar cal = new GregorianCalendar();
		StringBuffer bfCopy = new StringBuffer(root);
		String[] dateArr = { String.valueOf(cal.get(Calendar.YEAR)),
				String.valueOf(cal.get(Calendar.MONTH) + 1)};
		// 년도월 디렉토리 생성
		String dir = null;
		File dirExist = null;
		for (int i = 0; i < dateArr.length; i++) {
			dir = bfCopy.append(dateArr[i]).toString();
			dirExist = new File(dir);
			if (!dirExist.exists()) {
				makeDir(dir);
			}
			if (i + 1 < dateArr.length) {
				bfCopy.append("/0");
			}
		}
		return bfCopy.toString();
	}

	/**
	 * 
	 * @brief 디렉토리 생성
	 * @details 디렉토리 생성
	 * @param String
	 *            makeDirName : 생성 할 디렉토리 이름
	 */
	private static void makeDir(String makeDirName) {
		File f1 = new File(makeDirName);
		f1.mkdirs();
	}

	/**
	 * 
	 * @brief 파일 복사
	 * @details 파일 복사
	 * @param String
	 *            inFile : 복사 해 올 원본 파일의 경로+파일명
	 * @param String
	 *            outFile : 복사 할 복사 파일의 경로+파일명
	 */
	private static void copyFile(String inFile, String outFile) {
		try {
			BufferedInputStream fis = new BufferedInputStream(
					new FileInputStream(inFile));
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(outFile));

			int data = 0;
			while ((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
