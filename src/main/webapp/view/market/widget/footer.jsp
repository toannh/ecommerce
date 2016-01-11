<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib  prefix="text" uri="/WEB-INF/taglib/text.tld" %>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib  prefix="url" uri="/WEB-INF/taglib/url.tld" %>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script> 

<div class="footer-text">
    <div class="container">
        <div class="ft-left">   
            <p><b>Hà Nội:</b> Tầng 12A, tòa nhà 18 Tam Trinh, Hai Bà Trưng</p>
            <p>Tel: 1900-585-888 (Nhánh 107 & 117), Fax: 04-3632-0987 <a title="Bản đồ đường đi" style="cursor:pointer" onclick="item.getMaps('18 Tam Trinh,Hoàng Mai, Hà Nội');" data-toggle="modal" data-target="#ModalNormal">[Bản đồ đường đi]</a></p>
        </div><!--ft-left-->
        <div class="ft-right">
            <p><b>TP.HCM:</b> Lầu 3, tòa nhà VTC online, 132 Cộng Hòa, P4, Q. Tân Bình</p>
            <p>Tel: 1900-585-888 (Nhánh 117), Fax: 08- 3811-4757 <a title="Bản đồ đường đi" style="cursor:pointer" onclick="item.getMaps('132 Cộng Hòa, phường 4, quận Tân Bình, Thành Phố Hồ Chí Minh');" data-toggle="modal" data-target="#ModalNormal">[Bản đồ đường đi]</a></p>
        </div><!--ft-right-->
    </div><!--container-->
