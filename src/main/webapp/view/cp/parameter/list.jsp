<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul class="nav nav-tabs" role="tablist">
    <li class="active">
        <a>
            <i class="fa fa-list"></i>
            Danh sách thông số
        </a>
    </li>
</ul>
<div class="func-container">
    <div class="clearfix"></div>
    <div style="margin-top: 10px">
        <h5 class="pull-left" style="padding: 10px; width: 33%;" >
            Tìm thấy <strong>${parameter.dataCount} </strong> kết quả <strong>${parameter.pageCount}</strong> trang.
        </h5>            
        <div class="btn-toolbar pull-right" style="padding: 15px 0px;">
            <jsp:include page="/view/cp/widget/paging.jsp">
                <jsp:param name="pageIndex" value="${parameter.pageIndex}"/>
                <jsp:param name="pageCount" value="${parameter.pageCount}"/>
            </jsp:include>
        </div>
        <div class="clearfix"></div>
    </div>
    <table class="table table-striped table-bordered table-responsive" style="margin-top: 10px">
        <tr class="text-center">
            <th>Key quy ước</th>
            <th>Key</th>
            <th>Value</th>
            <th>
                <button onclick="parameter.add();" type="button" class="btn btn-success">
                    <span class="glyphicon glyphicon-plus"></span> Thêm mới</button>
            </th>
        </tr>
        <c:forEach var="pa" items="${parameter.data}" varStatus="stt">      
            <tr class="text-center">
                <td>${pa.keyConvention}</td>
                <td>${pa.key}</td>
                <td>${fn:substring(fn:trim(pa.value), 0, 70)}</td>
                <td>
                    <div class="btn-group">
                        <button type="button" class="btn btn-info" onclick="parameter.edit('${pa.keyConvention}');" >
                            <span class="glyphicon glyphicon-edit"></span> Sửa</button>
                        <button type="button" class="btn btn-danger" onclick="parameter.delete('${pa.keyConvention}');" >
                            <span class="glyphicon glyphicon-trash"></span> Xóa</button>
                    </div>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${(parameter.dataCount) == 0}">
            <tr>
                <td colspan="4" class="text-center text-danger">Không tìm thấy thông số nào</td>
            </tr>   
        </c:if>
    </table>
</div>