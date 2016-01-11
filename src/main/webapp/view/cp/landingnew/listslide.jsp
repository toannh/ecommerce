<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<ul class="nav nav-tabs" role="tablist">
    <li><a href="${baseUrl}/cp/landingnew.html"><i class="fa fa-list"></i> Danh sách Landing</a></li>
    <li class="active"><a href="${baseUrl}/cp/landingnew.html?landingid=${landing.id}">Danh sách Landing Slide</a></li>
</ul>
<div class="func-container">
    <div style="margin-top: 10px;">
        <div class="clearfix"></div>
    </div>
    <table class="table table-striped table-bordered table-responsive tbl-heartbanner" style="margin-top: 10px">
        <tr>
            <th style="vertical-align: middle; text-align: center; ">Mã danh mục</th>
            <th style="vertical-align: middle; text-align: center; ">Name</th>
            <th style="vertical-align: middle; text-align: center; ">Ảnh</th>
            <th style="vertical-align: middle; text-align: center; ">Vị trí</th>
            <th style="vertical-align: middle; text-align: center; ">Trạng thái</th>
            <th style="text-align: center; width: 100px;">
                <button onclick="landingnew.addslide('${landingid}');" type="button" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Thêm mới</button>
            </th>
        </tr>     
        <c:if test="${dataPage==null}">
            <tr><td colspan="6" class="clr-red fs-18">Chưa có danh mục nào cho landing !</td></tr>
        </c:if>
        <c:forEach items="${dataPage.data}" var="cate">
            <tr>
                <td class="text-center">
                    ${cate.id}</td>
                <td class="text-center">
                    ${cate.name}
                </td>
              <td class="text-center">
                    <img src="${cate.image==null?'':cate.image}" width="100" />
                </td>
                <td class="text-center">
                    <input name="position" for="${cate.id}" class="form-control" style="width:50px;" onchange="landingnew.changePositionSlide('${cate.id}');" value="${cate.position}"/>
                </td>
                <td class="text-center">
                    <a href="javascript:void(0);" onclick="landingnew.changeActiveSlide('${cate.id}');" editStatus="${cate.id}">
                        <c:if test="${!cate.active}">
                            <img src="${staticUrl}/cp/img/icon-disable.png" />
                        </c:if>
                        <c:if test="${cate.active}">
                            <img src="${staticUrl}/cp/img/icon-enable.png" />
                        </c:if>
                    </a>
                </td>
                <td style="vertical-align: middle; text-align: center">
                    <button style="width: 100%" onclick="landingnew.editslide('${cate.id}')" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-edit"></span>Sửa</button>
                    <button style="width: 100%; margin-top: 5px" onclick="landingnew.cropSlideImage('${cate.id}')" type="button" class="btn btn-info"><span class="glyphicon glyphicon-edit"></span>Crop ảnh</button>
                    <button style="width: 100%; margin-top: 10px;" onclick="landingnew.delSlideD('${cate.id}')" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> Xoá</button>
                </td>
            </tr>   
        </c:forEach>
    </table>
    <div style="margin-top: 10px;">
        <div class="clearfix"></div>
    </div>
</div>