</div><!--footer-text-->
<div class="footer">
    <div class="container">
        <div class="footer-row">
            <div class="footer-left">
                <div class="row">
                    <h4>Giới thiệu CĐT</h4>
                </div>
                <div class="row">
                    <p>Bản quyền © 2006-2013. Chodientu.vn - Sàn mua bán nhiều hàng số 1 Việt Nam</p>
                    <p>Giấy phép số 69/GP-BC (Bộ VHTT) và 321/GP-BBCVT (Bộ BCVT). Chịu trách nhiệm: <b>ông Nguyễn Hòa Bình.</b></p>
                </div>
                <div class="row">
                    <a title="Giới thiệu ChợĐiệnTử" href="${baseUrl}/tin-tuc/gioi-thieu-ve-chodientu-152859779631.html">Giới thiệu ChợĐiệnTử</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Quy chế hoạt động" href="${baseUrl}/tin-tuc/quy-che-hoat-dong-606178473238.html" rel="nofollow">Quy chế hoạt động</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Thoả thuận người dùng" href="${baseUrl}/tin-tuc/thoa-thuan-nguoi-dung-578158007467.html" rel="nofollow">Thoả thuận người dùng</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Tin tức" href="${baseUrl}/tin-tuc/tin-tuc-su-kien/891162137765.html" rel="nofollow">Tin tức</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Tuyển dụng" href="${baseUrl}/tin-tuc/tuyen-dung/858333062432.html" rel="nofollow">Tuyển dụng</a> 
                </div>
                <div class="footer-line"></div>
                <div class="row">   
                    <h4>Dành cho người mua</h4>
                </div>
                <div class="row">
                    <a title="Tài khoản thành viên" href="${baseUrl}/tin-tuc/tai-koan-thanh-vien/721647942898.html" rel="nofollow">Tài khoản thành viên</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Quy định người mua" href="${baseUrl}/tin-tuc/quy-dinh-danh-cho-nguoi-mua--610135373421.html" rel="nofollow">Quy định người mua</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Hướng dẫn mua hàng" href="${baseUrl}/tin-tuc/huong-dan-mua-hang/824548042719.html" rel="nofollow">Hướng dẫn mua hàng</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Mua hàng an toàn" href="${baseUrl}/tin-tuc/mua-hang-an-toan-391746877225.html" rel="nofollow">Mua hàng an toàn</a>
                    <!--&nbsp;&nbsp;|&nbsp;&nbsp;<a href="${baseUrl}/tim-kiem-pho-bien.html">Tìm kiếm sản phẩm</a>-->
                </div>
                <div class="row">
                    <h4>Dành cho người bán</h4>
                </div>
                <div class="row">
                    <a title="Quy định người bán" href="${baseUrl}/tin-tuc/quy-dinh-danh-cho-nguoi-ban-334803218827.html" rel="nofollow">Quy định người bán</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Tạo & sử dụng Shop" href="${baseUrl}/tin-tuc/huong-dan-tao---su-dung-shop/640169436057.html" rel="nofollow">Tạo & sử dụng Shop</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Quảng cáo tăng doanh số" href="${baseUrl}/tin-tuc/tai-khoan-xeng/404566202963.html" rel="nofollow">Quảng cáo tăng doanh số</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a title="Hướng dẫn bán hàng" href="${baseUrl}/tin-tuc/huong-dan-ban-hang/562050525233.html" rel="nofollow">Hướng dẫn bán hàng</a>
                </div>
                <div class="footer-line"></div>
                <div class="row">
                    <div class="footer-partners">
                        <h4><label>Đầu tư và hợp tác bởi:</label></h4>
                        <span class="icon-idg"></span>
                        <span class="icon-softbank"></span>
                        <span class="icon-ebay"></span>
                    </div><!--footer-partners-->
                </div>
                <div class="footer-line"></div>
                <div class="row">   
                    <h4>Liên kết website</h4>
                </div>
                <div class="row">
                    <div class="footer-product">
                        <a title="ebay.vn" href="http://www.ebay.vn" target="_blank"><span class="icon-ebayvn"></span></a>
                        <a title="naima.vn" href="http://naima.vn" target="_blank"><span class="icon-naima"></span></a>
                        <a title="chongiadung.com" href="http://chongiadung.com" target="_blank"><span class="icon-chongia"></span></a>
                        <a title="thanhtoanonline.vn" href="http://thanhtoanonline.vn" target="_blank"><span class="icon-thanhtoan"></span></a>
                        <a title="nganluong.vn" href="http://nganluong.vn" target="_blank"><span class="icon-nganluong36"></span></a>
                        <a title="shipchung.vn" href="http://shipchung.vn" target="_blank"><span class="icon-shipchung36"></span></a>
                        <a title="adnet.vn" href="http://adnet.vn" target="_blank"><span class="icon-adnet"></span></a><br/>
                    </div>
                </div>
                <div class="row">   
                    <h4>Kết nối với chúng tôi</h4>
                </div>
                <div class="row">
                    <div class="footer-social">
                        <a href="https://www.facebook.com/chodientu?fref=ts" rel="nofollow"><i class="fa fa-facebook-square"></i></a>
                        <a href="https://twitter.com/cdt_vn" rel="nofollow"><i class="fa fa-twitter-square"></i></a>
                        <a href="https://plus.google.com/111916099110283531711" rel="nofollow"><i class="fa fa-google-plus-square"></i></a>
                        <a href="https://www.pinterest.com/cdtebay/" rel="nofollow"><i class="fa fa-pinterest-square"></i></a>
                    </div>
                </div>
            </div><!--footer-left-->
            <div class="footer-right">
                <div class="row">   
                    <h4>Chấp nhận thanh toán</h4>
                </div>
                <div class="row">
                    <a title="NgânLượng.vn" class="ficon-nganluong" href="http://nganluong.vn/" target="_blank"><span class="icon-nganluong60"></span></a>
                    <ul class="ul-bank">
                         <li><span title="Thẻ thanh toán VisaCard" rel="nofollow" class="visacrd"></span></li>
                         <li><span title="Thẻ thanh toán MasterCard" rel="nofollow" class="mastercrd"></span></li>
                         <li><span title="Ngân hàng TMCP Ngoại Thương Việt Nam" rel="nofollow" class="vietcombank"></span></li>
                         <li><span title="Ngân hàng TMCP Kỹ Thương Việt Nam" rel="nofollow" class="techbk"></span></li>
                         <li><span title="Ngân hàng Đông Á" rel="nofollow" class="dongabk"></span></li>
                         <li><span title="Ngân hàng TMCP Công Thương Việt Nam" rel="nofollow" class="vietinbk"></span></li>
                         <li><span title="Ngân hàng Quốc Tế" rel="nofollow" class="vibk"></span></li>
                         <li><span title="Ngân hàng TMCP Sài Gòn" rel="nofollow" class="shbk"></span></li>
                         <li><span title="Ngân hàng Á Châu" rel="nofollow" class="acbbk"></span></li>
                         <li><span title="Ngân hàng TMCP Sài Gòn Thương Tín" rel="nofollow" class="sacombk"></span></li>
                         <li><span title="Ngân hàng Đầu Tư và Phát Triển Việt Nam" rel="nofollow" class="bidvbk"></span></li>
                         <li><span title="Ngân hàng Nông Nghiệp và Phát Triển Nông Thôn Việt Nam" rel="nofollow" class="agrbk"></span></li>
                         <li><span title="Ngân hàng Quân Đội" rel="nofollow" class="mbk"></span></li>
                         <li><span title="Ngân hàng Xuất Nhập Khẩu Việt Nam" rel="nofollow" class="exembank"></span></li>
                         <li><span title="Ngân hàng Việt Nam Thịnh Vượng" rel="nofollow" class="vpbk"></span></li>
                         <li><span title="Ngân hàng TMCP Đông Nam Á" rel="nofollow" class="seabk"></span></li>
                         <li><span title="Ngân hàng Bắc Á" rel="nofollow" class="bacabk"></span></li>
                         <li><span title="Ngân hàng TMCP Hàng Hải Việt Nam" rel="nofollow" class="maritbk"></span></li>
                         <li><span title="Ngân hàng TMCP Xăng Dầu Petrolimex Việt Nam" rel="nofollow" class="pgbk"></span></li>
                         <li><span title="Ngân hàng TMCP Đại Dương" rel="nofollow" class="oceanbank"></span></li>
                         <li><span title="Ngân hàng TMCP Phát Triển TP.HCM" rel="nofollow" class="hdbank"></span></li>
                         <li><span title="Ngân hàng Dầu Khí Toàn Cầu" rel="nofollow" class="gpbank"></span></li>
                         <li><span title="Ngân hàng TMCP Việt Á" rel="nofollow" class="vietabank"></span></li>
                         <li><span title="Ngân hàng Quốc Dân" rel="nofollow" class="nvibank"></span></li>
                    </ul>
                </div>
                <div class="footer-line"></div>
                <div class="row">   
                    <h4>Đối tác vận chuyển</h4>
                </div>
                <div class="row">   
                    <a title="ShipChung.vn" class="ficon-shipchung" href="http://shipchung.vn/" target="_blank"><span class="icon-shipchung60"></span></a> 
                    <ul class="ul-ship">
                        <li><span class="ems"></span></li>
                        <li><span class="kerry"></span></li>
                        <li><span class="nasco"></span></li>
                        <li><span class="netco"></span></li>
                        <li><span class="viettelpost"></span></li>
                        <li><span class="vietnampost"></span></li>
                        <li><span class="giaohangnhanh"></span></li>
                        <li><span class="ghtk"></span></li>
                    </ul>  
                </div>
                <div class="footer-line"></div>
                <div class="row">
                    <form class="form-horizontal" id="email-add-form-footer" method="POST">
                        <div class="row">   
                            <h4>Đăng ký nhận tin</h4>
                        </div>
                        <div class="row has-error"> 
                            <span class="text-danger" for="errorEmail"></span>
                            <div class="box-email">
                                <div class="be-inner"><input name="email" type="text" class="text" placeholder="Nhập địa chỉ Email của bạn để đăng ký nhận bản tin" /></div>
                                <input type="button" class="btn-email" value="Đăng ký" onclick="auth.addEmailFooterMarket();" />
                            </div><!--box-email-->
                        </div>
                    </form>
                </div>
                <div class="row">   
                    <div class="fb-like" data-href="https://www.facebook.com/chodientu" data-width="300" data-layout="standard" data-action="like" data-show-faces="true" data-share="true"></div>
                </div>
            </div><!--footer-right-->
        </div><!--footer-row-->
        <div class="footer-keyword">
            <a href="http://www.online.gov.vn/HomePage/WebsiteDisplay.aspx?DocId=58" class="footer-verify" rel="nofollow" target="_blank">
                <img src="${staticUrl}/market/images/web-register.png" alt="eBay verify">
            </a>
            <div class="fk-inner">
                <b>XU HƯỚNG TÌM KIẾM: </b>
                <c:forEach items="${footerkeywords}" var="fk" varStatus="stt">
                    <a href="${fk.url}" title="${fk.keyword}">${fk.keyword}</a> <c:if test="${!stt.last}">|&nbsp;</c:if>
                </c:forEach>
            </div>
        </div>

    </div><!--container-->
