<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<%@ taglib prefix="url" uri="http://chodientu.vn/url" %>
<link href="${staticUrl}/biglanding/css/landing-big.css" rel="stylesheet" />
<div class="lb-banner">
    	<img src="${staticUrl}/market/images/landing-big/lb-bg.jpg" alt="Landing big" />
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
            	<div class="lb-page lb-page-browser">
                	<div class="lb-bigtitle">
                            <a href="${baseUrl}/biglanding/${landing.id}/${text:createAlias(categoryParent.name)}.html"><i class="fa fa-home"></i></a>
                        <c:if test="${categoryChildList != null}" >
                        <a class="lb-bigtitle-link" >${categoryParent.name}</a>
                        </c:if>
                        <c:if test="${categoryChildList == null}" >
                        <a class="lb-bigtitle-link" href="${baseUrl}/biglanding/${landing.id}/${categoryParent.id}/${text:createAlias(categoryParent.name)}.html">${categoryParent.name}</a>    
                        </c:if>
                    </div>
                        <c:if test="${categoryChildList != null}" >
                     <c:forEach items="${categoryChildList}" var="categoryChild">
                    <div class="lb-subbox">
                    	<div class="ls-title">
                        	<a class="ls-title-link" href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html">${categoryChild.name}</a>
                            <a class="ls-title-more" href="${baseUrl}/biglanding/${landing.id}/${categoryChild.id}/${text:createAlias(categoryChild.name)}.html"><i class="fa fa-caret-right"></i>Xem tất cả</a>
                        </div>
                        <div class="ls-content">
                        	<div class="lb-listproduct">
                                <c:forEach items="${categoryChild.bigLandingItem}" var="blItem">
                            	<div class="lb-subitem">
                                    <div class="ls-thumb"><a href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.name)}.html"><img data-original="${blItem.image}" class="lazy" alt="img" /></a></div>
                                        <c:if test="${blItem.sellerImage !=null && blItem.sellerName !=null && blItem.sellerImage !='' && blItem.sellerName !=''}">
                                            <a class="ls-owner" href="${baseUrl}/${blItem.sellerName}"><img data-original="${blItem.sellerImage}" class="lazy" alt="avatar" /></a>
                                        </c:if>
                                    <c:if test="${blItem.item.listingType != 'AUCTION' && (text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent) != '0')}">
                                <span class="ls-sale">-${text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent)}%</span>
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
                                </div><!-- /lb-subitem -->
                                </c:forEach>
                                <div class="clearfix"></div>
                            </div><!-- /lb-listproduct -->
                        </div><!-- /ls-content -->
                    </div><!-- /lb-subbox -->
                     </c:forEach>
                        </c:if>
                     <c:if test="${categoryChildList == null}" >
                         <div class="lb-subbox">
                    	<div class="ls-title">
                        	<label class="ls-title-link">${categoryChild.name}</label>
                        </div>
                             <div class="ls-content">
                        	<div class="lb-listproduct">
                                 <c:forEach items="${bigLandingItems.data}" var="blItem">
                            	<div class="lb-subitem">
                                    <div class="ls-thumb"><a href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.name)}.html"><img data-original="${blItem.image}" class="lazy" alt="img" /></a></div>
                                    <c:if test="${blItem.sellerImage !=null && blItem.sellerName !=null && blItem.sellerImage !='' && blItem.sellerName !=''}">
                                            <a class="ls-owner" href="${baseUrl}/${blItem.sellerName}"><img data-original="${blItem.sellerImage}" class="lazy" alt="avatar" /></a>
                                        </c:if>
                                    <c:if test="${blItem.item.listingType != 'AUCTION' && (text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent) != '0')}">
                                <span class="ls-sale">-${text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent)}%</span>
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
                                </div><!-- /lb-subitem -->
                                 </c:forEach>
                                <div class="clearfix"></div>
                                </div>
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
                             </div>
                         </div>
                     </c:if>
                </div><!-- /lb-page -->
            </div><!-- /bground -->
        </div><!-- /container -->
        <div class="internal-text">
        </div><!-- /internal-text -->
    </div><!-- bg-white -->
