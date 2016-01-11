<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="container">
    <h4>Quản trị marketing người bán</h4>
</div>
<div class="container" style="margin-bottom: -1px;" >
    <div style="margin-top: 10px" >
        <ul class="nav nav-tabs nav-collapse">
            <li class="active"><a>Danh sách sms marketing</a></li>
        </ul>
    </div>
</div>
<div class="container" style="border: 1px solid #ccc;" >
    <form:form modelAttribute="sellerSmsSearch" cssClass="form-vertical" method="POST" role="form" style="margin-top: 20px;"> 
        <div class="col-sm-4">  
            <div class="form-group">
                <form:input path="id" type="text" class="form-control" placeholder="Mã sms marketing"/>
            </div>
        </div>
        <div class="col-sm-4">  
            <div class="form-group">
                <form:input path="name" type="text" class="form-control" placeholder="Tiêu đề tin nhắn"/>
            </div>
        </div>
        <div class="col-sm-4">  
            <div class="form-group">
                <form:input path="sendTimeForm" type="hidden" class="form-control timeFrom" placeholder="Ngày yêu cầu gửi từ"/>
            </div>
        </div>
        <div class="col-sm-4">  
            <div class="form-group">
                <form:input path="sendTimeTo" type="hidden" class="form-control timeTo" placeholder="Ngày yêu cầu gửi đến"/>
            </div>
        </div>
        <div class="col-md-4">
            <div class="form-group">
                <form:select  path="run" class="form-control">
                    <form:option value="0">-- Trạng thái --</form:option>
                    <form:option value="1">Đã chạy</form:option>
                    <form:option value="2">Chưa chạy</form:option>
                </form:select>
            </div>
        </div>
        <div class="col-sm-4">  
            <div class="form-group">
                <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Lọc thông tin</button>
            </div>
        </div>
    </form:form>
    <div class="clearfix"></div>
    <div style="margin-top: 10px">
        <h5 class="pull-left alert alert-success" style="padding: 10px;" >Tìm thấy tổng số <span style="color: tomato; font-weight: bolder">${page.dataCount} </span> sms marketing trong <span style="color: tomato; font-weight: bolder">${page.pageCount}</span> trang.</h5>            
        <div class="btn-toolbar pull-right" style="padding: 15px 0px;">
            <jsp:include page="/view/cp/widget/paging.jsp">
                <jsp:param name="pageIndex" value="${page.pageIndex}"/>
                <jsp:param name="pageCount" value="${page.pageCount}"/>
            </jsp:include>
        </div>
        <div class="clearfix"></div>
    </div>
    <table class="table table-striped table-bordered table-responsive" style="margin-top: 10px">
        <tr>
            <th class="text-center">Mã</th>
            <th class="text-center">Nội dung</th>
            <th class="text-center" style="width: 150px;" >Ngày tạo</th>
            <th class="text-center" style="width: 150px;" >Ngày gửi</th>
            <th class="text-center">Trạng thái</th>
            <th class="text-center">Duyệt</th>
            <th class="text-center" style="width: 250px;"> Chức năng </th>
        </tr>        
        <jsp:useBean id="date" class="java.util.Date"></jsp:useBean>
        <c:forEach var="item" items="${page.data}">  
            <tr class="${item.active?'success':'danger'}" >
                <td class="text-left">
                    ${item.id}
                </td>
                <td class="text-left">
                    <p style="max-width: 300px; max-height: 100px; overflow: auto" >
                        <p style="max-width: 300px; overflow: auto" ><strong>${item.name}</strong></p>
                        <p style="max-width: 300px; overflow: auto" >${item.content}</p>
                    </p>
                </td>
                <td class="text-center" >
                    <jsp:setProperty name="date" property="time" value="${item.createTime}" />
                    <fmt:formatDate type="date" pattern="HH:mm dd/MM/yyyy"  value="${date}"></fmt:formatDate>
                    </td>
                    <td class="text-center" >
                    <jsp:setProperty name="date" property="time" value="${item.sendTime}" />
                    <fmt:formatDate type="date" pattern="HH:mm dd/MM/yyyy"  value="${date}"></fmt:formatDate>
                    </td>
                    <td  class="text-center" style="vertical-align: middle"  >
                    ${!item.run?'<i class="glyphicon glyphicon-unchecked"></i>':'<i class="glyphicon glyphicon-check"></i>'}
                </td>
                <td rel-data-check="${item.id}" class="text-center" style="vertical-align: middle"  >
                    ${!item.active?'<i class="glyphicon glyphicon-unchecked"></i>':'<i class="glyphicon glyphicon-check"></i>'}
                </td>
                <td class="text-center"  style="vertical-align: middle" >
                    <div class="btn-group">
                        <c:if test="${item.active==false}">
                            <button rel-data-sms="${item.id}" style="width: 100px;" type="button" class="btn btn-success ${item.done?'disabled':''}"  onclick="sellerMarketing.activeSms('${item.id}', 'active')" >
                                <span class="glyphicon glyphicon-refresh"></span> Duyệt
                            </button>
                        </c:if>
                        <c:if test="${item.active==true}">
                            <button rel-data-sms="${item.id}" style="width: 100px;" type="button" class="btn btn-danger ${item.done?'disabled':''}"  onclick="sellerMarketing.activeSms('${item.id}', 'inactive')" >
                                <span class="glyphicon glyphicon-refresh"></span> Khóa
                            </button>
                        </c:if>
                        <button type="button" class="btn btn-primary" onclick="sellerMarketing.viewSmS('${item.phone}', '${item.name}', '${item.run}', '${item.done}')">
                            <span class="glyphicon glyphicon-tag"></span> Chi tiết
                        </button>
                    </div>
                </td>
            </tr>      
        </c:forEach>
    </table>
    <div style="margin-top: 10px; margin-bottom:10px">
        <div class="btn-toolbar pull-right">
            <jsp:include page="/view/cp/widget/paging.jsp">
                <jsp:param name="pageIndex" value="${page.pageIndex}"/>
                <jsp:param name="pageCount" value="${page.pageCount}"/>
            </jsp:include>
        </div>
        <div class="clearfix"></div>
    </div>
</div>