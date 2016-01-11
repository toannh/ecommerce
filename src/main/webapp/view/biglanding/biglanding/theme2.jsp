<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld" %>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<link href="${staticUrl}/biglanding/css/landing-big.css" rel="stylesheet" />
<div class="lb-banner">
    <img src="${landing.heartBanner}" alt="Landing big" />
        <div class="lb-caption">
        <div class="container">
            <div class="timer">
                <div class="timer-area">
                    <ul id="countdown-landing">
                        <li>
                            <span class="days big">00</span>
                            <p class="timeRefDays small">Ngày</p>
                        </li>
                        <li>
                            <span class="hours big">00</span>
                            <p class="timeRefHours small">Giờ</p>
                        </li>
                        <li>
                            <span class="minutes big">00</span>
                            <p class="timeRefMinutes small">Phút</p>
                        </li>
                        <li>
                            <span class="seconds big">00</span>
                            <p class="timeRefSeconds small">Giây</p>
                        </li>
                    </ul>
                </div> <!-- end timer-area -->
            </div><!-- timer -->
        </div><!-- /container -->
    </div><!-- /lb-caption -->
</div><!-- /lb-banner -->

<div class="bg-white">
    <div class="container">
        <div class="bground">
            <div class="lb-page">
                <c:forEach items="${bigLandingCates}" var="bigLandingCate">
                    <c:set var="bigLandingCate" value="${bigLandingCate}" scope="request" />
                    <c:if test="${bigLandingCate.template=='template1'}">
                        <jsp:include page="/view/biglanding/biglandingbox/theme2/template1.jsp"></jsp:include>
                    </c:if>

                    <c:if test="${bigLandingCate.template=='template2'}">
                        <jsp:include page="/view/biglanding/biglandingbox/theme2/template2.jsp"></jsp:include>
                    </c:if>
                    <c:if test="${bigLandingCate.template=='template3'}">
                        <jsp:include page="/view/biglanding/biglandingbox/theme2/template3.jsp"></jsp:include>
                    </c:if>
                    <c:if test="${bigLandingCate.template=='template4'}">
                        <jsp:include page="/view/biglanding/biglandingbox/theme2/template4.jsp"></jsp:include>
                    </c:if>

                </c:forEach>	

                
            </div><!-- /lb-page -->
        </div><!-- /bground -->
    </div><!-- /container -->
    <div class="internal-text">
    </div><!-- /internal-text -->
</div><!-- bg-white -->


