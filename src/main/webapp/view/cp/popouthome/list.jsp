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
            Danh sách POPOUT trang chủ
        </a>
    </li>
</ul>
<div class="func-container">
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
            <th>STT</th>
            <th>Ảnh</th>
            <th>Tiêu đề</th>
            <th style="width: 350px">Đường dẫn</th>
            <th>Kiểu hiển thị</th>
            <th>Thời gian tạo</th>
            <th>Trạng thái</th>
            <th><p><a href="javascript:;" class="btn btn-success" onclick="popouthome.add()"><i class="glyphicon glyphicon-tag"></i> Thêm mới</a></p></th>
        </tr>

        <c:if test="${dataPage.dataCount > 0}">
            <jsp:useBean id="date" class="java.util.Date" />
            <c:forEach items="${dataPage.data}" varStatus="i" var="pop">
                <tr class="text-center" id="${pop.id}">
                    <td>${i.index + 1}</td>
                    <td><img src="${pop.image}" width="100" /></td>
                    <td>${pop.title}</td>
                    <td>${pop.url}</td>
                    <td>${pop.type==1?'Người mua':'Người bán'}</td>
                    <td> <jsp:setProperty name="date" value="${pop.time}" property="time"/>
                        <fmt:formatDate value="${date}" pattern="HH:mm dd/MM/yyyy" type="date"></fmt:formatDate></td>
                        <td style="vertical-align: middle; text-align: center;">
                            <a href="javascript:void(0);" onclick="popouthome.changeActive('${pop.id}');" editStatus="${pop.id}">
                            <c:if test="${!pop.active}">
                                <img src="${staticUrl}/cp/img/icon-disable.png" />
                            </c:if>
                            <c:if test="${pop.active}">
                                <img src="${staticUrl}/cp/img/icon-enable.png" />
                            </c:if>
                        </a>
                    </td>
                    <td style="vertical-align: middle">
                        <div class="btn-group">
                            <button onclick="popouthome.edit('${pop.id}')" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span> Sửa</button>         
                            <button onclick="popouthome.del('${pop.id}')" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> Xóa</button>
                        </div>
                    </td>

                </tr>
            </c:forEach>
        </c:if>
        <c:if test="${dataPage.dataCount <= 0}">
            <tr><td colspan="10" class='text-center text-danger'>Không tìm thấy dữ liệu tương ứng</td></tr>
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