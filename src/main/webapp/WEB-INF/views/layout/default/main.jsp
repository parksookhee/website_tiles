<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><tiles:getAsString name="title"/></title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="description" content="" />
	<meta name="keywords" content="" />
	<noscript>
	<link rel="stylesheet" href="/css/5grid/core.css" />
	<link rel="stylesheet" href="/css/5grid/core-desktop.css" />
	<link rel="stylesheet" href="/css/5grid/core-1200px.css" />
	<link rel="stylesheet" href="/css/5grid/core-noscript.css" />
	<link rel="stylesheet" href="/css/style.css" />
	<link rel="stylesheet" href="/css/style-desktop.css" />
	</noscript>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script src="/css/5grid/init.js?use=mobile,desktop,1000px&amp;mobileUI=1&amp;mobileUI.theme=none"></script>
	<!--[if IE 9]><link rel="stylesheet" href="/css/style-ie9.css" /><![endif]-->
</head>

<script>
var gnb = "${gnb}";
$(document).ready(function() {
	$("#header nav li").removeClass("current_page_item");
	$("#header nav li[gnb=" + gnb + "]").addClass("current_page_item");
});
</script>

<body>
	<div id="wrapper">
		<tiles:insertAttribute name="header"/>
		<tiles:insertAttribute name="body"/>
		<tiles:insertAttribute name="footer"/>
	</div>
	<div id="copyright" class="5grid-layout">
		<section>
			<p>&copy; Your Site Name | Images: <a href="http://fotogrph.com/">Fotogrph</a> | Design: <a href="http://templated.co/">TEMPLATED</a></p>
		</section>
	</div>
</body>
</html>