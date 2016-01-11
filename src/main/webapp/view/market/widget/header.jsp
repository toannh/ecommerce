<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="url" uri="http://chodientu.vn/url" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="header">
    <div class="container"> 
        <c:if test="${marketshop == null}">
            <a class="open-show-free" href="${baseUrl}/user/open-shop-step1.html">Mở Shop miễn phí</a>
        </c:if>
        <c:if test="${marketshop != null}">
            <a class="open-show-free" href="${baseUrl}/${marketshop.alias}/" target="_blank">Vào shop</a>
        </c:if>
    </div><!-- /container -->
</div><!-- /header -->

</div><!-- /big-header -->