<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul class="nav nav-tabs" role="tablist">
    <li>
        <a href="/cp/itemcrawllog.html">
            <i class="fa fa-list"></i>
            Quản lý sản phẩm crawl
        </a>
    </li>
    <li class="active">
        <a>
            <i class="glyphicon glyphicon-shopping-cart"></i>
            Lịch sử shop crawl
        </a>
    </li>
</ul>
<div class="func-container">
    <form:form modelAttribute="sellerCrawlLogSearch" method="POST" role="form" style="margin-top: 20px;">
        <div class="col-sm-4">
            <div class="form-group">
                <form:input path="sellerId" type="text" class="form-control" placeholder="Mã người bán"/>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="form-group">
                <form:hidden path="timeFrom" class="form-control timeselect" placeholder="Thời gian chạy từ"/>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="form-group">
                <form:hidden path="timeTo" class="form-control timeselect" placeholder="Thời gian chạy đến"/>
            </div>
        </div>
        <div class="col-sm-4">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Lọc</button>
            <button type="button" class="btn btn-default" onclick="sellercrawl.resertForm();"><span class="glyphicon glyphicon-refresh"></span> Nhập lại</button>
        </div>
    </form:form>
    <div class="clearfix"></div>
    <div style="margin-top: 10px">
        <h5 class="pull-left" style="padding: 10px; width: 33%;" >
            Tìm thấy <strong>${dataPage.dataCount} </strong> kết quả <strong>${dataPage.pageCount}</strong> trang.
        </h5>            
        <div class="btn-toolbar pull-right" style="padding: 15px 0px;">
            <jsp:include page="/view/cp/widget/paging.jsp">
                <jsp:param name="pageIndex" value="${dataPage.pageIndex}"/>
                <jsp:param name="pageCount" value="${dataPage.pageCount}"/>
            </jsp:include>
        </div>
        <div class="clearfix"></div>
    </div>
    <table class="table table-striped table-bordered table-responsive" style="margin-top: 10px">
        <tr class="text-center">
            <th >#</th>
            <th style="width: 100px;">Mã người bán</th>
            <th style="width: 100px;">Tổng tin Crawl  
                <span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" title="Tổng số lượt crawl SP theo ngày"></span>
            </th>
            <th style="width: 100px;">Lỗi  
                <span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" title="Tổng số lượt crawl SP bị lỗi"></span>
            </th>
            <th style="width: 300px;">Thành công  
                <span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" title="Tổng số lượt crawl SP thành công"></span>
            </th>
            <th style="width: 300px;">Thêm mới  
                <span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" title="Tổng số lượt crawl SP thêm mới"></span>
            </th>
            <th style="width: 300px;">Update  
                <span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" title="Tổng số lượt crawl SP update sp cũ"></span>
            </th>
            <th style="width: 300px;">Đủ điều kiện  
                <span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" title="Tổng số sp crawl đủ điều kiện đăng bán"></span>
            </th>
            <th style="width: 300px;">Ko đủ điều kiện  
                <span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" title="Tổng số sp crawl không đủ điều kiện đăng bán"></span>
            </th>
            <th style="width: 300px;">Trên sàn - realTime</th>
            <th style="width: 300px;">Trong kho - realTime</th>
            <th style="width: 300px;">Ngày cập nhật</th>
        </tr>
        <c:if test="${dataPage.dataCount > 0}">
            <jsp:useBean id="date" class="java.util.Date" />
            <tr class="text-center success" data-key="total">
                <td>
                    <strong>Total</strong>
                </td>
                <td>
                </td>
                <td><strong>${totalData.totalRequest}</strong></td>
                <td><strong>${totalData.errorRequest}</strong></td>
                <td><strong>${totalData.successRequest}</strong></td>
                <td>
                    <strong>${totalData.addRequest}</strong>
                </td>
                <td>
                    <strong>${totalData.editRequest}</strong>
                </td>
                <td>
                    <strong>${totalData.noCondition}</strong>
                </td>
                <td>
                    <strong>${totalData.enoughCondition}</strong>
                </td>
                <td>
                    <strong>${totalData.itemOK}</strong>
                </td>
                <td>
                    <strong>${totalData.itemMiss}</strong>
                </td>
                <td>
                </td>
                </tr>
            <c:forEach items="${dataPage.data}" varStatus="i" var="crawlLog">
                <tr class="text-center" data-key="${crawlLog.id}">
                    <td>
                        <p>${i.index + 1}</p>
                    </td>
                    <td>
                        <p><a href="/cp/itemcrawllog.html?ref=true&timeFrom=${sellerCrawlLogSearch.timeFrom}&timeTo=${sellerCrawlLogSearch.timeTo}&sellerId=${crawlLog.sellerId}&status=-1" target="_blank">${crawlLog.seller.username !=null ? crawlLog.seller.username : crawlLog.sellerId}</a></p>
                    </td>
                    <td><a href="/cp/itemcrawllog.html?ref=true&timeFrom=${sellerCrawlLogSearch.timeFrom}&timeTo=${sellerCrawlLogSearch.timeTo}&sellerId=${crawlLog.sellerId}&status=-1" target="_blank">${crawlLog.totalRequest}</a></td>
                    <td><a href="/cp/itemcrawllog.html?ref=true&timeFrom=${sellerCrawlLogSearch.timeFrom}&timeTo=${sellerCrawlLogSearch.timeTo}&sellerId=${crawlLog.sellerId}&status=0" target="_blank">${crawlLog.errorRequest}</a></td>
                    <td><a href="/cp/itemcrawllog.html?ref=true&timeFrom=${sellerCrawlLogSearch.timeFrom}&timeTo=${sellerCrawlLogSearch.timeTo}&sellerId=${crawlLog.sellerId}&status=1" target="_blank">${crawlLog.successRequest}</a></td>
                    <td>
                        <a href="/cp/itemcrawllog.html?ref=true&timeFrom=${sellerCrawlLogSearch.timeFrom}&timeTo=${sellerCrawlLogSearch.timeTo}&sellerId=${crawlLog.sellerId}&status=1&type=ADD" target="_blank">${crawlLog.addRequest}</a>
                    </td>
                    <td>
                        <a href="/cp/itemcrawllog.html?ref=true&timeFrom=${sellerCrawlLogSearch.timeFrom}&timeTo=${sellerCrawlLogSearch.timeTo}&sellerId=${crawlLog.sellerId}&status=1&type=EDIT" target="_blank">${crawlLog.editRequest}</a>
                    </td>
                    <td>
                        ${crawlLog.noCondition}
                    </td>
                    <td>
                        ${crawlLog.enoughCondition}
                    </td>
                    <td>
                        ${crawlLog.itemOK}
                    </td>
                    <td>
                        ${crawlLog.itemMiss}
                    </td>
                    <td>
                        <jsp:setProperty name="date" value="${crawlLog.time}" property="time"/>
                        <fmt:formatDate value="${date}" pattern="HH:mm dd/MM/yyyy" type="date"></fmt:formatDate>
                        </td>
                    </tr>
            </c:forEach>
        </c:if>
        <c:if test="${dataPage.dataCount <= 0}">
            <tr><td colspan="12" class='text-center text-danger'>Không tìm thấy dữ liệu tương ứng</td></tr>
        </c:if>
    </table>
    <div style="margin-top: 10px">
        <div class="btn-toolbar pull-right" style="padding: 15px 0px;">
            <jsp:include page="/view/cp/widget/paging.jsp">
                <jsp:param name="pageIndex" value="${dataPage.pageIndex}"/>
                <jsp:param name="pageCount" value="${dataPage.pageCount}"/>
            </jsp:include>
        </div>
        <div class="clearfix"></div>
    </div>
</div>