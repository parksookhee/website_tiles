<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!-- paginate -->
<div class="paginate">
<c:if test="${pageInfo.totalPage > 0}">
	<c:if test="${pageInfo.totalPage > 10}">
		<c:if test="${pageInfo.currentPage != 1}">
			<a href="${pageInfo.url}${pageInfo.separator}${pageInfo.pageParam}=1${pageInfo.paramString}" class="first">맨처음목록</a>
		</c:if>
	    <c:if test="${pageInfo.totalPage != 1 && pageInfo.currentPage != 1}">
			<a href="${pageInfo.url}${pageInfo.separator}${pageInfo.pageParam}=${pageInfo.currentPage-1}${pageInfo.paramString}" class="prev">이전목록</a>
	    </c:if>
    </c:if>
    <c:if test="${!empty pageInfo.pages}">
    </c:if>
    <c:forEach items="${pageInfo.pages}" var="page" varStatus="status">
        <c:if test="${pageInfo.currentPage != page}">
            <a href="${pageInfo.url}${pageInfo.separator}${pageInfo.pageParam}=${page}${pageInfo.paramString}">${page}</a>
        </c:if>
        <c:if test="${pageInfo.currentPage == page}">
            <a href="javascript://" class="current"><strong>${page}</strong></a>
        </c:if>
    </c:forEach>
    <c:if test="${!empty pageInfo.pages}">
    </c:if>
    
    <c:if test="${pageInfo.totalPage > 10}">
	    <c:if test="${pageInfo.nextScalePage <= pageInfo.totalPage}">
	        <a href="${pageInfo.url}${pageInfo.separator}${pageInfo.pageParam}=${pageInfo.currentPage+1}${pageInfo.paramString}" class="next">다음목록</a>
	    </c:if>
	    <c:if test="${pageInfo.totalPage > pageInfo.currentPage}">
	    	<a href="${pageInfo.url}${pageInfo.separator}${pageInfo.pageParam}=${pageInfo.totalPage}${pageInfo.paramString}" class="last">마지막목록</a>
	    </c:if>
    </c:if>
</c:if>
</div>
<!-- //paginate -->
