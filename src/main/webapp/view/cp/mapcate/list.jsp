<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld"%>
<ul class="nav nav-tabs" role="tablist">
    <li class="active">
        <a>
            <i class="fa"></i>
            Lịch sử map danh mục
        </a>
    </li>
</ul>
<div class="func-container" >
    <form:form modelAttribute="categoryMappingSearch" cssClass="form-vertical" method="POST" role="form" style="margin-top: 35px;">
        <div class="col-sm-3">
            <div class="form-group">
                <form:input path="originCategoryId" type="text" class="form-control" placeholder="Mã danh mục nguồn"/>
            </div>
            <div class="form-group">
                <form:select path="running" type="text" class="form-control">
                    <form:option value="0" label="-- Chọn trạng thái --" />
                    <form:option value="1" label="Chưa chạy" />
                    <form:option value="2" label="đang chạy" />
                    <form:option value="3" label="Lỗi" />
                    <form:option value="4" label="Chạy thành công" />
                </form:select>
            </div>
            <div class="form-group ">
                <button type="submit"  class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Lọc </button>
                <button onclick="mapcate.add();" type="button" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Thêm mới</button>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="form-group">
                <form:input path="destCategoryId" type="text" class="form-control" placeholder="Mã danh mục đích"/>
            </div>
            <div class="form-group">
                <form:input path="userId" type="text" class="form-control" placeholder="Nhập mã người tạo" />               
            </div>
        </div>
        <div class="col-sm-3">
            <div class="form-group">
                <form:select path="active" type="text" class="form-control">
                    <form:option value="-1" label="-- Chọn trạng thái --" />
                    <form:option value="1" label="hoạt động" />
                    <form:option value="0" label="Không kích hoạt" />
                </form:select>
            </div>
        </div>
        <div class="col-sm-3">
            <div class="form-group"> 
                <%--<form:input path="createTimeTo" type="hidden" class ="form-control timeselect" placeholder="Ngày tạo đến"/>--%>
            </div>
        </div>
        <div class="clearfix" style="margin-bottom: 20px;"></div>
    </form:form>
    <div class="cms-line"></div>
    <div class="clearfix"></div>
    <div style="margin-top: 20px;">
        <h5 class="pull-left" style="padding: 10px; width: 33%;" >
            Tìm thấy <strong>${text:numberFormat(dataPage.dataCount)} </strong> kết quả <strong>${text:numberFormat(dataPage.pageCount)}</strong> trang.
        </h5>
        <div class="btn-toolbar pull-right">
            <jsp:include page="/view/cp/widget/paging.jsp">
                <jsp:param name="pageIndex" value="${dataPage.pageIndex}"/>
                <jsp:param name="pageCount" value="${dataPage.pageCount}"/>
            </jsp:include>
        </div>
        <div class="clearfix"></div>
    </div>
    <table class="table table-striped table-bordered table-responsive tbl-heartbanner" style="margin-top: 10px">
        <tr>
            <th style="vertical-align: middle; text-align: center; ">STT</th>
            <th style="vertical-align: middle; text-align: center; ">Category nguồn</th>
            <th style="vertical-align: middle; text-align: center; ">Category đích</th>
            <th style="vertical-align: middle; text-align: center; ">Danh sách thuộc tính</th>
            <th style="vertical-align: middle; text-align: center; ">Người tạo</th>
            <th style="vertical-align: middle; text-align: center; ">Thời gian</th>
            <th style="vertical-align: middle; text-align: center; ">Trạng thái</th>
            <th style="vertical-align: middle; text-align: center; ">Kích hoạt</th>
            <th style="vertical-align: middle; text-align: center; ">Chức năng</th>
        </tr>
        <c:forEach items="${dataPage.data}" var="cateMapp" varStatus="cateMapStatus">
            <tr id ="${cateMapp.id}" class="${!cateMapp.active? 'danger':''}">
                <td>${cateMapStatus.index+1}</td>
                <td>
                    <p>
                        ${cateMapp.oriCate.name} </br> ${cateMapp.originCategoryId}
                    </p>
                </td>
                <td>
                    <p>
                        ${cateMapp.destCate.name} </br> ${cateMapp.destCategoryId}
                    </p>
                </td>
                <td>
                    <!-- < c:forEach items="$ {cateMapp.listSameCateProp}" var="cateProp" varStatus="catePropStatus">
                        <p>
                            $ {catePropStatus.index+1}.$ {cateProp.name}($ {cateProp.id})
                        </p>
                    < /c:forEach> -->
                </td>
                <td>
                    <p>
                        ${cateMapp.user.email} </br> ${cateMapp.userId}
                    </p>
                </td>
                <td>
                    <jsp:useBean id="date" class="java.util.Date" />
                    <p>Tạo: <br/>
                        <jsp:setProperty name="date" property="time" value="${cateMapp.createTime}" />                            
                        <strong><fmt:formatDate type="date" pattern="HH:mm dd/MM/yyyy"  value="${date}"></fmt:formatDate></strong>
                        </p>

                        <p>Bắt đầu chạy: <br/>
                        <c:if test="${cateMapp.startTime > 0}" >
                        <p>Cập nhật <br/>
                            <jsp:setProperty name="date" property="time" value="${cateMapp.startTime}" />                            
                            <strong><fmt:formatDate type="date" pattern="HH:mm dd/MM/yyyy"  value="${date}"></fmt:formatDate></strong>
                            </p>
                    </c:if>  
                    <c:if test="${cateMapp.endTime > 0}" >
                        <jsp:setProperty name="date" property="time" value="${cateMapp.endTime}" />
                        <strong><fmt:formatDate type="date" pattern="HH:mm dd/MM/yyyy"  value="${date}"></fmt:formatDate></strong>
                            </p>
                    </c:if>  
                </td>
                <td class="text-center" style="vertical-align: middle" >
                    <c:if test="${cateMapp.running == 'EVERRUN'}"><span  class="label label-default">Chưa chạy</span></c:if>
                    <c:if test="${cateMapp.running == 'RUNNING'}"><span  class="label label-success">Đang chạy</span></c:if>
                    <c:if test="${cateMapp.running == 'SUCCESS'}"><span  class="label label-success">Chạy xong</span></c:if>
                    <c:if test="${cateMapp.running == 'FAIL'}"><span class="label label-danger">Bị Lỗi</span></c:if>
                </td>
                <td class="text-center" style="vertical-align: middle" >

                    <c:if test="${cateMapp.active}"><span  role="statusText" class="label label-success">Đang hoạt động</span></c:if>
                    <c:if test="${!cateMapp.active}"><span role="statusText" class="label label-danger">Tạm dừng</span></c:if>
                </td>
                <td class="text-center" style="vertical-align: middle" >
                        <button type="submit" onclick="mapcate.changeActive(${cateMapp.id});" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Chang Active </button>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${dataPage.dataCount <= 0}">
            <tr>
                <td class="text-danger text-center" colspan="4">Không tìm thấy lệnh map danh mục nào</td>
            </tr>
        </c:if>
    </table>
    <div style="margin-top: 10px; margin-bottom:20px">
        <div class="btn-toolbar pull-right">
            <jsp:include page="/view/cp/widget/paging.jsp">
                <jsp:param name="pageIndex" value="${dataPage.pageIndex}"/>
                <jsp:param name="pageCount" value="${dataPage.pageCount}"/>
            </jsp:include>
        </div><div class="clearfix"></div>
    </div>
</div>