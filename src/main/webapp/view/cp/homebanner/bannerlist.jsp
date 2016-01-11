<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<ul class="nav nav-tabs" role="tablist">
    <li class="active">
        <a>
            <i class="fa fa-tags"></i>
            Danh sách Home Banner
        </a>
    </li>
</ul>
<div class="func-container">
    <div class="nav form-group" style="padding:0px 0px 10px 0px;">
        <div class="col-sm-1" >
            <button class="btn btn-success" onclick="homebanner.saveBanner('Thêm mới')" >
                <span class="glyphicon glyphicon-plus"></span>Thêm Mới</button>
        </div>
    </div>

    <table class="table table-striped table-bordered table-responsive" style="margin-top: 10px">
        <tr>
            <th class="text-center" >STT</th>
            <th class="text-center" >Thông tin</th>
            <th class="text-center" >Vị trí</th>
            <th class="text-center" >Visited</th>
            <th class="text-center" >Banner hiện tại</th>
            <th class="text-center" >Trang thái</th>
            <th class="text-center" style="width: 155px;">Thao tác</th>
        </tr>
        <c:forEach var="banner" items="${lst}" varStatus="stt" >
            <tr class="active" style="text-align: center;">
                <td>${stt.index+1}</td>
                <td class="text-left">
                    <p>${banner.name}</p>
                    <a href='${banner.url}' target="_blank">${banner.url}</a>
                </td>
                <td>${banner.position==4?'Banner center':banner.position}</td>
                <td>${banner.visited}</td>
                <td style="text-align: left" >
                    <div class="form-group" >
                        <div class="col-sm-8" >
                            <img src="${banner.image}" />    
                        </div>
                    </div>
                </td>
                <td>
                    <a href="javascript:void(0);" onclick="homebanner.activeBanner('${banner.id}');" editStatus="${banner.id}">
                        <c:if test="${!banner.active}">
                            <img src="${staticUrl}/cp/img/icon-disable.png" />
                        </c:if>
                        <c:if test="${banner.active}">
                            <img src="${staticUrl}/cp/img/icon-enable.png" />
                        </c:if>
                    </a>         
                </td>
                <td style="vertical-align: middle">
                    <div class="btn-group">
                        <button onclick="homebanner.editBanner('Sửa', '${banner.id}')" class="btn btn-primary">
                            <span class="glyphicon glyphicon-edit" ></span>Đổi
                        </button>
                        <button onclick="homebanner.deleteBanner('${banner.id}')" class="btn btn-danger">
                            <span class="glyphicon glyphicon-trash" ></span>Xóa
                        </button>
                    </div>
                </td>
            </tr>
        </c:forEach>

    </table>
</div>