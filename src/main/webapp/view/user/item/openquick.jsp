<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="text" uri="http://chodientu.vn/text" %>
<%@ taglib prefix="url" uri="http://chodientu.vn/url" %>

<div class="col-lg-9 col-md-9 col-sm-12 col-xs-12 slidebar-right">
    <ol class="breadcrumb">
        <li><span class="glyphicon glyphicon-home"></span>  <a href="${baseUrl}">Trang chủ</a></li>
        <li>
            <a href="${baseUrl}/user/profile.html" target="_blank" >
                ${viewer.user.username==null?viewer.user.email:viewer.user.username}
            </a>
        </li>
        <li class="active">Đăng bán sản phẩm</li>
    </ol>
    <h1 class="title-pages">Đăng bán sản phẩm</h1>
    <div class="tabs-content-user">
        <div class="tabs-intro"><a href="${baseUrl}/tin-tuc/huong-dan-dang-ban-nhanh-42616303723.html" target="_blank"><span class="icon16-faq"></span>Hướng dẫn đăng  bán nhanh
            </a></div>
        <ul class="tab-title-content">
            <li><a href="${baseUrl}/user/dang-ban.html">Đăng 1 tin bán</a></li>
            <li class="active"><a>Đăng nhanh</a></li>
        </ul>
        <div class="tabs-content-block">
            <div class="row">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <p class="mgt-15"> • Chức năng này phù hợp với việc đăng bán cùng một lúc nhiều sản phẩm với các thông tin cơ bản nhất, giúp người bán tiết kiệm thời gian tổ chức dữ liệu bán hàng. <br>
                        • Hình thức thu tiền và chính sách vận chuyển của người bán được mặc định sử dụng theo <a href="${baseUrl}/user/cau-hinh-tich-hop.html">chế độ cài đặt hiện tại</a>. Nếu bạn chưa cấu hình, ChợĐiệnTử sẽ mặc định người mua phải liên hệ trực tiếp với người bán để làm rõ phí vận chuyển và hình thức thanh toán.
                    </p>
                    <p class="mgt-15"><strong>Phí kích hoạt và sử dụng chức năng: <span class="clr-red">100.000 xèng</span></strong> (tương đương <span class="clr-red">100.000 đ</span>)</p>

                    <div class="mgt-25 clearfix"><strong>Kích hoạt: </strong> <button type="button" class="btn btn-danger btn-lg" onclick="additemquick.activeNow();">Thanh toán bằng xèng</button></div>                                    
                </div>
            </div>
        </div>
    </div>
</div>
