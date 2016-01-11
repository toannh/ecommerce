<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld"%>
<c:set var="datetime" value="<%= new java.util.Date().getTime()%>" />
<div class="container">
    <div class="banner"> 
        <c:if test="${landingNewCDT.bannerCenter!=null && landingNewCDT.bannerCenter!=''}">
            <img src="${landingNewCDT.bannerCenter}">
        </c:if>
        <div class="slider hidden-xs" style="display: none">
            <c:forEach items="${landingNewSlide}" var="slide"> 
                <div class="slide">
                    <img src="${slide.image}">
                    <p>${slide.name}</p>
                    <a class="btn btn-default btn-slide" target="_blank" href="${slide.url}" type="submit">Xem Ngay</a>
                </div> 
            </c:forEach>

        </div> 
    </div><!--end .banner-->
    <div class="main_content">
        <div class="top-content clearfix">
            <div class="pull-left col-md-9">
                <h3 style="color: ${landingNewCDT.color}"> ${landingNewCDT.name}</h3>
                <p class="text">${landingNewCDT.description}</p>
            </div>
            <div class="pull-right btn-share hidden-xs col-md-3">
                <ul class="list-inline"> 
                    <li>
                        <div class="fb-like" data-href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html" data-layout="button" data-action="like" data-show-faces="true"></div>
                    </li>
                    <script src="https://apis.google.com/js/platform.js" async defer></script>
                    <li><div class="g-plusone" data-size="medium"></div></li>
                </ul>
            </div>
        </div><!-- .top-content -->			
        <div id="product" class="product clearfix" > 
            <c:forEach items="${listLandingNewItem.data}" var="blItem">
                <div class="landing-list">


                    <c:if test="${blItem.item.listingType != 'AUCTION' && (text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent))!='0'}">
                        <span class="item-redsale" style="background-color:${landingNewCDT.color};">
                            <span class="ir-percent">-${text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent)}<i>%</i></span>
                            <c:if test="${blItem.item.shipmentType=='FIXED' && blItem.item.shipmentPrice<=0}">
                                <span class="ir-line"></span>
                                <span class="ir-text">Miễn phí vận chuyển</span>
                            </c:if>

                        </span>
                    </c:if>  
                    <c:if test="${blItem.item.listingType != 'AUCTION' && (text:percentPrice(blItem.item.startPrice,blItem.item.sellPrice,blItem.item.discount,blItem.item.discountPrice,blItem.item.discountPercent))=='0' && (blItem.item.shipmentType=='FIXED' && blItem.item.shipmentPrice<=0)}">
                        <span class="item-redsale" style="background-color:${landingNewCDT.color};">
                            <span class="ir-text">Miễn phí vận chuyển</span>
                        </span>
                    </c:if>  


                    <div class="thumb clearfix">
                        <a class="hvr-radial-out" href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.item.name)}.html" target="_blank"><img data-original="${blItem.image}" class="lazy" />
                        </a>
                        <c:if test="${blItem.item.endTime < datetime || blItem.item.quantity<=0 || !blItem.item.approved || !blItem.item.active}">
                            <span>Hết hàng !</span>
                        </c:if>
                    </div>
                    <div class="info-thumb">
                        <a class="title" href="${basicUrl}/san-pham/${blItem.itemId}/${text:createAlias(blItem.item.name)}.html" target="_blank">${blItem.name}</a>
                        <p class="price" style="color: ${landingNewCDT.color}">${text:sellPrice(blItem.item.sellPrice, blItem.item.discount, blItem.item.discountPrice, blItem.item.discountPercent)}<sup class="u-price">đ</sup>
                            <c:if test="${text:startPrice(blItem.item.startPrice, blItem.item.sellPrice, blItem.item.discount) != '0'}">
                                <span class="sellof-price">${text:startPrice(blItem.item.startPrice, blItem.item.sellPrice, blItem.item.discount)}<sup class="u-price">đ</sup></span>
                            </c:if>
                        </p>

                    </div>   
                </div><!-- end .landing-list-->
            </c:forEach>

        </div>
        <c:if test="${listLandingNewItem.pageCount > 1}">
            <div class="box-bottom">
                <ul class="pagination">
                    <c:if test="${listLandingNewItem.pageIndex > 3}"><li><a href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html" >&laquo;</a></li></c:if>
                    <c:if test="${listLandingNewItem.pageIndex > 2}"><li><a href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html?page=${listLandingNewItem.pageIndex}" >&lt;</a></li></c:if>
                    <c:if test="${listLandingNewItem.pageIndex >= 2}"><li><a href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html?page=${listLandingNewItem.pageIndex-1}" >${listLandingNewItem.pageIndex-1}</a></li></c:if>
                    <c:if test="${listLandingNewItem.pageIndex >= 1}"><li><a href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html?page=${listLandingNewItem.pageIndex}" >${listLandingNewItem.pageIndex}</a></li></c:if>
                    <li class="active"><a>${listLandingNewItem.pageIndex + 1}</a></li>
                    <c:if test="${listLandingNewItem.pageCount - listLandingNewItem.pageIndex >= 2}"><li><a href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html?page=${listLandingNewItem.pageIndex+2}">${listLandingNewItem.pageIndex+2}</a></li></c:if>
                    <c:if test="${listLandingNewItem.pageCount - listLandingNewItem.pageIndex > 3}"><li><a href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html?page=${listLandingNewItem.pageIndex+3}">${listLandingNewItem.pageIndex+3}</a></li></c:if>
                    <c:if test="${listLandingNewItem.pageCount - listLandingNewItem.pageIndex > 2}"><li><a href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html?page=${listLandingNewItem.pageIndex+2}">&gt;</a></li></c:if>
                    <c:if test="${listLandingNewItem.pageCount - listLandingNewItem.pageIndex > 2}"><li><a href="${baseUrl}/landingnew/${landingNewCDT.id}/${text:createAlias(landingNewCDT.name)}.html?page=${listLandingNewItem.pageCount}">&raquo;</a></li></c:if>
                    </ul>
                </div><!--box-bottom-->
        </c:if>
        <div class="slider-bottom">
            <div class="divslider">
                <div id="landingnew-slider" class="owl-carousel">
                    <c:forEach items="${landingNewSlide}" var="slide"> 
                        <div class="ld-item">
                            <div class="hi-thumb">
                                <img src="${slide.image}">
                                <p>${slide.name}</p>
                                <a class="btn btn-default btn-slide" target="_blank" href="${slide.url}" type="submit">Xem Ngay</a>
                            </div>
                        </div><!-- logo-item -->
                    </c:forEach>

                </div><!-- owl-carousel -->  
            </div> 
        </div>                     
    </div><!--end .main-content-->
</div><!-- container -->