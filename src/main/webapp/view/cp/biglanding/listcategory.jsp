<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<ul class="nav nav-tabs" role="tablist">
    <li><a href="${baseUrl}/cp/biglanding.html"><i class="fa fa-list"></i> Danh sách Landing</a></li>
    <li class="active"><a href="${baseUrl}/cp/landing.html?landingid=${landing.id}">Danh sách danh mục landing</a></li>
</ul>
<div class="func-container">
    <div style="margin-top: 10px;">
        <div class="clearfix"></div>
    </div>
    <table class="table table-striped table-bordered table-responsive tbl-heartbanner" style="margin-top: 10px">
        <tr>
            <th style="vertical-align: middle; text-align: center; ">Mã danh mục</th>
            <th style="vertical-align: middle; text-align: center; ">Tên danh mục</th>
            <th style="vertical-align: middle; text-align: center; ">Template</th>
            <th style="vertical-align: middle; text-align: center; ">Vị trí</th>
            <th style="vertical-align: middle; text-align: center; ">Trạng thái</th>
            <th style="text-align: center; width: 100px;">
                <button onclick="biglanding.addCategory('${biglandingid}');" type="button" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Thêm mới</button>
            </th>
        </tr>     
        <c:if test="${fn:length(listCategories) <=0}">
            <tr><td colspan="4" class="clr-red fs-18">Chưa có danh mục nào cho landing ${biglandingid}!</td></tr>
        </c:if>
        <c:forEach items="${listCategories}" var="cate">
            <tr>
                <td class="text-center">
                    <a href="${baseUrl}/cp/biglanding.html?biglandingid=${biglandingid}&biglandingcate=${cate.id}" class="btn btn-primary btn-xs">Quản lý danh mục con</a>
                <td class="text-center">
                    ${cate.name}
                </td>
                <td class="text-center">
                    <p>${cate.template}</p>
                    <img src="${staticUrl}/cp/images/template/${landing.landingTemplate}/box/${cate.template}.png" width="300" />
                </td>
                <td class="text-center">
                    <input name="position" for="${cate.id}" class="form-control" style="width:50px;" onchange="biglanding.changePositionCate('${cate.id}');"  value="${cate.position}"/>
                </td>
                <td class="text-center">
                    <a href="javascript:void(0);" onclick="biglanding.changeActiveCate('${cate.id}');" editStatus="${cate.id}">
                        <c:if test="${!cate.active}">
                            <img src="${staticUrl}/cp/img/icon-disable.png" />
                        </c:if>
                        <c:if test="${cate.active}">
                            <img src="${staticUrl}/cp/img/icon-enable.png" />
                        </c:if>
                    </a>
                </td>
                <td style="vertical-align: middle; text-align: center">
                    <button style="width: 100%" onclick="biglanding.editCategory('${cate.id}')" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span>Sửa</button>
                    <button style="width: 100%; margin-top: 10px;" onclick="biglanding.delbiglandingcate('${cate.id}')" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> Xoá</button>
                </td>
            </tr>   
        </c:forEach>
    </table>
    <div style="margin-top: 10px;">
        <div class="clearfix"></div>
    </div>
</div>