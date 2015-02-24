package com.site.commons.lib.utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository("GenerateParameter")
public class GenerateParameter {
    private static final Logger logger = LoggerFactory.getLogger(GenerateParameter.class);
    
    /**
     * @brief Parameter 생성(HTTP 통신용)
     * @details HttpServletRequest로 받은 parameter를 hashmap 에 담으면서 UTF-8로 인코딩함(HTTP 통신시 사용) 
     * @param  HttpServletRequest : view 호출시 받은 HttpServletRequest 객체
     * @return HashMap<String, String>
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> generateEncodeParam(HttpServletRequest request){
        HashMap<String, String> paramMap = new HashMap<String, String>();
        
        try{
            request.setCharacterEncoding("UTF-8");
            Enumeration<String> reqParam = request.getParameterNames();
        
            String name;
            String all[];
            while (reqParam.hasMoreElements()) {
                name = (String) reqParam.nextElement();
                all = request.getParameterValues(name);
                
                for (int i = 0; i < all.length; i++) {
                    if(request.getMethod().equals("GET")){
                        //paramMap.put(name, encodeKorean(new String((all[i].toString()).getBytes("8859_1"), "UTF-8")));        // GET
                        paramMap.put(name, encodeKorean(all[i].toString()));        // GET
                    }
                    else{
                        paramMap.put(name, all[i].toString());      // POST
                    }
                }               
            }
            name = null;
            all = null;
            
            logger.debug("------- Encode Parameter Values -------");
            Iterator<String> iter = paramMap.keySet().iterator();
            String key, value;
            while (iter.hasNext()){
                key = (String)iter.next().toString();
                value = (String)paramMap.get(key); 
                logger.debug("[key]:" + key + " [value]:" + value);
            }
            logger.debug("------- Encode Parameter Values -------");
        }
        catch(UnsupportedEncodingException e){
            logger.debug("generateParam() Error : ", e);
        }
        return paramMap;
    }
    
    /**
     * @brief Parameter 생성 
     * @details HttpServletRequest로 받은 parameter를 hashmap 에 담아서 리턴
     * @param  HttpServletRequest : view 호출시 받은 HttpServletRequest 객체
     * @return HashMap<String, String>
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> generateParam(HttpServletRequest request){
        HashMap<String, String> paramMap = new HashMap<String, String>();
        
        try{
            request.setCharacterEncoding("UTF-8");
            Enumeration<String> reqParam = request.getParameterNames();
        
            String name;
            String all[];
            while (reqParam.hasMoreElements()) {
                name = (String) reqParam.nextElement();
                all = request.getParameterValues(name);
                
                for (int i = 0; i < all.length; i++) {
                    // 2014-04-16 by Evan : jboss의 설정으로 인코딩 제거
                    //paramMap.put(name, new String((all[i].toString()).getBytes("8859_1"), "UTF-8"));
                    paramMap.put(name, all[i].toString());
                }
            }
            name = null;
            all = null;
            
            logger.debug("------- Parameter Values -------");
            Iterator<String> iter = paramMap.keySet().iterator();
            String key, value;
            while (iter.hasNext()){
                key = (String)iter.next().toString();
                value = (String)paramMap.get(key); 
                logger.debug("[key]:" + key + " [value]:" + value);
            }
            logger.debug("------- Parameter Values -------");
        }
        catch(UnsupportedEncodingException e){
            logger.debug("generateParam() Error : ", e);
        }
        return paramMap;
    }
    
    /**
     * @brief 한글 URLEncode 
     * @details  HTTP GET 통신을 위한 한글 URLEncoding
     * @param  strData : parameter로 넘어온 데이터
     * @return String
     */
    @SuppressWarnings("deprecation")
    public String encodeKorean(String strData) throws UnsupportedEncodingException{
        boolean chkKorean = false;
        
        if(strData != null){
            for(int i=0;i<strData.length();i++){
                char c = strData.charAt(i);
                //한글 ( 한글자 || 자음 , 모음 )
                if( ( 0xAC00 <= c && c <= 0xD7A3 ) || ( 0x3131 <= c && c <= 0x318E ) ){
                    chkKorean = true;
                    break;
                }
                else {
                    chkKorean = false;
                }
            }
            
            if(chkKorean){
                strData = URLEncoder.encode(strData);
            }
        }
        
        return strData;
    }
}
