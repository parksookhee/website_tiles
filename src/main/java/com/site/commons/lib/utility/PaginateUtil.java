package com.site.commons.lib.utility;


import java.util.HashMap;

public class PaginateUtil {
	private static int startPage;
	private static int endPage;
	private static int page;
	private static String link;
	
	/**
	 * 
	 * @brief	페이징 
	 * @details	페이징 : 아래 메소드와 같으나, perPage, listSize가 Default로 섧정되어 있음
	 * @param 	String inputLink : 링크
	 * @param	String inputPage : 현재 페이지
	 * @param	int total : 리스트 전체 row수
	 * @return	리턴 타입 (리턴이 없으면 생략)
	 */
	public static HashMap<String, String> paginate(String inputLink, String inputPage, int total) {
		return paginate(inputLink, inputPage, "10", 5, total);
	}
	/**
	 * 
	 * @brief	페이징
	 * @details	페이징
	 * @param 	String inputLink : 링크
	 * @param	String inputPage : 현재 페이지
	 * @param	String inputPerPage : 페이지 당 row수
	 * @param	int listSize : 페이지 버튼 수
	 * @param	int total : 리스트 전체 row수
	 * @return	HashMap<String, String> - showPaginate : 페이징 출력 / showPaginateNoLink : 페이징 출력 - 링크없음 / listCut : DB Query에 사용
	 */
	public static HashMap<String, String> paginate(String inputLink, String inputPage, String inputPerPage, int listSize, int total) {
        link = inputLink+"?page=";
		page = Integer.parseInt(inputPage);
		int perPage = Integer.parseInt(inputPerPage);
		
		int pageCount = (total / perPage) + (total % perPage == 0? 0:1);
		int listCut = (page - 1) * perPage;
		startPage = page - (int)(listSize / 2);
		endPage = startPage + listSize - 1;
		if(startPage<=0) {
			startPage = 1;
		}
		if(endPage>=pageCount) {
			endPage = pageCount;
		}
		
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("showPaginate", showPaginate());
		result.put("showPaginateNoLink", showPaginateNoLink());
		result.put("listCut", String.valueOf(listCut));
		
		return result;
	}

	/**
     * 
     * @brief   페이징 태그
     * @details 페이징 태그
     * @return  String
     */
	private static String showPaginate() {
	    String html = "";
        html += "<div class='paginate'>";
        html += "<a href='"+ link + startPage +"' class='first'>맨처음목록</a>";
        if((page - 1) >= startPage)
            html += "<a href='"+link + (page-1) +"' class='prev'>이전목록</a>";
        else
            html += "<a class='prev'>이전목록</a>";
        for (int i = startPage; i <= endPage; i++) {
            if (i == page)
                html += "<a href='"+link + i +"' class='current'>"+ i +"</a>";
            else
                html += "<a href='"+link + i +"'>"+ i +"</a>";
        }
        if ((page + 1) <= endPage)
            html += "<a href='"+ link + (page+1) +"' class='next'>다음목록</a>";
        else
            html += "<a class='next'>다음목록</a>";
        html += "<a href='"+ link + endPage +"' class='last'>마지막 목록</a>";
        html += "</div>";
        return html;
	}
	
	/**
     * 
     * @brief   페이징 태그 - 페이지번호 제거
     * @details 페이징 태그 - 페이지번호 제거
     * @return  String
     */
    private static String showPaginateNoLink() {
        String html = "";
        html += "<div class='paginate'>";
        html += "<a href='javascript:void(0)' page='" + startPage + "' class='first'>맨처음목록</a>";
        if((page - 1) >= startPage)
            html += "<a href='javascript:void(0)' page='" + (page-1) + "' class='prev'>이전목록</a>";
        else
            html += "<a class='prev'>이전목록</a>";
        for (int i = startPage; i <= endPage; i++) {
            if (i == page)
                html += "<a href='javascript:void(0)' page='" + i + "' class='current'>"+ i +"</a>";
            else
                html += "<a href='javascript:void(0)' page='" + i + "'>"+ i +"</a>";
        }
        if ((page + 1) <= endPage)
            html += "<a href='javascript:void(0)' page='" + (page+1) + "' class='next'>다음목록</a>";
        else
            html += "<a class='next'>다음목록</a>";
        html += "<a href='javascript:void(0)' page='" + endPage + "' class='last'>마지막 목록</a>";
        html += "</div>";
        return html;
    }
}
