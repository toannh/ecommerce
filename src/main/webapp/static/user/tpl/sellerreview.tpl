<form id="form-review">
    <input name="orderId" value="<%=data.id%>" type="hidden">
    <input name="productQuality" value="<%=data.productQuality%>" type="hidden" />
    <input name="interactive" value="<%=data.interactive%>" type="hidden" />
    <input name="shippingCosts" value="<%=data.shippingCosts%>" type="hidden" />
    <input name="shipmentPrice" value="<%=data.shipmentPrice%>" type="hidden" />
    <div class="modal-body">
        <div class="rate"> 
            <div class="rate-top clearfix">
                <img class="col-md-3 modal-img" style="width: 85px; height: 105px" src="<%=(data.user.avatar!=null)?data.user.avatar:(baseUrl+'/static/user/images/data/AvatatShop-Default.png')%>">
                <div class="col-md-9 modal-text">
                    <%
                        if(data.items!= null && data.items.length>0){
                            $.each(data.items, function () {
                                %>
                                    <span style="float: left; font-size: 18px"><%=this.quantity%> x <%=this.itemName%>, &nbsp;</span>
                                <%
                            });
                        }
                    %>
                    <div class="clearfix"></div>
                    <p>Người bán: <a class="text-normal" href="<%=baseUrl%>/user/<%=data.user.id%>/ho-so-nguoi-ban.html" target="_blank"><%=(data.shop != null)?data.shop.alias:data.user.name%></a>  
                        ( <i rel="pointStar" class="red" style="font-style: normal;" value="<%=data.pointSellerReviewer%>"><%=data.pointSellerReviewer%>%</i>
                        Uy Tín 
                        <i id="uytin1" class="fa fa-star" style="color: black"></i>
                        <i id="uytin2" class="fa fa-star" style="color: black"></i>
                        <i id="uytin3" class="fa fa-star" style="color: black"></i>
                        <i id="uytin4" class="fa fa-star" style="color: black"></i>
                        <i id="uytin5" class="fa fa-star" style="color: black"></i>
                        )</p>
                    <article>
                        <ul class="list-unstyled">
                            <li><p>Mã đơn hàng: <span class="text-normal"><%=data.id%> 
                                        (<a href="<%=baseUrl%>/<%=data.id%>/chi-tiet-don-hang.html" target="_blank">Chi tiết đơn hàng</a>)</span></p></li>
                            <li><p>Ngày thanh toán: <span class="text-normal"><%=textUtils.formatTime(data.paidTime,'hour')%></span> </p></li>
                            <li><p>Hình thức vận chuyển: 
                                    <span class="text-normal">
                                        <%
                                        if(data.shipmentService == 'SLOW'){
                                        %>
                                        <span>Tiết kiệm</span>
                                        <%
                                        }
                                        if(data.shipmentService == 'FAST'){
                                        %>
                                        <span>Nhanh</span>
                                        <%
                                        }
                                        if(data.shipmentService == 'RAPID'){
                                        %>
                                        <span>Nhanh</span>
                                        <%
                                        }
                                        %>
                                    </span>
                                </p>
                            </li>
                            <li><p>Phí vận chuyển: <span class="red text-normal"><%=parseFloat(data.shipmentPrice).toMoney(0, ',', '.')%> đ</span></p></li>
                        </ul>
                    </article>
                </div>
            </div><!-- end .clearfix-->
            <div class="rate-product">
                <!--<div id="abc">-->
                <p class="title"> Đánh giá cho sản phảm này <span class="text-normal">( <%=data.id%> ) </span></p>
                <ul class="list-inline">
                    <li>
                        <div class="radio blue">
                            <label>
                                <input type="radio" name="reviewType" id="nenmua" value="1" checked>Nên mua<i class="fa fa-star-o"></i> 
                            </label>
                        </div>
                    </li>
                    <li>
                        <div class="radio yellow">
                            <label>
                                <input type="radio" name="reviewType" id="khongykien" value="2">Không ý kiến<i class="fa fa-star-o"></i> 
                            </label>
                        </div>
                    </li>
                    <li>
                        <div class="radio red">
                            <label>
                                <input type="radio" name="reviewType" id="khongnenmua" value="3">Không nên mua<i class="fa fa-star-o"></i> 
                            </label>
                        </div>
                    </li>
                </ul><!-- end .list-inline -->
                <!--</div> -->
                <span class="rel-name"><p>Lý do mà bạn <span class="blue text-uppercase">"nên mua"</span>sản phẩm này</p></span>
                <div id="table-danhgia">
                    <table>
                        <tbody>
                            <tr>
                                <td>Chất lượng sản phẩm</br> <span class="text-normal">Chất lượng/Hình ảnh đúng như mô tả</span></td>
                                <td>
                                    <i id="ratkem" rel="chatluong" value="-1" class="fa fa-star left" title="Kém"></i>
                                    <i id="kem" rel="chatluong" value="-2" class="fa fa-star left" title="Rất kém"></i>
                                    <i id="trungbinh" rel="chatluong" value="0" class="fa fa-star center" title="Bình thường"></i>
                                    <i id="tot" rel="chatluong" value="1" class="fa fa-star right" title="Tốt"></i>
                                    <i id="rattot" rel="chatluong" value="2" class="fa fa-star right" title="Rất tốt"></i>
                                </td>
                                <td><span class="rel-number"><span class="yellow"></span></span></td>
                            </tr>
                            <tr>
                                <td>Tương tác </br> <span class="text-normal">Người bán tương tác, phản hồi với người mua</span></td>
                                <td>
                                    <i id="klienhedc" rel="tuongtac" value="-1" class="fa fa-star left" title="Không nhiệt tình"></i>
                                    <i id="knhiettinh" rel="tuongtac" value="-2" class="fa fa-star left" title="Không liên hệ được"></i>
                                    <i id="binhthuong" rel="tuongtac" value="0" class="fa fa-star center" title="Bình thường"></i>
                                    <i id="nhiettinh" rel="tuongtac" value="1" class="fa fa-star right" title="Nhiệt tình giúp đỡ"></i>
                                    <i id="hailong" rel="tuongtac" value="2" class="fa fa-star right" title="Rất hài lòng"></i>
                                </td>
                                <td><span class="rel-tuongtac"><span class="yellow"></span></span></td>
                            </tr>
                            <tr>
                                <td>Thời gian vận chuyển </br> <span class="text-normal">Đúng như cam kết trên website</span></td>
                                <td>
                                    <i id="quacham" rel="thoigian" value="-1" class="fa fa-star left" title="Giao hàng chậm"></i>
                                    <i id="cham" rel="thoigian" value="-2" class="fa fa-star left" title="Giao hàng quá chậm"></i>
                                    <i id="dung" rel="thoigian" value="0" class="fa fa-star center" title="Giao hàng đúng thời gian"></i>
                                    <i id="nhanh" rel="thoigian" value="1" class="fa fa-star right" title="Giao hàng nhanh"></i>
                                    <i id="ratnhanh" rel="thoigian" value="2" class="fa fa-star right" title="Giao hàng rất nhanh"></i>
                                </td>
                                <td><span class="rel-thoigian"><span class="yellow"></span></span></td>
                            </tr>
                            <tr>
                                <td>Chi phí vận chuyển</td>
                                <td>
                                    <i id="ratdat" rel="chiphi" value="-1" class="fa fa-star left" title="Đắt"></i>
                                    <i id="dat" rel="chiphi" value="-2" class="fa fa-star left" title="Rất đắt"></i>
                                    <i id="dunggia" rel="chiphi" value="0" class="fa fa-star center" title="Đúng với giá thị trường"></i>
                                    <i id="re" rel="chiphi" value="1" class="fa fa-star right" title="Rẻ"></i>
                                    <i id="ratre" rel="chiphi" value="2" class="fa fa-star right" title="Rất rẻ"></i>
                                </td>
                                <td><span class="rel-chiphi"><span class="yellow"></span></span></td>
                            </tr>
                        </tbody>
                    </table>  
                    <div class="form-group">
                        <textarea class="form-control" name="content" placeholder="Hãy cho chúng tôi biết thêm ý kiến của bạn." rows="3" id="content"></textarea> 
                    </div>
                </div>
            </div><!-- end .rate-product -->
            <div class="rate-bottom">
                <p class="disable-select">Bạn đã đánh giá người bán <a href="<%=baseUrl%>/user/<%=data.user.id%>/ho-so-nguoi-ban.html" target="_blank"><%=(data.shop != null)?data.shop.alias:data.user.name%>
                    </a> với <span class="rel-point" style="color: red" name="point">  </span> điểm UY TÍN </p>
            </div>
        </div><!-- checkbox -->
    </div><!-- end modal-body -->
</form>