<form id="form-review">
    <input name="orderId" value="<%=data.id%>" type="hidden">
    <input name="productQuality" value="<%=data.productQuality%>" type="hidden" />
    <input name="interactive" value="<%=data.interactive%>" type="hidden" />
    <input name="shippingCosts" value="<%=data.shippingCosts%>" type="hidden" />
    <input name="shipmentPrice" value="<%=data.shipmentPrice%>" type="hidden" />
    <div class="modal-body">
        <div class="rate"> 
            <div class="rate-product">
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
                <span class="rel-name"><p>Lý do mà Admin <span class="blue text-uppercase">"nên mua"</span>sản phẩm này</p></span>
                <div id="table-danhgia"> 
                    <div class="form-group">
                        <textarea class="form-control" name="content" placeholder="Tại sao Admin CĐT lại đánh giá người bán này" rows="3" id="content"></textarea> 
                    </div>
                </div>
            </div><!-- end .rate-product -->
        </div><!-- checkbox -->
    </div><!-- end modal-body -->
</form>