</div><!--footer-->
<div class="footer-mobile">
    <p>Bản quyền © 2006-2014. Liên doanh với eBay-Uy tín số 1</p>
    <p>Giấy phép số 69/GP-BC (Bộ VHTT) và 321/GP-BBCVT (Bộ BCVT). Chịu trách nhiệm: ông <b>Nguyễn Hòa Bình.</b></p>
    <p>Hotline: 1900-585-888  |  E-mail: support@chodientu.vn</p>
</div><!--footer-mobile-->
<div class="go-top"></div>

<div class="modal fade" id="ModalNormal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            </div>
            <div class="modal-body">
                <div id="map-canvas"></div>
            </div><!-- end modal-body -->
        </div><!-- end modal-content -->
    </div><!-- end modal-dialog -->
</div><!-- end Modal -->
<c:if test="${checkMobile}">
<nav id="menu" style="display: none" >
    <ul>
        <c:forEach var="item" items="${categories}" >
            <li>
                <a href="${url:browse(item.id, item.name)}" title="${item.name}" >
                    ${item.name}
                </a>
            </li>
        </c:forEach>
        <li><a href="${baseUrl}/danh-muc-san-pham.html">Xem tất cả</a></li>
        <li class="white-li"><a href="${baseUrl}/hotdeal.html">Hotdeal</a></li>
        <li class="white-li"><a href="${baseUrl}/tin-tuc/huong-dan-nguoi-mua/824548042719.html">Hướng dẫn</a></li>
        <li class="white-li"><a rel="nofollow">Liên hệ</a></li>
    </ul>
