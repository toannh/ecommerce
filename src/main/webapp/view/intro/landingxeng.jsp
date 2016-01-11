<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="text" uri="http://chodientu.vn/text"%>
<%@taglib  prefix="url" uri="http://chodientu.vn/url"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="author" content="Landing Xèng" />
    <meta name='COPYRIGHT' content='&copy; Landing Xèng' />
    <meta name="keywords" content="Landing Xèng, kiếm xèng, thưởng xèng, khuyến mãi xèng, " />
    <meta name="description" content="Xem chương trình Landing Xèng tại Chợ Điện Tử - Giá rẻ, nhiều khuyến mãi, thanh toán online, Cod, giao hàng toàn quốc, bảo vệ người mua." />
    <link rel="shortcut icon" href="images/favicon.png" />
    <title>Xem chương trình Landing Xèng tại ChợĐiệnTử eBay Việt Nam</title>
    <!-- Bootstrap core CSS -->
   <jwr:style src="/css/landingxeng.css"></jwr:style>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
      	<div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#cdt-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="http://chodientu.vn/"><img src="${baseUrl}/static/landingxeng/images/logo.png" alt="logo" /></a>
            </div>
            <div class="collapse navbar-collapse" id="cdt-collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#gioi-thieu">Giới thiệu</a></li>
                    <li><a href="#cach-kiem-xeng">Cách kiếm xèng</a></li>
                    <li><a href="#tro-giup">Trợ giúp</a></li>
                    <li class="li-more"><a target="_blank" href="http://chodientu.vn/tin-tuc/thong-bao--chuong-trinh-thuong-xeng---gan-ket-thanh-vien-82930526079.html?rel=notification&code=82930526079">Tìm hiểu thêm</a></li>
                </ul>
            </div><!-- /.navbar-collapse -->
      	</div><!-- /.container -->
    </nav>
    <div class="box-slider">
        <div class="bs-img"></div>
        <div class="bs-ovelay"></div>
        <div class="bs-caption">
        	<div class="container">
            	<h3>Chương trình<br />Thưởng Xèng - Gắn kết thành viên</h3>
                <h4>Bắt đầu từ ngày 15/10/2014</h4>
                <div class="bs-list">
                	<div class="bs-inner">
                        <label>Cơ cấu giải thưởng</label>
                        <ul>
                            <li><i class="fa fa-caret-right"></i>Mệnh giá thẻ 10.000 = 20.000 xèng</li>
                            <li><i class="fa fa-caret-right"></i>Mệnh giá thẻ 20.000 = 40.000 xèng</li>
                            <li><i class="fa fa-caret-right"></i>Mệnh giá thẻ 30.000 = 60.000 xèng</li>
                            <li><i class="fa fa-caret-right"></i>Mệnh giá thẻ 50.000 = 100.000 xèng</li>
                            <li><i class="fa fa-caret-right"></i>Mệnh giá thẻ 100.000 = 200.000 xèng</li>
                            <li><i class="fa fa-caret-right"></i>Mệnh giá thẻ 200.000 = 400.000 xèng</li>
                        </ul>
                        <p>(Tối đa giá trị thẻ 200.000/tháng/ thành viên)</p>
                        <span class="bs-card"></span>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- box-slider -->
    <div class="bg-red" id="gioi-thieu">
   		<div class="container">
        	<div class="row">
            	<div class="col-sm-8">
                	<h3>GIỚI THIỆU</h3>	
                    <p>Với mục đích tạo một môi trường  giao lưu và tăng sự gắn kết giữa các thành viên. ChợĐiệnTử xin ra mắt chương trình “Thưởng xèng -  gắn kết thành viên”. </p>
                    <p>Chương trình nhằm tạo ra động lực cho người dùng tham gia sâu hơn vào hệ thống sản phẩm ChợĐiệnTử, thông qua việc thưởng xèng cho các hành vi của người dùng. Đồng thời là công cụ đo lường mức độ hoạt động, khả năng tiêu dùng, thói quen… của người dùng. Hỗ trợ tích cực tới việc xây dựng các chương trình dành riêng cho người bán.</p>
                   	
                </div><!-- col -->
                <div class="col-sm-4">
                	<div class="box-click">
                    	<div class="bc-img">
                        	<img src="${baseUrl}/static/landingxeng/images/box-click.png" alt="img" />
                            <div class="clearfix"></div>
                            <a target="_blank" class="bc-more" href="http://chodientu.vn/tin-tuc/thong-bao--chuong-trinh-thuong-xeng---gan-ket-thanh-vien-82930526079.html?rel=notification&code=82930526079">Xem chi tiết</a>
                        </div>
                    </div>	
                </div><!-- col -->
            </div><!-- row -->
        </div><!-- container -->	
    </div><!-- bg-red -->
    <div class="bg-blue">
    	<div class="container">
        	<div class="row">
            	<div class="col-sm-8">
                	<h3>Công dụng của Xèng</h3>	
                    <div class="bb-list">
                    	<div class="grid">
                        	<div class="img"><img src="${baseUrl}/static/landingxeng/images/data/image1.jpg" alt="img" /></div>
                            <div class="g-content">
                            	<div class="g-row"><span class="g-title">Nạp thẻ điện thoại</span></div>
                                <div class="g-row">Thành viên có thể đổi xèng ra thẻ điện thoại với mệnh giá tương ứng</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                        	<div class="img"><img src="${baseUrl}/static/landingxeng/images/data/image2.jpg" alt="img" /></div>
                            <div class="g-content">
                            	<div class="g-row"><span class="g-title">Up tin VIP, UP TIN</span></div>
                                <div class="g-row">Tăng tỷ lệ truy cập, tăng khả năng mua hàng</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                        	<div class="img"><img src="${baseUrl}/static/landingxeng/images/data/image3.jpg" alt="img" /></div>
                            <div class="g-content">
                            	<div class="g-row"><span class="g-title">Kích Hoạt<br />Tính Năng Đăng Nhanh</span></div>
                                <div class="g-row">Đăng bán tiết kiệm thời gian, nâng cao hiệu quả bán hàng</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                        	<div class="img"><img src="${baseUrl}/static/landingxeng/images/data/image4.jpg" alt="img" /></div>
                            <div class="g-content">
                            	<div class="g-row"><span class="g-title">Kích Hoạt<br />Danh Sách Khách Hàng</span></div>
                                <div class="g-row">Quản lý, gia tăng danh sách khách hàng dễ dàng nhất</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                        	<div class="img"><img src="${baseUrl}/static/landingxeng/images/data/image5.jpg" alt="img" /></div>
                            <div class="g-content">
                            	<div class="g-row"><span class="g-title">Gửi SMS Marketing</span></div>
                                <div class="g-row">Gửi thông tin, chương trình khuyến mại nhanh chóng, hiệu quả</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                        	<div class="img"><img src="${baseUrl}/static/landingxeng/images/data/image6.jpg" alt="img" /></div>
                            <div class="g-content">
                            	<div class="g-row"><span class="g-title">Gửi email Marketing</span></div>
                                <div class="g-row">Chăm sóc khách hàng, tạo lòng tin và tăng khả năng nhận diện thương hiệu</div>
                            </div>
                        </div><!-- grid -->
                        <div class="clearfix"></div>
                    </div><!-- bb-list -->	
                </div><!-- col -->
            </div><!-- row -->
        </div><!-- container -->	
    </div><!-- bg-blue -->
    <div class="bg-howto" id="cach-kiem-xeng">
    	<div class="container">
        	<div class="howto-title">Làm thế nào để kiếm xèng?</div>
            <div class="howto-list">
            	<div class="howto-item">
                	<div class="hi-thumb">
                    	<img src="${baseUrl}/static/landingxeng/images/data/image7.jpg" alt="img" />
                        <i class="fa fa-caret-up"></i>
                        <i class="fa fa-search"></i>
                    </div>
                    <div class="hi-text">
                    	Liên kết lần đầu với Shipchung được <span class="text-danger">2.000 Xèng</span>
                    </div>
                </div><!-- howto-item -->
                <div class="howto-item">
                	<div class="hi-thumb">
                    	<img src="${baseUrl}/static/landingxeng/images/data/image8.jpg" alt="img" />
                        <i class="fa fa-caret-up"></i>
                        <i class="fa fa-search"></i>
                    </div>
                    <div class="hi-text">
                    	Liên kết lần đầu với Ngân Lượng được <span class="text-danger">2.000 Xèng</span>
                    </div>
                </div><!-- howto-item -->
                <div class="howto-item">
                	<div class="hi-thumb">
                    	<img src="${baseUrl}/static/landingxeng/images/data/image9.jpg" alt="img" />
                        <i class="fa fa-caret-up"></i>
                        <i class="fa fa-search"></i>
                    </div>
                    <div class="hi-text">
                    	Đánh giá sản phẩm, model được <span class="text-danger">200 Xèng</span>
                    </div>
                </div><!-- howto-item -->
                <div class="howto-item">
                	<div class="hi-thumb">
                    	<img src="${baseUrl}/static/landingxeng/images/data/image10.jpg" alt="img" />
                        <i class="fa fa-caret-up"></i>
                        <i class="fa fa-search"></i>
                    </div>
                    <div class="hi-text">
                    	Mở Shop Miễn phí được <span class="text-danger">2.000 Xèng</span>
                    </div>
                </div><!-- howto-item -->
                <div class="howto-item">
                	<div class="hi-thumb">
                    	<img src="${baseUrl}/static/landingxeng/images/data/image11.jpg" alt="img" />
                        <i class="fa fa-caret-up"></i>
                        <i class="fa fa-search"></i>
                    </div>
                    <div class="hi-text">
                    	Tạo chiến dịch khuyến mại được <span class="text-danger">1.000 Xèng</span>
                    </div>
                </div><!-- howto-item -->
                <div class="howto-item howto-more">
                	<div class="hm-inner">
                        <div class="hm-title">Vẫn Còn Nhiều cách<br />ĐỂ kiếm xèng!</div>
                        <a target="_blank" class="hm-link" href="http://chodientu.vn/tin-tuc/thong-bao--chuong-trinh-thuong-xeng---gan-ket-thanh-vien-82930526079.html?rel=notification&code=82930526079">Xem thêm</a>	
                    </div>
                </div><!-- howto-item -->
                <div class="clearfix"></div>
            </div><!-- howto-list -->
        </div><!-- container -->
    </div><!-- bg-howto -->
    <div class="bg-support" id="tro-giup">
    	<div class="container">
            <div class="row">
                <div class="col-md-4 col-lg-3">
                    <div class="support-title">Hỗ trợ Khách hàng</div>
                    <div class="support-desc">Liên hệ ngay với chuyên viên tư vấn của chúng tôi để được hỗ trợ!</div>
                </div><!-- col -->
                <div class="col-md-1 col-lg-2"></div>
                <div class="col-md-7">
                    <div class="support-list">
                        <div class="grid">
                            <div class="img"><img src="${baseUrl}/static/landingxeng/images/data/avatar1.jpg" alt="img" /></div>
                            <div class="g-content">
                                <div class="g-row">Phạm Thị Thanh Phúc</div>
                                <div class="g-row">Tư vấn shop - TP.HCM</div>
                                <div class="g-row">0903 716 400</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                            <div class="img"><img src="${baseUrl}/static/landingxeng/images/data/avatar2.jpg" alt="img" /></div>
                            <div class="g-content">
                                <div class="g-row">Nguyễn Hiền Vương</div>
                                <div class="g-row">Tư vấn bán hàng - TP.HCM</div>
                                <div class="g-row">0907 786 283 - 08 6292 0945 (117)</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                            <div class="img"><img src="${baseUrl}/static/landingxeng/images/data/avatar3.jpg" alt="img" /></div>
                            <div class="g-content">
                                <div class="g-row">Nguyễn Đức Vương</div>
                                <div class="g-row">Tư vấn shop - TP.HCM</div>
                                <div class="g-row">0987 973 095</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                            <div class="img"><img src="${baseUrl}/static/landingxeng/images/data/avatar4.jpg" alt="img" /></div>
                            <div class="g-content">
                                <div class="g-row">Nguyễn Văn Nam</div>
                                <div class="g-row">Tư vấn shop - TP.HCM</div>
                                <div class="g-row">0909 811 443</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                            <div class="img"><img src="${baseUrl}/static/landingxeng/images/data/avatar5.jpg" alt="img" /></div>
                            <div class="g-content">
                                <div class="g-row">Đàm Bùi Vũ</div>
                                <div class="g-row">Tư vấn shop - Hà Nội</div>
                                <div class="g-row">0977 941 369</div>
                            </div>
                        </div><!-- grid -->
                        <div class="grid">
                            <div class="img"><img src="${baseUrl}/static/landingxeng/images/data/avatar6.jpg" alt="img" /><</div>
                            <div class="g-content">
                                <div class="g-row">Lã Ngọc Cao</div>
                                <div class="g-row">Tư vấn shop - Hà Nội</div>
                                <div class="g-row">0984 607 937</div>
                            </div>
                        </div><!-- grid -->
                    </div><!-- support-list -->
                </div><!-- col -->
            </div><!-- row -->
        </div><!-- container -->
    </div><!-- bg-support -->
    <div class="footer">
    	<div class="container">
        	<div class="footer-more">BẠN Muốn tìm hiểu thêm về chương trình</div> 
            <a target="_blank" class="footer-button" href="http://chodientu.vn/tin-tuc/thong-bao--chuong-trinh-thuong-xeng---gan-ket-thanh-vien-82930526079.html?rel=notification&code=82930526079">Xem chi tiết</a>   
        </div><!-- container -->
    </div><!-- footer -->
    <!-- Bootstrap core JavaScript -->
      <jwr:script src="/js/landingxeng.js"></jwr:script>
     

  </body>
</html>