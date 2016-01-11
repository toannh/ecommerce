<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld"%>

<div class="fd-title">
    <a class="fdt-name fdt-red" href="${baseUrl}/biglanding/${landing.id}/${bigLandingCate.id}/${text:createAlias(bigLandingCate.name)}.html">${bigLandingCate.name}</a>
    <ul class="fdt-category">
        <c:forEach items="${bigLandingCate.categorySubs}" var="categorySubItem">
            <li><a href="${baseUrl}/biglanding/${landing.id}/${categorySubItem.id}/${text:createAlias(categorySubItem.name)}.html">${categorySubItem.name}</a></li>
            </c:forEach>
    </ul>
</div><!-- /fd-title -->
<div class="fd-box">
    <div class="fdc-banner">
        <img class="fdc-image lazy" data-original="${bigLandingCate.image}" />
        <a class="fdc-link" href="${baseUrl}/biglanding/${landing.id}/${bigLandingCate.id}/${text:createAlias(bigLandingCate.name)}.html">Xem tất cả<i class="fa fa-caret-right"></i></a>
    </div><!-- /fdc-banner -->
    <div class="fd-listitem">
        <c:forEach items="${bigLandingCate.bigLandingItem}" var="blItem">	
            <div class="fd-item">
                <div class="fdi-thumb">
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
                        <span class="fdi-end">Hết hàng</span>
                    </c:if>
                 
                    <c:if test="${blItem.promition !=null && blItem.promition !=''}">
                        <!--<span class="fdi-gift">KM</span>-->
                    </c:if>
                    <a href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.item.name)}.html"><img data-original="${blItem.image}" class="lazy" /></a>
                </div>
                <div class="fdi-title"><a href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.item.name)}.html">${blItem.name}</a></div>
                <div class="fdi-row">
                    <span class="fdi-price">${text:sellPrice(blItem.item.sellPrice, blItem.item.discount, blItem.item.discountPrice, blItem.item.discountPercent)}<sup>đ</sup></span>
                    <c:if test="${text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent) != '0'}">
                        <span class="fdi-col">&nbsp;|&nbsp;</span>                    
                    </c:if>
                    <c:if test="${text:startPrice(blItem.item.startPrice, blItem.item.sellPrice, blItem.item.discount) != '0'}">
                        <span class="fdi-oldprice">${text:startPrice(blItem.item.startPrice, blItem.item.sellPrice, blItem.item.discount)}<sup>đ</sup></span>
                    </c:if>
                </div>
                <div class="fdi-button"><a href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.item.name)}.html">Xem ngay</a></div>
            </div><!-- /fd-item -->
        </c:forEach>
        <div class="clearfix"></div>
    </div><!-- /fd-listitem -->
    <div class="clearfix"></div>
</div><!-- /fd-box -->

