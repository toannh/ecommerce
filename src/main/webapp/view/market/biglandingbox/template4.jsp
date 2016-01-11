<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld"%>
<div class="lb-box lb-box4">
    <div class="lb-category lb-category-right lb-bg-green">
        <div class="lbc-inner">
            <div class="lbc-title"><a href="${baseUrl}/biglanding/${bigLandingCate.id}/${text:createAlias(bigLandingCate.name)}.html">${bigLandingCate.name}</a></div>
            <div class="lbc-list">
                <ul>
                    <c:forEach items="${bigLandingCate.categorySubs}" var="categorySubItem">
                        <li><a href="${baseUrl}/biglanding/${categorySubItem.id}/${text:createAlias(categorySubItem.name)}.html">${categorySubItem.name}</a></li>
                        </c:forEach>
                </ul>
            </div>
            <div class="lbc-img"><img data-original="${bigLandingCate.image}" class="lazy" /></div>
            <a class="lbc-more" href="${baseUrl}/biglanding/${bigLandingCate.id}/${text:createAlias(bigLandingCate.name)}.html">Xem tất cả</a>
        </div><!-- /lbc-inner -->
    </div><!-- /lb-category -->
    <c:forEach items="${bigLandingCate.bigLandingItem}" var="blItem">
        <c:if test="${blItem.position==2 || blItem.position==3}">
            <div class="lb-item lb-skyscraperitem lb-position${blItem.position}">
            </c:if>
            <c:if test="${blItem.position !=2 && blItem.position !=3}">            
                <div class="lb-item lb-position${blItem.position}">
                </c:if>
                <div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title data-original-title="${blItem.promition}">
                    <a href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.item.name)}.html"><img data-original="${blItem.image}" class="lazy" /></a>
                    <div class="lbi-caption">
                        <a class="lbi-title" href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.item.name)}.html">${blItem.name}</a>
                        <span class="lbi-price">${text:sellPrice(blItem.item.sellPrice, blItem.item.discount, blItem.item.discountPrice, blItem.item.discountPercent)}<sup>đ</sup></span>
                        <c:if test="${text:startPrice(blItem.item.startPrice, blItem.item.sellPrice, blItem.item.discount) != '0'}">
                            <span class="lbi-oldprice">${text:startPrice(blItem.item.startPrice, blItem.item.sellPrice, blItem.item.discount)}<sup>đ</sup></span>
                        </c:if>
                    </div>
                    <c:if test="${blItem.item.listingType != 'AUCTION' && (text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent) != '0')}">
                                <div class="lbi-sale"><label>Sale</label><span>${text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent)}<sup>%</sup></span></div>
                            </c:if>
                </div>
            </div><!-- /lb-item -->
        </c:forEach>
    </div><!-- /lb-box -->