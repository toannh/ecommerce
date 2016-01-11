<div class="pu-review-item">
    <div class="row">
        <div class="col-sm-4">
            <p style="font-weight: bold">
                <%
                    if(data.reviewType == 'BUY'){
                        %>
                            <span>Nên mua</span>
                        <%
                    }
                    if(data.reviewType == 'NOREVIEW'){
                        %>
                            <span>Không ý kiến</span>
                        <%
                    }
                    if(data.reviewType == 'DONOTBUY'){
                        %>
                            <span>Không nên mua</span>
                        <%
                    }
                %>
            </p>
            <p><%=(data.content == null) ? '&nbsp;' : data.content%></p>
            <p>
                <%
                var arrs = data.itemIds;
                    $.each(items, function(i){
                         if ($.inArray(items[i].id,arrs) >= 0) {
                             %>
                                <%=items[i].name%><br/>
                             <%
                         }
                     });
                %>
            </p>
        </div>
        <div class="col-sm-2" style="text-align: center;">
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p><%
                var arrsName = data.userIds;
                    $.each(users, function(i){
                         if ($.inArray(users[i].id,arrsName) >= 0) {
                                    var nameSeller = users[i].email;
                                    var a = nameSeller.substring(0,1);
                                    var b = nameSeller.substring(nameSeller.length - 1,nameSeller.length);
                                    var temp = nameSeller.substring(1, nameSeller.length - 1);
                                    var str = temp.replace(temp, "****");
                                    var strSellerName = a + str + b;
                           $.each(point, function(j){
                                if(data.userId == j){
                                    %>
                                        <%=strSellerName%>(<%=Math.round(point[j])%>% Uy tín)<br/>
                                    <%
                                }
                            });
                        }
                    });
                %></p>
        </div>
        <div class="col-sm-2" style="text-align: center;">
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>
                <%
                var arrsPrice = data.itemIds;
                    $.each(items, function(i){
                         if ($.inArray(items[i].id,arrsPrice) >= 0) {
                             %>
                                <%=items[i].sellPrice%>đ<br/>
                             <%
                         }
                     });
                %>
            </p>
        </div>
        <div class="col-sm-2" style="text-align: center">
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>
                <%
                var arrsCreateTime = data.itemIds;
                    $.each(items, function(i){
                         if ($.inArray(items[i].id,arrsCreateTime) >= 0) {
                             %>
                                <%=textUtils.formatTime(data.createTime,'hour')%><br/>
                             <%
                         }
                     });
                %>
            </p>
        </div>
        <div class="col-sm-2" style="text-align: center">
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>
                <%
                var arrsView = data.itemIds;
                    $.each(items, function(i){
                         if ($.inArray(items[i].id,arrsView) >= 0) {
                             %>
                                <a target="_blank" href="<%= baseUrl %>/san-pham/<%=items[i].id %>/<%= textUtils.createAlias(items[i].name) %>.html">Xem sản phẩm</a><br/>
                             <%
                         }
                     });
                %>
            </p>
        </div>
    </div><!-- row -->
</div><!-- pu-review-item -->
