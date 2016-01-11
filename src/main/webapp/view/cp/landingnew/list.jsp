<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<ul class="nav nav-tabs" role="tablist">
    <li class="active">
        <a>
            <i class="fa fa-list"></i>
            Danh sách Landing new
        </a>
    </li>
</ul>
<div class="func-container">
    <table class="table table-striped table-bordered table-responsive tbl-heartbanner" style="margin-top: 10px">
        <tr>
            <th style="vertical-align: middle; text-align: center; ">Mã landing</th>
            <th style="vertical-align: middle; text-align: center; ">Ảnh banner</th>
            <th style="vertical-align: middle; text-align: center; ">Tên landing</th>
            <th style="vertical-align: middle; text-align: center; ">Thời gian</th>
            <th style="vertical-align: middle; text-align: center; ">Trạng thái</th>
            <th style="text-align: center; width: 100px;">
                <button onclick="landingnew.add();" type="button" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Thêm mới</button>
            </th>
        </tr>   
        <c:if test="${fn:length(dataPage.data) <= 0}">
            <tr><td colspan="5" class="clr-red fs-18">Chưa có landing nào!</td></tr>
        </c:if>
        <c:forEach items="${dataPage.data}" var="landing">
            <tr>
                <td class="text-center">
                    <a href="${baseUrl}/landingnew/${landing.id}/${text:createAlias(landing.name)}.html" target="_blank">${landing.id}</a><br/>
                    <a href="${baseUrl}/cp/landingnew.html?landingid=${landing.id}">Thay đổi slide</a>

                </td>
                <td class="text-center">
                    <image src="${landing.bannerCenter}" width="80" />                   
                </td>
                <td class="text-center">
                   ${landing.name}
                </td>
                <td>
                    <jsp:useBean id="date" class="java.util.Date" />
                    <jsp:setProperty name="date" property="time" value="${landing.time}" />
                    <strong><fmt:formatDate type="date" pattern="HH:mm dd/MM/yyyy"  value="${date}"></fmt:formatDate></strong>
                      
                    </td>

                    <td class="text-center">
                        <a href="javascript:void(0);" onclick="landingnew.changeActive('${landing.id}');" editStatus="${landing.id}">
                        <c:if test="${!landing.active}">
                            <img src="${staticUrl}/cp/img/icon-disable.png" />
                        </c:if>
                        <c:if test="${landing.active}">
                            <img src="${staticUrl}/cp/img/icon-enable.png" />
                        </c:if>
                    </a>
                </td>
                <td style="vertical-align: middle; text-align: center">
                    <button style="width: 100%" onclick="landingnew.edit('${landing.id}')" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span>Sửa</button>
                    <button style="width: 100%; margin-top: 10px;" onclick="landingnew.addItem('${landing.id}')" type="button" class="btn btn-default"><span class="glyphicon glyphicon-list"></span>Danh sách sản phẩm</button>
                    <button style="width: 100%; margin-top: 10px;" onclick="landingnew.del('${landing.id}')" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> Xoá</button>
                </td>
            </tr>   
        </c:forEach>
    </table>

</div>