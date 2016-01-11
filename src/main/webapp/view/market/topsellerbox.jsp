<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld"%>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld"%>
<div class="box">
    <div class="box-title"><h2><label class="lb-name">Bán chạy nhất từ người bán uy tín</label></h2></div>
    <div class="tab-outer">
        <div class="tab-title">
            <c:forEach items="${topSellerBox}" var="topseller" varStatus="stt">
                <a <c:if test="${stt.index==0}">class="active"</c:if> href="#usertab${stt.index+1}" rel="nofollow">
                    <img data-original="${topseller.image}" class="lazy" />
                    <p><b>Shop</b><span onclick="market.linkShop('${topseller.sellerName}')">${topseller.sellerName}</span></p>
                    <p>${topseller.city}</p>
                    <p>${topseller.countItem} sản phẩm</p>
                </a>
            </c:forEach>
        </div>
        <div class="tab-container">
            <c:forEach items="${topSellerBox}" var="topsellerItem" varStatus="stt">
                <div id="usertab${stt.index+1}" class="tab-content" <c:if test="${stt.index==0}">style="display:block;"</c:if><c:if test="${stt.index!=0}">style="display:none;"</c:if>>
                        <div class="userhot-list">
                        <c:forEach items="${topsellerItem.items}" var="items">

                            <div class="home-item">
                                <div class="hoi-inner">
                                    <c:if test="${items.listingType=='AUCTION'}">
                                        <span class="hoi-bid"></span>
                                    </c:if>
                                        <a class="hoi-thumblink" target="_blank" href="${basicUrl}/san-pham/${items.id}/${text:createAlias(items.name)}.html"><img class="lazy" data-original="${(items.images != null && fn:length(items.images) >0)?items.images[0]:staticUrl.concat('/market/images/no-image-product.png')}" /></a>
                                    <div class="hoi-title">
                                        <div class="hoi-row"><a title="${items.name}" target="_blank" href="${basicUrl}/san-pham/${items.id}/${text:createAlias(items.name)}.html">${items.name}</a></div>
                                        <div class="hoi-row"><span class="hoi-price">${text:sellPrice(items.sellPrice,items.discount,items.discountPrice,items.discountPercent)} <sup class="u-price">đ</sup></span></div>
                                    </div>
                                    <div class="hoi-view">
                                        <a class="hoi-btn" href="javascript:;" rel="nofollow" onclick="market.quickview('${items.id}')">Xem nhanh</a>
                                        <a class="hoi-btn" href="#" rel="nofollow"><span class="icon24-star"></span></a>
                                    </div>
                                </div>
                            </div><!-- home-item -->
                        </c:forEach>
                    </div><!-- userhot-list -->
                </div><!-- /tab-content -->
            </c:forEach>

        </div><!-- /tab-container -->
    </div><!-- /tab-outer -->
</div><!-- box -->