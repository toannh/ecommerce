<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="${staticUrl}/biglanding/css/landing-freeday.css" rel="stylesheet" />
<div class="fd-comingsoon-bg">
    <div class="fd-banner-outer">
        <div class="fd-banner"> 
            <img src="${staticUrl}/market/images/landing/landing-freeday/fd-banner-full-blank.jpg" alt="banner" />
            <div class="fd-caption">
                <div class="container">
                    <div class="timer">
                        <div class="timer-image"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-timer-image-blank.png" alt="Ngày miễn phí" /></div>
                        <div class="timer-area">
                            <ul id="countdown-freedayy">
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
                        </div><!-- end timer-area -->
                        <div class="timer-image fd-commit"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-commit.png" alt="Ngày miễn phí" /></div>
                    </div><!-- timer -->

                </div><!-- /container --> 
            </div><!-- fd-caption -->       
        </div><!-- /fd-banner -->
    </div><!-- /fd-banner-outer -->
    <div class="container">
        <div class="fd-comingsoon">
            <div class="fdc-row">
                <div class="fdc-left">
                    <div class="fdc-qc">
                        <a href="http://chodientu.vn/biglanding/236615420801/ngay-mien-phi.html"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-comingsoon1.jpg" alt="img" /></a>
                    </div>
                    <div class="fdc-qc">
                        <a href="http://chodientu.vn/biglanding/236615420801/ngay-mien-phi.html"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-comingsoon2.jpg" alt="img" /></a>
                    </div>
                </div><!-- /fdc-left -->
                <div class="fdc-right">
                    <div class="fdc-qc">
                        <a href="http://chodientu.vn/biglanding/236615420801/ngay-mien-phi.html"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-comingsoon3.jpg" alt="img" /></a>
                    </div>
                </div><!-- /fdc-right -->
                <div class="fdc-qc">
                    <a href="http://chodientu.vn/biglanding/236615420801/ngay-mien-phi.html"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-comingsoon4.jpg" alt="img" /></a>
                </div>
                <div class="fdc-funding">
                    <div class="row">
                        <div class="col-sm-4">
                            <div class="fdc-funding-left">
                                <img src="${staticUrl}/market/images/landing/landing-freeday/fd-comingsoon5-label.png" alt="img" />
                            </div>
                        </div>
                        <div class="col-sm-8">
                            <div class="fdc-funding-right">
                                <a href="http://www.viettelpost.com.vn/" target="_blank"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-logo-viettelpost2.png" alt="viettelpost" /></a>
                                <a href="http://www.ebay.vn/" target="_blank"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-logo-ebay.png" alt="ebay" /></a>
                                <a href="http://www.shipchung.vn/" target="_blank"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-logo-shipchung.png" alt="shipchung" /></a>
                                <a href="https://www.nganluong.vn/" target="_blank"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-logo-nganluong.png" alt="nganluong" /></a>
                                <a href="http://thanhtoanonline.vn/" target="_blank"><img src="${staticUrl}/market/images/landing/landing-freeday/fd-logo-ttol.png" alt="ttol" /></a>
                            </div>
                        </div>
                    </div>	
                </div><!-- /fdc-funding -->
            </div><!-- /fdc-row -->
        </div><!-- /fd-comingsoon -->
    </div><!-- /container -->
</div><!-- /fd-comingsoon-bg -->