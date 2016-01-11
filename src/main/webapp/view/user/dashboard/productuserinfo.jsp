<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="url" uri="http://chodientu.vn/url" %>
<jsp:useBean id="date" class="java.util.Date" />
<div class="container">
    <div class="tree-main">
        <jsp:include page="/view/market/widget/alias.jsp" />
        <div class="tree-view">
            <a class="home-button" href="${baseUrl}"></a>
            <span class="tree-normal"></span>
            <a href="${baseUrl}/user/${user.id}/ho-so-nguoi-ban.html">Hồ sơ người bán</a>
        </div><!-- /tree-view -->
    </div><!-- /tree-main -->
    <div class="bground">
        <div class="bg-white">
            <div class="page-userinfo">
                <div class="pu-title">Hồ sơ người bán</div>
                <div class="row">
                    <div class="userinfo-cv grid" style="background-color: white">
                        <c:if test="${shop !=null && shop != ''}">
                            <div class="img">
                                <a href="${baseUrl}/${shop.alias}" target="_blank">
                                    <img src="${shop.logo}" alt="avatar">
                                </a>
                            </div>
                            <div class="g-content">
                                <div class="g-row">
                                    <p class="p-checkred">
                                        <span class="icon16-shopin"></span>
                                        <a href="${baseUrl}/${shop.alias}/" target="_blank">
                                            <b>${shop.alias == null ? shop.email : shop.alias}</b>
                                        </a>
                                    </p> ( Điểm uy tín: ${pointSellerReputable}, 
                                    <i class="red" style="font-weight: bold;">${pointSellerUser}%</i> <i style="font-weight: bold;">Uy tín</i>) 
                                </div>
                                <div class="g-row">
                                    <p class="p-checkred">
                                        <span class="icon16-calendar"></span>Tham gia ngày: 
                                        <jsp:setProperty name="date" property="time" value="${shop.createTime}" /> 
                                        <fmt:formatDate type="date" pattern="dd/MM/yyyy"  value="${date}"></fmt:formatDate> - ${shop.address}
                                        </p>
                                    </div>
                                </div>
                        </c:if>
                        <c:if test="${shop==null || shop==''}">
                            <p><strong>${user.username==null?user.email:user.username}</strong>( Điểm uy tín: ${pointSellerReputable}, 
                                <i class="red">${pointSellerUser}%</i> <i style="font-weight: bold;">Uy tín</i>)
                            </p>
                            <jsp:setProperty name="date" property="time" value="${user.joinTime}" /> 
                            <p class="mgt-15"><span class="fa fa-calendar"></span> Tham gia ngày: 
                                <fmt:formatDate type="date" pattern="dd/MM/yyyy"  value="${date}"></fmt:formatDate>
                                <c:if test="${user.address!=null && user.address!=''}"> - ${user.address}</c:if>
                                </p>
                        </c:if>
                    </div><!-- userinfo-cv -->
                    <div class="col-md-5">
                        <div class="userinfo-review">
                            <div class="ur-title userinfo-cv" style="text-align: right">Điểm</div>
                            <div class="row">
                                <label class="pull-left">Uy tín</label>
                                <label class="pull-right">${pointSellerUser}%</label>
                            </div>
                            <div class="row">
                                <label class="pull-left">Bình thường</label>
                                <label class="pull-right">0%</label>
                            </div>
                            <div class="row">
                                <label class="pull-left">Không uy tín</label>
                                <label class="pull-right">${pointSellerNoReputable}%</label>
                            </div>
                        </div><!-- userinfo-review -->
                    </div><!-- col-md-4 -->
                    <div class="col-md-7">
                        <div class="userinfo-review">
                            <div class="ur-title userinfo-cv" style="text-align: center; padding-left: 240px">Người đánh giá
                                <span style="padding-left: 190px">Điểm</span>
                            </div>
                            <div class="row">
                                <label style="width: 200px">Chất lượng sản phẩm</label>
                                <label style="margin-left: 90px">${countSeller}</label>
                                <label style="margin-left: 270px">${productQuality}</label>
                            </div>
                            <div class="row">
                                <label style="width: 200px">Tương tác</label>
                                <label style="margin-left: 90px">${countSeller}</label>
                                <label style="margin-left: 270px">${interactive}</label>
                            </div>
                            <div class="row">
                                <label style="width: 200px">Chi phí vận chuyển</label>
                                <label style="margin-left: 90px">${countSeller}</label>
                                <label style="margin-left: 270px">${shippingCosts}</label>
                            </div>
                        </div><!-- userinfo-review -->
                    </div><!-- col-md-4 -->
                </div><!-- row -->
                <div class="boxblue">
                    <div class="boxblue-title full-tab">
                        <ul class="pull-left">
                            <li class="active reviewTrue">
                                <a href="javascript:;" onclick="sellerreview.changeTabReview(1, '${user.id}')">Người mua đánh giá</a>
                            </li>
                            <li class="reviewFalse">
                                <a href="javascript:;" onclick="sellerreview.changeTabReview(2, '${user.id}')">Đánh giá người bán khác</a>
                            </li>
                            <li class="reviewAll">
                                <a href="javascript:;" onclick="sellerreview.changeTabReview(3, '${user.id}')">Tất cả đánh giá</a>
                            </li>
                        </ul>
                    </div><!-- boxblue-title -->
                    <input type="hidden" name="userIdReview" value="${user.id}">
                    <input type="hidden" name="sellerId" value="${user.id}">
                    <div class="boxblue-content" id="showitemOther">
                        <ul class="pagination pull-right" id="pagination"></ul>
                    </div><!-- boxblue-content -->
                </div><!-- boxblue -->
            </div><!-- page-userinfo -->
        </div><!-- bg-white -->
        <div class="clearfix"></div>
    </div><!-- bground -->
</div>