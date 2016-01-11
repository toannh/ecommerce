<div class="product-item">
    <div class="big-shadow">
        <% if(data.listingType != 'AUCTION' && textUtils.percentFormat(data.startPrice,data.sellPrice,data.discount,data.discountPrice,data.discountPercent) > 0){
        %>
        <span class="item-sale"><%=textUtils.percentFormat(data.startPrice,data.sellPrice,data.discount,data.discountPrice,data.discountPercent)%>%</span>
        <%}%>
        <a class="view-now" href="<%= baseUrl %>/san-pham/<%= data.id %>/<%= textUtils.createAlias(data.name) %>.html" target="_blank">Xem ngay</a>
        <div class="item-img"><a href="<%= baseUrl %>/san-pham/<%= data.id %>/<%= textUtils.createAlias(data.name) %>.html" target="_blank"><img src="<%=(typeof data.images != 'undefined' && data.images.length >0)?data.images[0]:staticUrl+'/market/images/no-image-product.png'%>"  alt="<%=data.name%>" title="<%=data.name%>"/></a></div>
        <div class="item-text">
            <a class="item-link" href="<%= baseUrl %>/san-pham/<%= data.id %>/<%= textUtils.createAlias(data.name) %>.html" target="_blank">
                <% for(var i = 0; i < cities.length; i++){
                if(cities[i].id===data.cityId){
                %>                                                        
                <span><%=cities[i].name%>: </span>
                <%}}%>
                <%=data.name%>
            </a>
            <div class="item-row">
                <% if(data.listingType=='BUYNOW'){
                %>
                <span class="item-price"><%=textUtils.sellPrice(data.sellPrice,data.discount,data.discountPrice,data.discountPercent)%> <sup class="u-price">đ</sup></span>
                <% if(eval(data.startPrice) <=  eval(data.sellPrice)) { data.startPrice = data.sellPrice } 
                if(textUtils.startPrice(data.startPrice,data.sellPrice,data.discount) > 0){
                %>
                <span class="old-price"><%=textUtils.startPrice(data.startPrice,data.sellPrice,data.discount)%> <sup class="u-price">đ</sup></span>
                <%}}else if(data.listingType=='AUCTION'){
                %>
                <span class="icon20-bidgray"></span>
                <span class="item-price"><%=parseFloat(data.highestBid).toMoney(0, ',', '.')%> <sup class="u-price">đ</sup></span>
                <span class="bid-count">(<%=parseFloat(data.bidCount).toMoney(0, ',', '.')%> lượt)</span>
                <%}%>
            </div>
            <div class="item-row bg-item">
                <div class="item-shop">
                    <% var hasShop = false;
                    for(var i = 0 ;i < shops.length ;i++){
                    if(shops[i].userId == data.sellerId){
                    hasShop = true;
                    %>
                    <span class="icon16-shop"></span>
                    <span class="shop-lb">Shop:</span>
                    <a title="<%= shops[i].alias %>" href="<%= baseUrl%>/<%= shops[i].alias %>/" target="_blank"><%= shops[i].alias %></a>

                    <%}}
                    if(!hasShop){
                    %>
                    <a title="<%=data.sellerName%>" href="<%= urlUtils.browseUrl(itemSearch, '', [{key: 'cid', op: 'rm'},{key:'sellerId',val:data.sellerId,op:'mk'}]) %>/" target="_blank"><%=data.sellerName%></a>
                    <%}%>
                    <%if(data.condition=='NEW'){
                    %>
                    <span class="item-status">(Hàng mới)</span>
                    <%}else{
                    %>
                    <span class="item-status">(Hàng cũ)</span>
                    <%}%>
                </div>
                <div class="item-icon">
                    <%if(data.onlinePayment){
                    %>
                    <p>
                        <span class="icon16-nlgray"></span>
                        <span class="icon-desc">Thanh toán qua NgânLượng</span>
                    </p>
                    <%} 
                    if(data.cod){
                    %>
                    <p>
                        <span class="icon16-codgray"></span>
                        <span class="icon-desc">Giao hàng và thu tiền</span>
                    </p>
                    <%}%>
                    <% if(data.weight < 2000 && data.shipmentType == 'BYWEIGHT'){ %>
                          <!--                    <p><span class="icon16-transgray"></span><span class="icon-desc">Miễn phí vận chuyển</span></p>-->

                    <%}%>
                </div>
            </div>
        </div>
        <ul class="item-hover">
            <%if(typeof data.images != 'undefined' && data.images.length > 1){
            for(var i = 0;i < data.images.length ; i++){
            if(i < 4){
            %>
            <li>
                <span><img src="<%=data.images[i]%>" alt="<%=data.name%>" /></span>
            </li>
            <%}}}%>
        </ul>
    </div><!--big-shadow-->
</div><!--product-item-->