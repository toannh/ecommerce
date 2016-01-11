
<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld" %>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<link href="${staticUrl}/biglanding/css/landing-freeday.css" rel="stylesheet" />
<div class="fd-banner-outer" <c:if test="${landing.background!=null && landing.background!=''}"> style="background: ${landing.background}"</c:if>>
        <div class="fd-banner"> 
            <img src="${landing.heartBanner}" alt="banner" />
        <div class="fd-caption">
            <div class="container">
                <div class="timer">
                    <div class="timer-image">
                        <c:if test="${landing.logoBanner!=null && landing.logoBanner!=''}">
                            <img src="${landing.logoBanner}" alt="Ngày miễn phí" />
                        </c:if>
                    </div>
                    <!--                    <div class="timer-desc">Thời gian còn:</div>
                                        <div class="timer-area">
                                            <ul id="countdown-freeday">
                                                <li>
                                                    <span class="days big">00</span>
                                                    <span class="time-dot">:</span>
                                                    <p class="timeRefDays small">Ngày</p>
                                                </li>
                                                <li>
                                                    <span class="hours big">00</span>
                                                    <span class="time-dot">:</span>
                                                    <p class="timeRefHours small">Giờ</p>
                                                </li>
                                                <li>
                                                    <span class="minutes big">00</span>
                                                    <span class="time-dot">:</span>
                                                    <p class="timeRefMinutes small">Phút</p>
                                                </li>
                                                <li>
                                                    <span class="seconds big">00</span>
                                                    <p class="timeRefSeconds small">Giây</p>
                                                </li>
                                            </ul>
                                        </div> end timer-area 
                                    </div> timer -->

                </div><!-- /container -->             
            </div><!-- /container -->             
        </div><!-- /fd-banner -->
        <c:if test="${landing.centerBannerActive == true}">
            <div class="fd-smallbanner">
                <img src="${landing.centerBanner}" alt="img" />
            </div><!-- /fd-smallbanner -->
        </c:if>
    </div><!-- /fd-banner-outer -->
    
</div>
<div class="container">
    <div class="bg-white">
        <div class="fd-space"></div>


        <c:forEach items="${bigLandingCates}" var="bigLandingCate">
            <c:set var="bigLandingCate" value="${bigLandingCate}" scope="request" />
            <c:if test="${bigLandingCate.template=='template1'}">
                <jsp:include page="/view/biglanding/biglandingbox/theme1/template1.jsp"></jsp:include>
            </c:if>

            <c:if test="${bigLandingCate.template=='template2'}">
                <jsp:include page="/view/biglanding/biglandingbox/theme1/template2.jsp"></jsp:include>
            </c:if>
            <c:if test="${bigLandingCate.template=='template3'}">
                <jsp:include page="/view/biglanding/biglandingbox/theme1/template3.jsp"></jsp:include>
            </c:if>
            <c:if test="${bigLandingCate.template=='template4'}">
                <jsp:include page="/view/biglanding/biglandingbox/theme1/template4.jsp"></jsp:include>
            </c:if>

        </c:forEach>

    </div><!-- /bg-white -->

</div><!-- /container -->
