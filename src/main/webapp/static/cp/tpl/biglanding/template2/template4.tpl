<%
var name1 = '';
var name2 = '';
var name3 = '';
var name4 = '';
var name5 = '';
var name6 = '';
var name7 = '';
var name8 = '';
var name9 = '';
var name10 = '';
var image1 = '';
var image2 = '';
var image3 = '';
var image4 = '';
var image5 = '';
var image6 = '';
var image7 = '';
var image8 = '';
var image9 = '';
var image10 = '';
if(data.bigLandingItem!=null && data.bigLandingItem!=''){
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
        if(data.bigLandingItem[i].position==9){
            name9 = data.bigLandingItem[i].name;
            image9 = data.bigLandingItem[i].image;
        }
        if(data.bigLandingItem[i].position==10){
            name10 = data.bigLandingItem[i].name;
            image10 = data.bigLandingItem[i].image;
        }
    }
}
%>
<div class="lb-box lb-box4">
    <form id="image-form-banner" style="display:none">
                        <input type="file" style="display:none" id="lefileS" name="image" onchange="biglanding.uploadBanner();">
                        <input type="text" style="display:none" name="targetIdBanner" value="<%=(typeof data!=='undefined')? data.id: ''%>">
                    </form>
                    	<div class="lb-category lb-category-right lb-bg-green">
                        	<div class="lbc-inner">
                            	<div class="lbc-title"><a href="#"><%=(typeof data!=='undefined')? data.name: ''%></a></div>
                                <div class="lbc-list">
                                	 <ul>
                                    <%if(typeof data!=='undefined' && data.categorySubs!=null && data.categorySubs.length>0) {
                                    $.each(data.categorySubs, function(){
                                    %>
                                    <li><a href="#"><%=this.name%></a></li>
                                   <%  }); } %>
                                </ul>
                                </div>
                                 <div class="lbc-img"><img src="<%=(typeof data!=='undefined')? data.image: ''%>" alt="img"></div>
                                <a class="lbc-more" href="javascript:;" onclick="$('#lefileS').click();" >Thay banner 399x510px</a>
                            </div><!-- /lbc-inner -->
                        </div><!-- /lb-category -->
                        <div class="lb-item lb-position1" onclick="biglanding.addItemNB('<%=data.id%>',1,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image1%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name1%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-skyscraperitem lb-position2" onclick="biglanding.addItemNB('<%=data.id%>',2,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image2%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name2%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><div class="tooltip fade top in" style="top: -48px; left: 0px; display: block;"><div class="tooltip-arrow"></div><div class="tooltip-inner">Khuyến mại thêm 1 chuột dây mitsumi</div></div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-skyscraperitem lb-position3" onclick="biglanding.addItemNB('<%=data.id%>',3,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image3%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name3%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-position4" onclick="biglanding.addItemNB('<%=data.id%>',4,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image4%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name4%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><div class="tooltip fade top" style="top: -48px; left: 0px; display: block;"><div class="tooltip-arrow"></div><div class="tooltip-inner">Khuyến mại thêm 1 chuột dây mitsumi</div></div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-position5" onclick="biglanding.addItemNB('<%=data.id%>',5,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image5%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name5%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-position6" onclick="biglanding.addItemNB('<%=data.id%>',6,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image6%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name6%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-position7" onclick="biglanding.addItemNB('<%=data.id%>',7,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image7%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name7%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-position8" onclick="biglanding.addItemNB('<%=data.id%>',8,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image8%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name8%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-position9" onclick="biglanding.addItemNB('<%=data.id%>',9,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image9%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name9%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                        <div class="lb-item lb-position10" onclick="biglanding.addItemNB('<%=data.id%>',10,4);">
                        	<div class="lbi-inner cdt-tooltip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Khuyến mại thêm 1 chuột dây mitsumi">
                            	<a href="#"><img src="<%=image10%>" alt="img"></a>
                                <div class="lbi-caption">
                                	<a class="lbi-title" href="#"><%=name10%></a>
                                    <span class="lbi-price">350.000 <sup>đ</sup></span>
                                    <span class="lbi-oldprice">390.000 <sup>đ</sup></span>
                                </div>
                                <div class="lbi-sale"><label>Sale</label><span>30<sup>%</sup></span></div>
                            </div><!-- /lbi-inner -->
                        </div><!-- /lb-item -->
                    </div>