<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="date" class="java.util.Date" />
<ul class="nav nav-tabs" role="tablist">
    <li class="active"><a href="${baseUrl}/cp/modelseo.html">Danh sách model Seo</a></li>
</ul>
<div class="func-container">
    <form:form modelAttribute="modelSeoSearch" cssClass="form-vertical" method="POST" role="form" style="margin-top: 20px;">
        <div class="col-sm-4">
            <div class="form-group ">
                <form:input path="modelId" type="text" class="form-control" placeholder="Mã model"/>
            </div>
            <div class="form-group">
                <form:select path="status" type="text" class="form-control">
                    <option value="0">-- Chọn trạng thái --</option>
                    <option value="1">Đã duyệt</option>
                    <option value="2">Chưa duyệt</option>
                </form:select>
            </div>
            <div class="form-group">
                <form:input path="createTimeFrom" type="hidden" class ="form-control timestamp" placeholder="Ngày tạo từ"/>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Lọc model</button>
            </div>

        </div>
        <div class="form-group col-md-4">
            <div class="form-group ">
                <form:input path="administrator" type="text" class="form-control" placeholder="Email nhân viên Seo"/>
            </div>
            <div class="form-group">
                <form:input path="createTimeTo" type="hidden" class="form-control timestamp" placeholder="Đến ngày"/>
            </div>
        </div>

    </form:form>
    <div class="cms-line"></div>
    <div class="clearfix"></div>
    <div style="margin-top: 10px">
        <h5 class="pull-left" style="padding: 10px; width: 33%;" >
            Tìm thấy <strong>${text:numberFormat(dataPage.dataCount)} </strong> kết quả <strong>${text:numberFormat(dataPage.pageCount)}</strong> trang.
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
        <tr>
            <th class="text-center" style="vertical-align: middle">Mã</th>
            <th class="text-center" style="vertical-align: middle">Model</th>
            <th class="text-center" style="vertical-align: middle">Người sửa</th>
            <th class="text-center" style="vertical-align: middle">Thời gian sửa/cập nhật</th>
            <th class="text-center" style="vertical-align: middle">Trạng thái</th>
            <th class="text-center" style="width: 400px; vertical-align: middle;" >Chức năng</th>
        </tr>
        <c:forEach var="item" items="${dataPage.data}">
            <tr>
                <td class="text-center" style="vertical-align: middle">${item.modelId}</td>
                <td class="text-center" style="vertical-align: middle">${item.name}</td>
                <td class="text-center" style="vertical-align: middle">${item.administrator}</td>
                <td class="text-center" style="vertical-align: middle">
                    <jsp:setProperty name="date" property="time" value="${item.createTime}" /> 
                    <fmt:formatDate type="date" pattern="HH:mm dd/MM/yyyy"  value="${date}"></fmt:formatDate>
                        <hr style="margin: 0px">
                    <jsp:setProperty name="date" property="time" value="${item.updateTime}" /> 
                    <fmt:formatDate type="date" pattern="HH:mm dd/MM/yyyy"  value="${date}"></fmt:formatDate>

                    </td>
                    <td class="text-center" style="vertical-align: middle">${(item.active)?'<span class="label label-success">Đã duyệt</span>':'<span class="label label-info">Chưa duyệt</span>'}</td>
                
                <td class="text-center" style="vertical-align: middle">
                    <div class="btn-group">
                        <button onclick="modelseo.editSeo('${item.modelId}');" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span> Xem lại SEO </button>  
                        <c:if test="${item.active == false}">
                            <button type="button" class="btn btn-success" onclick="modelseo.reviewModelSeo('${item.modelId}');" >
                                <span class="glyphicon glyphicon-ok"></span> Duyệt luôn</button>
                            </c:if>
                            <c:if test="${item.active == true}">
                            <button type="button" class="btn btn-danger" onclick="modelseo.reviewModelSeo('${item.modelId}');" >
                                <span class="glyphicon glyphicon-remove"></span> Yêu cầu sửa</button>
                            </c:if>
                    </div>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${dataPage.dataCount == 0}">
            <tr>
                <td colspan="6" class="text-center text-danger">Không tìm thấy model nào</td>
            </tr>
        </c:if>
    </table>
    <div style="margin-top: 10px; margin-bottom:10px">
        <div class="btn-toolbar pull-right">
            <jsp:include page="/view/cp/widget/paging.jsp">
                <jsp:param name="pageIndex" value="${dataPage.pageIndex}"/>
                <jsp:param name="pageCount" value="${dataPage.pageCount}"/>
            </jsp:include>
        </div>
        <div class="clearfix"></div>
    </div>
</div>