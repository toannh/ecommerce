<%
var name1 = '';
var name2 = '';
var name3 = '';
var name4 = '';
var name5 = '';
var name6 = '';
var name7 = '';
var name8 = '';
var image1 = '';
var image2 = '';
var image3 = '';
var image4 = '';
var image5 = '';
var image6 = '';
var image7 = '';
var image8 = '';
if(data.bigLandingItem!=null){
for(var i = 0; i < data.bigLandingItem.length; i++){
if(data.bigLandingItem[i].position==1){
name1 = data.bigLandingItem[i].name;
image1 = data.bigLandingItem[i].image;
}
if(data.bigLandingItem[i].position==2){
name2 = data.bigLandingItem[i].name;
image2 = data.bigLandingItem[i].image;
}
if(data.bigLandingItem[i].position==3){
name3 = data.bigLandingItem[i].name;
image3 = data.bigLandingItem[i].image;
}
if(data.bigLandingItem[i].position==4){
name4 = data.bigLandingItem[i].name;
image4 = data.bigLandingItem[i].image;
}
if(data.bigLandingItem[i].position==5){
name5 = data.bigLandingItem[i].name;
image5 = data.bigLandingItem[i].image;
}
if(data.bigLandingItem[i].position==6){
name6 = data.bigLandingItem[i].name;
image6 = data.bigLandingItem[i].image;
}
if(data.bigLandingItem[i].position==7){
name7 = data.bigLandingItem[i].name;
image7 = data.bigLandingItem[i].image;
}
if(data.bigLandingItem[i].position==8){
name8 = data.bigLandingItem[i].name;
image8 = data.bigLandingItem[i].image;
}
}
}
%>
<div class="bg-white"><div class="fd-space"></div>
    <div class="fd-title">
        <a class="fdt-name fdt-green" href="#"><%=(typeof data!=='undefined')? data.name: ''%></a>
        <ul class="fdt-category">
            <%if(typeof data!=='undefined' && data.categorySubs!=null && data.categorySubs.length>0) {
            $.each(data.categorySubs, function(){
            %>
            <li><a href="#"><%=this.name%></a></li>
            <%  }); } %>
        </ul>
    </div><!-- /fd-title -->
    <div class="fd-box">
        <form id="image-form-banner" style="display:none">
            <input type="file" style="display:none" id="lefileS" name="image" onchange="biglanding.uploadBanner();">
            <input type="hidden" style="display:none" name="bigLandingId" value="<%=(typeof data!=='undefined')? data.bigLandingId: ''%>">
            <input type="text" style="display:none" name="targetIdBanner" value="<%=(typeof data!=='undefined')? data.id: ''%>">
        </form>
        <a class="lbc-link" href="javascript:;" onclick="$('#lefileS').click();">Thay banner 200x666px</a>
        <div class="fdc-banner"> 
            <img src="<%=(typeof data!=='undefined')? data.image: ''%>" alt="img" class="fdc-image">
        </div><!-- /fdc-banner -->
        <div class="fd-listitem">
            <div class="fd-item" onclick="biglanding.addItemNB('<%=data.id%>',1,4,'LT1');">
                <div class="fdi-thumb">
                    <a href="javascript:;"><img src="<%=image1%>" alt="img"></a>
                </div>
                <div class="fdi-title"><a href="#"><%=name1%></a></div>
                <div class="fdi-row">
                    <span class="fdi-price">109.000<sup>đ</sup></span>
                    <span class="fdi-col">&nbsp;|&nbsp;</span>
                    <span class="fdi-oldprice">180.000<sup>đ</sup></span>
                </div>
                <div class="fdi-button"><a href="#">Xem ngay</a></div>
            </div><!-- /fd-item -->
             <div class="fd-item" onclick="biglanding.addItemNB('<%=data.id%>',2,4,'LT1');">
                <div class="fdi-thumb">
                    <a href="javascript:;"><img src="<%=image2%>" alt="img"></a>
                </div>
                <div class="fdi-title"><a href="#"><%=name2%></a></div>
                <div class="fdi-row">
                    <span class="fdi-price">109.000<sup>đ</sup></span>
                    <span class="fdi-col">&nbsp;|&nbsp;</span>
                    <span class="fdi-oldprice">180.000<sup>đ</sup></span>
                </div>
                <div class="fdi-button"><a href="#">Xem ngay</a></div>
            </div><!-- /fd-item -->
             <div class="fd-item" onclick="biglanding.addItemNB('<%=data.id%>',3,4,'LT1');">
                <div class="fdi-thumb">
                    <a href="javascript:;"><img src="<%=image3%>" alt="img"></a>
                </div>
                <div class="fdi-title"><a href="#"><%=name3%></a></div>
                <div class="fdi-row">
                    <span class="fdi-price">109.000<sup>đ</sup></span>
                    <span class="fdi-col">&nbsp;|&nbsp;</span>
                    <span class="fdi-oldprice">180.000<sup>đ</sup></span>
                </div>
                <div class="fdi-button"><a href="#">Xem ngay</a></div>
            </div><!-- /fd-item -->
            <div class="fd-item" onclick="biglanding.addItemNB('<%=data.id%>',4,4,'LT1');">
                <div class="fdi-thumb">
                    <a href="javascript:;"><img src="<%=image4%>" alt="img"></a>
                </div>
                <div class="fdi-title"><a href="#"><%=name4%></a></div>
                <div class="fdi-row">
                    <span class="fdi-price">109.000<sup>đ</sup></span>
                    <span class="fdi-col">&nbsp;|&nbsp;</span>
                    <span class="fdi-oldprice">180.000<sup>đ</sup></span>
                </div>
                <div class="fdi-button"><a href="#">Xem ngay</a></div>
            </div><!-- /fd-item -->
            <div class="fd-item" onclick="biglanding.addItemNB('<%=data.id%>',5,4,'LT1');">
                <div class="fdi-thumb">
                    <a href="javascript:;"><img src="<%=image5%>" alt="img"></a>
                </div>
                <div class="fdi-title"><a href="#"><%=name5%></a></div>
                <div class="fdi-row">
                    <span class="fdi-price">109.000<sup>đ</sup></span>
                    <span class="fdi-col">&nbsp;|&nbsp;</span>
                    <span class="fdi-oldprice">180.000<sup>đ</sup></span>
                </div>
                <div class="fdi-button"><a href="#">Xem ngay</a></div>
            </div><!-- /fd-item -->
            <div class="fd-item" onclick="biglanding.addItemNB('<%=data.id%>',6,4,'LT1');">
                <div class="fdi-thumb">
                    <a href="javascript:;"><img src="<%=image6%>" alt="img"></a>
                </div>
                <div class="fdi-title"><a href="#"><%=name6%></a></div>
                <div class="fdi-row">
                    <span class="fdi-price">109.000<sup>đ</sup></span>
                    <span class="fdi-col">&nbsp;|&nbsp;</span>
                    <span class="fdi-oldprice">180.000<sup>đ</sup></span>
                </div>
                <div class="fdi-button"><a href="#">Xem ngay</a></div>
            </div><!-- /fd-item -->
            <div class="fd-item" onclick="biglanding.addItemNB('<%=data.id%>',7,4,'LT1');">
                <div class="fdi-thumb">
                    <a href="javascript:;"><img src="<%=image7%>" alt="img"></a>
                </div>
                <div class="fdi-title"><a href="#"><%=name7%></a></div>
                <div class="fdi-row">
                    <span class="fdi-price">109.000<sup>đ</sup></span>
                    <span class="fdi-col">&nbsp;|&nbsp;</span>
                    <span class="fdi-oldprice">180.000<sup>đ</sup></span>
                </div>
                <div class="fdi-button"><a href="#">Xem ngay</a></div>
            </div><!-- /fd-item -->
            <div class="fd-item" onclick="biglanding.addItemNB('<%=data.id%>',8,4,'LT1');">
                <div class="fdi-thumb">
                    <a href="javascript:;"><img src="<%=image8%>" alt="img"></a>
                </div>
                <div class="fdi-title"><a href="#"><%=name8%></a></div>
                <div class="fdi-row">
                    <span class="fdi-price">109.000<sup>đ</sup></span>
                    <span class="fdi-col">&nbsp;|&nbsp;</span>
                    <span class="fdi-oldprice">180.000<sup>đ</sup></span>
                </div>
                <div class="fdi-button"><a href="#">Xem ngay</a></div>
            </div><!-- /fd-item -->
            <div class="clearfix"></div>
        </div><!-- /fd-listitem -->
        <div class="clearfix"></div>
    </div><!-- /fd-box -->
</div><!-- /bg-white -->