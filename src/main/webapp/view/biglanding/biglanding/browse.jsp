<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<%@ taglib prefix="url" uri="http://chodientu.vn/url" %>

<c:set var="landing" value="${bigLanding}" scope="request" />
                    <c:if test="${landing.landingTemplate=='template1'}">
                        <jsp:include page="/view/biglanding/biglanding/browseTheme1.jsp"></jsp:include>
                    </c:if>
                    <c:if test="${landing.landingTemplate=='template2'}">
                        <jsp:include page="/view/biglanding/biglanding/browseTheme2.jsp"></jsp:include>
                    </c:if>