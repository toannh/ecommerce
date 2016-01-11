<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul class="nav nav-tabs" role="tablist">
    <li class="active">
        <a>
            <i class="fa fa-list"></i>
            Quản lý sản phẩm crawl
        </a>
    </li>
    <li>
        <a href="/cp/sellercrawllog.html">
            <i class="glyphicon glyphicon-shopping-cart"></i>
            Lịch sử shop crawl
        </a>
    </li>
</ul>
<div class="func-container">
    <form:form modelAttribute="crawlLogSearch" method="POST" role="form" style="margin-top: 20px;">
        <div class="col-sm-3">
            <div class="form-group">
                <form:input path="itemId" type="text" class="form-control" placeholder="Mã sản phẩm"/>
            </div>
            <div class="form-group">
                <form:select path="imageStatus" type="text" class="form-control">
                    <form:option value="" label="-- Chọn trạng thái ảnh --" />
                    <form:option value="NO_IMAGE" label="Không có ảnh" />
                    <form:option value="WAIT_DOWNLOAD" label="Chưa download" />
                    <form:option value="DOWNLOAD_SUCCESS" label="Download thành công" />
                    <form:option value="DOWNLOAD_FAIL" label="Download lỗi" />
                    <form:option value="SMALL_IMAGE" label="Ảnh nhỏ" />
                </form:select>
            </div>
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Lọc</button>
            <button type="button" class="btn btn-default" onclick="crawllog.resertForm();"><span class="glyphicon glyphicon-refresh"></span> Nhập lại</button>

        </div>
        <div class="col-sm-3">
            <div class="form-group">
                <form:input path="sellerId" type="text" class="form-control" placeholder="Mã người bán"/>
            </div>
            <div class="form-group">
                <form:select path="type" type="text" class="form-control">
                    <form:option value="" label="-- Chọn trạng SP --" />
                    <form:option value="ADD" label="Thêm mới" />
                    <form:option value="EDIT" label="Update sp cũ" />
                </form:select>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="form-group">
                <form:hidden path="timeFrom" class="form-control timeselect" placeholder="Thời gian chạy từ"/>
            </div>
            <div class="form-group">
                <form:select path="status" type="text" class="form-control">
                    <form:option value="-1" label="-- Chọn trạng thái --" />
                    <form:option value="0" label="Thất bại" />
                    <form:option value="1" label="Thành công" />
                </form:select>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="form-group">
                <form:hidden path="timeTo" class="form-control timeselect" placeholder="Thời gian chạy đến"/>
            </div>
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
            <th >STT</th>
            <th style="width: 100px;">Mã sản phẩm</th>
            <th style="width: 100px;">Mã người bán</th>
            <th style="width: 100px;">Thời gian chạy</th>
            <th style="width: 300px;">Lỗi</th>
            <th style="width: 300px;">Cảnh báo</th>
            <th style="width: 150px;">Trạng thái</th><!--Trạng thái crawl, trang thai anh,trang thai item-->
        </tr>
        <c:if test="${dataPage.dataCount > 0}">
            <jsp:useBean id="date" class="java.util.Date" />
            <c:forEach items="${dataPage.data}" varStatus="i" var="crawlLog">
                <tr class="text-center" data-key="${crawlLog.id}">
                    <td><p>${i.index + 1}</p></br>
                        <button type="button" class="btn btn-default" onclick="crawllog.showRequest('${crawlLog.id}');"><span class="glyphicon glyphicon-question-sign"></span></button>
                        <div id="request-${crawlLog.id}" style="display : none;">
                            <c:if test="${crawlLog.request != null}">
                                <table class="table table-bordered table-striped js-options-table">
                                    <thead>
                                        <tr>
                                            <th style="width:100px;">Key</th>
                                            <th>Value</th>
                                        </tr>
                                    </thead>
                                    <tbody style="word-break: break-all;">
                                        <c:forEach var="keyval" items="${crawlLog.request}" varStatus="i">
                                            <tr>
                                                <td>${keyval.keyword}</td>
                                                <td>${keyval.value}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>
                            <c:if test="${crawlLog.request == null}">
                                <p>Không Lưu được request</p>
                            </c:if>
                        </div>
                    </td>
                    <td>
                        <p><span  class="label label-default"><a href="${crawlLog.sourceLink}">${crawlLog.itemId != null ? crawlLog.itemId : "Source Link"}</a></span></p>
                                <c:if test="${crawlLog.item != null}">
                            </br>Link CDT : <p><a href="${baseUrl}/san-pham/${crawlLog.item.id}/${text:createAlias(crawlLog.item.name)}.html">${crawlLog.item.name != null && crawlLog.item.name != "" ? crawlLog.item.name :crawlLog.item.id }</a></p>
                            </c:if>
                    </td>
                    <td>${crawlLog.sellerId}</td>
                    <td>
                        <jsp:setProperty name="date" value="${crawlLog.time}" property="time"/>
                        <fmt:formatDate value="${date}" pattern="HH:mm dd/MM/yyyy" type="date"></fmt:formatDate>
                        </td>
                        <td>
                        <c:if test="${crawlLog.errorMessage.size() > 0}">
                            <c:forEach items="${crawlLog.errorMessage}" varStatus="j" var="errMsg">
                                <p>${j.index + 1}.${errMsg}</p>
                            </c:forEach>
                        </c:if>
                        <c:if test="${crawlLog.errorMessage.size() < 0}">
                            Không có lỗi nào
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${crawlLog.alertMessage.size() > 0}">
                            <c:forEach items="${crawlLog.alertMessage}" varStatus="j" var="alertMsg">
                                <p>${j.index + 1}.${alertMsg}</p>
                            </c:forEach>
                        </c:if>
                        <c:if test="${crawlLog.alertMessage.size() < 0}">
                            Không có cảnh báo nào
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${crawlLog.status == true}"><span  class="label label-success">Thành công</span></c:if>
                        <c:if test="${crawlLog.status == false}"><span  class="label label-danger">Thất bại</span></c:if>
                            </br>
                        <c:if test="${crawlLog.imageStatus == 'NO_IMAGE'}"><span  class="label label-warning">Không có ảnh</span></c:if>
                        <c:if test="${crawlLog.imageStatus == 'WAIT_DOWNLOAD'}"><span  class="label label-default">Chưa download</span></c:if>
                        <c:if test="${crawlLog.imageStatus == 'DOWNLOAD_SUCCESS'}"><span  class="label label-success">Download thành công</span></c:if>
                        <c:if test="${crawlLog.imageStatus == 'DOWNLOAD_FAIL'}"><span  class="label label-danger">URL ảnh có vấn đề</span></c:if>
                        <c:if test="${crawlLog.imageStatus == 'SMALL_IMAGE'}"><span class="label label-danger">Ảnh nhỏ</span></c:if>
                            </br>
                        <c:if test="${crawlLog.type == 'ADD'}"><span class="label label-success">Thêm mới</span></c:if>
                        <c:if test="${crawlLog.type == 'EDIT'}"><span class="label label-primary">Update sp cũ</span></c:if>
                        </td>
                    </tr>
            </c:forEach>
        </c:if>
        <c:if test="${dataPage.dataCount <= 0}">
            <tr><td colspan="7" class='text-center text-danger'>Không tìm thấy dữ liệu tương ứng</td></tr>
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