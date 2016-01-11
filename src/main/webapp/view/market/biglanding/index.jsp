<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld" %>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>

<div class="lb-banner">
    <img src="${staticUrl}/market/images/landing-big/lb-bg.jpg" alt="Landing big" />
    <div class="lb-caption">
    </div><!-- /lb-caption -->
</div><!-- /lb-banner -->

<div class="bg-white">
    <div class="container">
        <div class="bground">
            <div class="lb-page">
                <c:forEach items="${bigLandingCates}" var="bigLandingCate">
                    <c:set var="bigLandingCate" value="${bigLandingCate}" scope="request" />
                    <c:if test="${bigLandingCate.template=='template1'}">
                        <jsp:include page="/view/market/biglandingbox/template1.jsp"></jsp:include>
                    </c:if>

                    <c:if test="${bigLandingCate.template=='template2'}">
                        <jsp:include page="/view/market/biglandingbox/template2.jsp"></jsp:include>
                    </c:if>
                    <c:if test="${bigLandingCate.template=='template3'}">
                        <jsp:include page="/view/market/biglandingbox/template3.jsp"></jsp:include>
                    </c:if>
                    <c:if test="${bigLandingCate.template=='template4'}">
                        <jsp:include page="/view/market/biglandingbox/template4.jsp"></jsp:include>
                    </c:if>

                </c:forEach>	

                <div class="lb-salesock">
                    <div class="lb-salesock-title">
                        <div class="row">
                            <div class="col-sm-7"><label class="promotionName">${bigLandingSeller.promotionName}</label></div><!-- /col -->
                            <div class="col-sm-5 lb-salesock-right">
                                <span class="lb-salesock-span bigLandingItemCount">Có ${blPromotionItemCount} sản phẩm</span>
                                <a class="lb-salesock-link bigLandingLinkPromotion" target="_blank" href="${basicUrl}/${bigLandingSeller.alias}/browse.html?promotionId=${bigLandingSeller.promotionId}">Xem tất cả khuyến mại</a>
                            </div><!-- /col -->
                        </div><!-- /row -->
                    </div><!-- /lb-salesock-title -->
                    <div class="lb-salesock-content" id="bigLandingItem">
                        <c:forEach items="${blPromotionItem}" var="item" begin="0" end="4">
                            <div class="lb-salesock-item">
                                <div class="lsi-thumb"><a href="${basicUrl}/san-pham/${item.id}/${text:createAlias(item.name)}.html"><img src="${(item.images != null && fn:length(item.images) >0)?item.images[0]:staticUrl.concat('/market/images/no-image-product.png')}" alt="img" /></a></div>
                                <div class="lsi-row"><a class="lsi-title" href="${basicUrl}/san-pham/${item.id}/${text:createAlias(item.name)}.html">${item.name}</a></div>
                                <div class="lsi-row"><span class="lsi-price">${text:sellPrice(item.sellPrice,item.discount,item.discountPrice,item.discountPercent)} <sup class="u-price">đ</sup></span>
                                    <c:if test="${(text:discountPrice(item.startPrice,item.sellPrice,item.discount,item.discountPrice,item.discountPercent))!='0'}">
                                        <span class="lsi-oldprice">${text:startPrice(item.startPrice,item.sellPrice,item.discount)} <sup class="u-price">đ</sup></span>
                                    </c:if>
                                </div>
                            </div><!-- /lb-salesock-item -->
                        </c:forEach>
                        <div class="clearfix"></div>
                    </div><!-- /lb-salesock-content -->
                </div><!-- /lb-salesock -->
                <div class="lbhome-product">
                    <div id="lbhomeslider" class="owl-carousel">
                        <c:forEach items="${listBigLandingPromotion}" var="blPromotion" varStatus="sttIndex">
                            <div class="lb-seller ${blPromotion.promotionId} ${sttIndex.index==0?" active":""}">
                                <a class="grid" onclick="biglanding.getbiglandingpromotion('${blPromotion.promotionId}');"  href="javascript:;" >
                                    <c:if test="${blPromotion.logoShop!=null && blPromotion.logoShop!=''}">
                                        <span class="img"><img src="${blPromotion.logoShop}" alt="${blPromotion.sellerName}" /></span>
                                        </c:if>
                                    <span class="g-content" <c:if test="${blPromotion.logoShop==null || blPromotion.logoShop==''}">style="margin-left: 0px"</c:if>>${blPromotion.sellerName}</span>
                                    </a>
                                </div><!-- /lb-seller -->
                        </c:forEach>
                    </div><!-- /owl-carousel -->
                </div><!-- /lbhome-product -->

            </div><!-- /lb-page -->
        </div><!-- /bground -->
    </div><!-- /container -->
    <div class="internal-text">
    </div><!-- /internal-text -->
</div><!-- bg-white -->