</nav>
        </c:if>
<c:if test="${viewer == null || viewer.user == null && 1==2}">
    <nav id="user" style="display: none" >
        <ul>
            <li> <a title="Đăng nhập" onclick="auth.login('${requestScope['javax.servlet.forward.request_uri']}')">Đăng nhập</a> </li>
            <li><a title="Đăng ký" href="${baseUrl}/user/signup.html">Đăng ký</a></li>
            <li class="white-li"><a title="Đăng bán" target="_blank" href="${baseUrl}/user/dang-ban.html">Đăng bán</a></li>
        </ul>
    </nav>
</c:if>
<c:if test="${viewer != null && viewer.user != null}">
    <nav id="user" style="display: none" >
        <ul>
            <li>
                <a href="${baseUrl}/user/profile.html" title="Thông tin cá nhân" >${viewer.user.username == null?viewer.user.email:viewer.user.username}</a>
                <ul>
                    <li><a href="${baseUrl}/user/profile.html" title="Thông tin cá nhân" >Trang cá nhân</a></li>
                    <li><a>Tin nhắn (0)</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:;">Mua hàng</a>
                <ul>
                    <li><a title="Đơn hàng của tôi" href="${baseUrl}/user/don-hang-cua-toi.html">Đơn hàng của tôi</a></li>
                    <li><a title="Sản phẩm đang theo dõi" href="${baseUrl}/user/theo-doi-dau-gia.html">Sản phẩm đang theo dõi</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript:;">Bán hàng</a>
                <ul>
                    <li><a title="Đăng bán sản phẩm" href="${baseUrl}/user/dang-ban.html" >Đăng bán</a></li>
                    <li><a title="Danh sách sản phẩm" href="${baseUrl}/user/item.html" >Danh sách sản phẩm</a></li>
                    <li><a title="Hoá đơn bán hàng" href="${baseUrl}/user/hoa-don-ban-hang.html">Hoá đơn bán hàng</a></li>
                    <li><a title="Quản lý up tin" href="${baseUrl}/user/posting.html" >Quản lý up tin</a></li>
                    <li><a title="Quản lý tin VIP" href="${baseUrl}/user/vipitem.html" >Quản lý tin VIP</a></li>
                        <c:if test="${marketshop == null}">
                        <li>
                            <a title="Mở shop" href="${baseUrl}/user/open-shop-step1.html">Mở shop</a>
                        </li>
                    </c:if>
                    <c:if test="${marketshop != null}">
                        <li>
                            <a title="Quản trị Shop" href="${baseUrl}/user/cau-hinh-shop-step1.html">Quản trị Shop</a>
                        </li>
                    </c:if>
                </ul>
            </li>
            <li class="white-li"><a href="${baseUrl}/user/signout.html" >Đăng xuất</a></li>
        </ul>
    </nav>
</c:if>