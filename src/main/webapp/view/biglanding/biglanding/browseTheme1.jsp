<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<%@ taglib prefix="url" uri="http://chodientu.vn/url" %>
<link href="${staticUrl}/biglanding/css/landing-freeday.css" rel="stylesheet" />
<div class="fd-banner-outer" <c:if test="${bigLanding.background!=null && bigLanding.background!=''}"> style="background: ${bigLanding.background}"</c:if>>
        <div class="fd-banner"> 
            <img data-original="${bigLanding.heartBanner}" alt="banner" class="lazy" />
        <div class="fd-caption">
            <div class="container">
                <div class="timer timer-yellow">
                    <div class="timer-image">
                        <c:if test="${bigLanding.logoBanner!=null && bigLanding.logoBanner!=''}">
                            <a href="${baseUrl}/biglanding/${bigLanding.id}/${text:createAlias(bigLanding.name)}.html"><img data-original="${bigLanding.logoBanner}" class="lazy" alt="Ngày miễn phí" /></a>
                            </c:if>
                    </div>

                </div><!-- timer -->
            </div><!-- /container -->     
        </div><!-- /fn caption -->  
    </div> <!-- /fd-banner -->
</div> <!-- /fd-banner-outer -->

<div class="container">
    <div class="bg-white">
        <div class="fd-space"></div>
        <div class="fd-title">
            <a class="fdt-home" href="${baseUrl}/biglanding/${bigLanding.id}/${text:createAlias(bigLanding.name)}.html"></a>
            <a class="fdt-name fdt-blue" href="${baseUrl}/biglanding/${landing.id}/${categoryParent.id}/${text:createAlias(categoryParent.name)}.html">${categoryParent.name}</a>    
            <ul class="fdt-category">

                <c:forEach items="${categoryAllChild}" var="bigLandingCate"> 
                    <li class="<c:if test="${bigLandingCate.id == categoryChild.id}">active</c:if>">
                        <a href="${baseUrl}/biglanding/${landing.id}/${bigLandingCate.id}/${text:createAlias(bigLandingCate.name)}.html">${bigLandingCate.name}</a>
                    </li>
                </c:forEach>
            </ul>
        </div><!-- /fd-title -->
        <div class="fd-listproduct">
            <c:forEach items="${bigLandingItems.data}" var="blItem">
                <div class="fd-subitem">
                    <div class="ls-thumb">
                        <c:if test="${blItem.item.listingType != 'AUCTION' && (text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent))!='0'}">
                            <span class="item-redsale">
                                <span class="ir-percent">-${text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent)}<i>%</i></span>
                                <c:if test="${blItem.item.shipmentType=='FIXED' && blItem.item.shipmentPrice<=0}">
                                    <span class="ir-line"></span>
                                    <span class="ir-text">Miễn phí vận chuyển</span>
                                </c:if>
                            </span>    
                        </c:if>  
                        <c:if test="${blItem.item.listingType != 'AUCTION' && (text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent))=='0' && (blItem.item.shipmentType=='FIXED' && blItem.item.shipmentPrice<=0)}">
                            <span class="item-redsale">
                                <span class="ir-text">Miễn phí vận chuyển</span>
                            </span>
                        </c:if>
                        <c:if test="${blItem.item.endTime < datetime || blItem.item.quantity<=0 || !blItem.item.approved || !blItem.item.active}">
                            <span class="ls-end">Hết hàng</span>
                        </c:if>
                            <a href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.name)}.html"><img data-original="${blItem.image}" class="lazy" /></a></div>
                            <c:if test="${blItem.sellerImage !=null && blItem.sellerName !=null && blItem.sellerImage !='' && blItem.sellerName !=''}">
                                <a class="ls-owner" href="${baseUrl}/${blItem.sellerName}"><img data-original="${blItem.sellerImage}" class="lazy" alt="avatar" /></a>
                        </c:if>
                        
                    <div class="ls-icon">
                        <span class="icon16-nlgray"></span>
                        <span class="icon16-codgray"></span>
                        <span class="icon16-transgray"></span>
                    </div>
                    <div class="ls-text">
                        <div class="ls-row"><a class="ls-title-name" href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.name)}.html">${blItem.name}</a></div>
                        <div class="ls-row"><span class="ls-price">${text:sellPrice(blItem.item.sellPrice, blItem.item.discount, blItem.item.discountPrice, blItem.item.discountPercent)} đ</span>
                            <c:if test="${text:startPrice(blItem.item.startPrice, blItem.item.sellPrice, blItem.item.discount) != '0'}">  
                                <span class="ls-oldprice">${text:startPrice(blItem.item.startPrice, blItem.item.sellPrice, blItem.item.discount)} đ</span>
                            </c:if>
                        </div>
                    </div>
                </div><!-- /fd-subitem -->
            </c:forEach>
            <div class="clearfix"></div>
        </div><!-- /fd-listproduct -->
        <c:if test="${bigLandingItems.pageCount > 1}">
            <div class="box-bottom">
                <ul class="pagination">
                    <c:if test="${bigLandingItems.pageIndex > 3}"><li><a href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html" >&laquo;</a></li></c:if>
                    <c:if test="${bigLandingItems.pageIndex > 2}"><li><a href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html?page=${bigLandingItems.pageIndex}" >&lt;</a></li></c:if>
                    <c:if test="${bigLandingItems.pageIndex >= 2}"><li><a href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html?page=${bigLandingItems.pageIndex-1}" >${bigLandingItems.pageIndex-1}</a></li></c:if>
                    <c:if test="${bigLandingItems.pageIndex >= 1}"><li><a href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html?page=${bigLandingItems.pageIndex}" >${bigLandingItems.pageIndex}</a></li></c:if>
                    <li class="active"><a>${bigLandingItems.pageIndex + 1}</a></li>
                    <c:if test="${bigLandingItems.pageCount - bigLandingItems.pageIndex >= 2}"><li><a href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html?page=${bigLandingItems.pageIndex+2}">${bigLandingItems.pageIndex+2}</a></li></c:if>
                    <c:if test="${bigLandingItems.pageCount - bigLandingItems.pageIndex > 3}"><li><a href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html?page=${bigLandingItems.pageIndex+3}">${bigLandingItems.pageIndex+3}</a></li></c:if>
                    <c:if test="${bigLandingItems.pageCount - bigLandingItems.pageIndex > 2}"><li><a href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html?page=${bigLandingItems.pageIndex+2}">&gt;</a></li></c:if>
                    <c:if test="${bigLandingItems.pageCount - bigLandingItems.pageIndex > 2}"><li><a href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html?page=${bigLandingItems.pageCount}">&raquo;</a></li></c:if>
                    </ul>
                </div><!--box-bottom-->
        </c:if>
    </div><!-- /bg-white -->
</div><!-- /container -->