<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld" %>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>

                    <c:set var="landing" value="${bigLanding}" scope="request" />
                    <c:if test="${landing.landingTemplate=='template1'}">
                        <jsp:include page="/view/biglanding/biglanding/theme1.jsp"></jsp:include>
                    </c:if>
                    <c:if test="${landing.landingTemplate=='template2'}">
                        <jsp:include page="/view/biglanding/biglanding/theme2.jsp"></jsp:include>
                    </c:if>




