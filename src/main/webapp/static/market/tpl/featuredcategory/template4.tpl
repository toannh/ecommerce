
<% if(this.categoryBannerHomes!=null){
%>
<% for(var i = 0; i < this.categoryBannerHomes.length; i++){ %>

<% if(this.categoryBannerHomes[i].position == 1){ 
var banner1 = true;
var banner_image1 = this.categoryBannerHomes[i].image;
var banner_url1 = this.categoryBannerHomes[i].url;
}
} %>

<%}%>
<% if(this.categoryItemHomes!=null){
%>
<% for(var j = 0; j < this.items.length; j++){ %>
<% for(var i = 0; i < this.categoryItemHomes.length; i++){ %>

<% if(this.categoryItemHomes[i].itemId==this.items[j].id){
if(this.categoryItemHomes[i].position == 1){ 
var item1 = true;
var image1 = this.categoryItemHomes[i].image;
var title1 = this.categoryItemHomes[i].title; 
var discount1 = this.items[j].discount; 
var id1 = this.items[j].id; 
var name1 = this.items[j].name; 
var listingType1 = this.items[j].listingType; 
var startPrice1 = this.items[j].startPrice; 
var sellPrice1 = this.items[j].sellPrice; 
var discountPrice1 = this.items[j].discountPrice; 
var discountPercent1 = this.items[j].discountPercent; 
}}%>

<% if(this.categoryItemHomes[i].itemId==this.items[j].id){
if(this.categoryItemHomes[i].position == 2){ 
var item2 = true;
var image2 = this.categoryItemHomes[i].image;
var title2 = this.categoryItemHomes[i].title; 
var discount2 = this.items[j].discount; 
var id2 = this.items[j].id; 
var name2 = this.items[j].name; 
var listingType2 = this.items[j].listingType; 
var startPrice2 = this.items[j].startPrice; 
var sellPrice2 = this.items[j].sellPrice; 
var discountPrice2 = this.items[j].discountPrice;
var discountPercent2 = this.items[j].discountPercent;
}} %>

<% if(this.categoryItemHomes[i].itemId==this.items[j].id){
if(this.categoryItemHomes[i].position == 3){ 
var item3 = true;
var image3 = this.categoryItemHomes[i].image;
var title3 = this.categoryItemHomes[i].title; 
var discount3 = this.items[j].discount; 
var id3 = this.items[j].id; 
var name3 = this.items[j].name; 
var listingType3 = this.items[j].listingType; 
var startPrice3 = this.items[j].startPrice; 
var sellPrice3 = this.items[j].sellPrice; 
var discountPrice3 = this.items[j].discountPrice;
var discountPercent3 = this.items[j].discountPercent;
}} %>
<% if(this.categoryItemHomes[i].itemId==this.items[j].id){
if(this.categoryItemHomes[i].position == 4){ 
var item4 = true;
var image4 = this.categoryItemHomes[i].image;
var title4 = this.categoryItemHomes[i].title; 
var discount4 = this.items[j].discount; 
var id4 = this.items[j].id; 
var name4 = this.items[j].name; 
var listingType4 = this.items[j].listingType; 
var startPrice4 = this.items[j].startPrice; 
var sellPrice4 = this.items[j].sellPrice; 
var discountPrice4 = this.items[j].discountPrice; 
var discountPercent4 = this.items[j].discountPercent;
}} %>
<% if(this.categoryItemHomes[i].itemId==this.items[j].id){
if(this.categoryItemHomes[i].position == 5){ 
var item5 = true;
var image5 = this.categoryItemHomes[i].image;
var title5 = this.categoryItemHomes[i].title; 
var discount5 = this.items[j].discount; 
var id5 = this.items[j].id; 
var name5 = this.items[j].name; 
var listingType5 = this.items[j].listingType; 
var startPrice5 = this.items[j].startPrice; 
var sellPrice5 = this.items[j].sellPrice; 
var discountPrice5 = this.items[j].discountPrice; 
var discountPercent5 = this.items[j].discountPercent;
}} %>
<% if(this.categoryItemHomes[i].itemId==this.items[j].id){
if(this.categoryItemHomes[i].position == 6){ 
var item6 = true;
var image6 = this.categoryItemHomes[i].image;
var title6 = this.categoryItemHomes[i].title; 
var discount6 = this.items[j].discount; 
var id6 = this.items[j].id; 
var name6 = this.items[j].name; 
var listingType6 = this.items[j].listingType; 
var startPrice6 = this.items[j].startPrice; 
var sellPrice6 = this.items[j].sellPrice; 
var discountPrice6 = this.items[j].discountPrice; 
var discountPercent6 = this.items[j].discountPercent;
}} %>
<% if(this.categoryItemHomes[i].itemId==this.items[j].id){
if(this.categoryItemHomes[i].position == 7){ 
var item7 = true;
var image7 = this.categoryItemHomes[i].image;
var title7 = this.categoryItemHomes[i].title; 
var discount7 = this.items[j].discount; 
var id7 = this.items[j].id; 
var name7 = this.items[j].name; 
var listingType7 = this.items[j].listingType; 
var startPrice7 = this.items[j].startPrice; 
var sellPrice7 = this.items[j].sellPrice; 
var discountPrice7 = this.items[j].discountPrice; 
var discountPercent7 = this.items[j].discountPercent;
}} %>
<% if(this.categoryItemHomes[i].itemId==this.items[j].id){
if(this.categoryItemHomes[i].position == 8){ 
var item8 = true;
var image8 = this.categoryItemHomes[i].image;
var title8 = this.categoryItemHomes[i].title; 
var discount8 = this.items[j].discount; 
var id8 = this.items[j].id; 
var name8 = this.items[j].name; 
var listingType8 = this.items[j].listingType; 
var startPrice8 = this.items[j].startPrice; 
var sellPrice8 = this.items[j].sellPrice; 
var discountPrice8 = this.items[j].discountPrice;
var discountPercent8 = this.items[j].discountPercent;
}} %>
<% } %>       

<%}%><%}%>
<div class="box-product bp-template4">
    <div class="bp-brand4 home-item">
        <div class="bb-inner">
            <% if(this.categoryManufacturerHomes!=null){
            for(var i = 0; i < this.categoryManufacturerHomes.length; i++){ %>
            <div class="bb-item"><a href="javascript:;" target="_blank" onclick="urlUtils.manufacturerUrl('<%=this.categoryManufacturerHomes[i].manufacturerId%>');"><img src="<%=this.categoryManufacturerHomes[i].image%>" alt="logo"></a></div>
            <%}}%>
        </div>
        <% if(banner1==true){ %>
        <a href="<%=banner_url1%>" target="_blank" rel="nofollow">
            <img src="<%=banner_image1%>" alt="banner">
        </a>
        <%}%>
    </div>

    <div class="home-item squarehome-item bp1">
        <% if(item1==true){ %>
        <div class="hoi-inner">
            <% if(listingType1 == 'BUYNOW' && textUtils.percentFormat(startPrice1,sellPrice1,discount1,discountPrice1,discountPercent1)>0){ 
                %>                
                 <span class="hoi-sale">-<%=textUtils.percentFormat(startPrice1,sellPrice1,discount1,discountPrice1,discountPercent1)%>%</span>
            <%}%>
            <a class="hoi-thumblink" target="_blank" href="<%= baseUrl %>/san-pham/<%= id1 %>/<%= textUtils.createAlias(name1) %>.html">
                <img src="<%=image1%>" alt="product" />
            </a>

            <div class="hoi-title">
                <div class="hoi-row">
                    <a href="<%= baseUrl %>/san-pham/<%= id1 %>/<%= textUtils.createAlias(name1) %>.html"> <%=title1%></a>  
                </div>
                <div class="hoi-row">
                   
                     <% if(listingType1 == 'BUYNOW'){ %>
                        <span class="hoi-price"><%=textUtils.sellPrice(sellPrice1,discount1,discountPrice1,discountPercent1)%><sup class="u-price">đ</sup></span>
                        <% if(textUtils.discountPrice(startPrice1,sellPrice1,discount1,discountPrice1,discountPercent1) != 0) {  %>
                            <span class="hoi-oldprice"><%=textUtils.startPrice(startPrice1,sellPrice1,discount1)%><sup class="u-price">đ</sup></span>
                        <%}%>
                    <%}%>
                </div>
            </div>
            
            <div class="hoi-view">
                <a class="hoi-btn" href="javascript:;" onclick="market.quickview('<%=id1%>')">Xem nhanh</a>
                <a class="hoi-btn" href="#"><span class="icon24-star"></span></a>
            </div>
        </div>
        <%}%>
    </div><!-- home-item -->
    <div class="home-item mediumhome-item bp2">
        <% if(item2==true){ %>
        <div class="hoi-inner">
            <% if(listingType2 == 'BUYNOW' && textUtils.percentFormat(startPrice2,sellPrice2,discount2,discountPrice2,discountPercent2)>0){ 
                %>                
                 <span class="hoi-sale">-<%=textUtils.percentFormat(startPrice2,sellPrice2,discount2,discountPrice2,discountPercent2)%>%</span>
            <%}%>
            <a class="hoi-thumblink" href="<%= baseUrl %>/san-pham/<%= id2 %>/<%= textUtils.createAlias(name2) %>.html" target="_blank">
                <img src="<%=image2%>" alt="product" />
            </a>

            <div class="hoi-title">
                <div class="hoi-row">
                    <a href="<%= baseUrl %>/san-pham/<%= id2 %>/<%= textUtils.createAlias(name2) %>.html" target="_blank"> <%=title2%></a>  
                </div>
                <div class="hoi-row">
                   
                     <% if(listingType2 == 'BUYNOW'){ %>
                        <span class="hoi-price"><%=textUtils.sellPrice(sellPrice2,discount2,discountPrice2,discountPercent2)%><sup class="u-price">đ</sup></span>
                        <% if(textUtils.discountPrice(startPrice2,sellPrice2,discount2,discountPrice2,discountPercent2) != 0) {  %>
                            <span class="hoi-oldprice"><%=textUtils.startPrice(startPrice2,sellPrice2,discount2)%><sup class="u-price">đ</sup></span>
                        <%}%>
                    <%}%>
                </div>
            </div>
            
            <div class="hoi-view">
                <a class="hoi-btn" href="javascript:;" onclick="market.quickview('<%=id2%>')">Xem nhanh</a>
                <a class="hoi-btn" href="#"><span class="icon24-star"></span></a>
            </div>
        </div>
        <%}%>
    </div><!-- home-item -->
    <div class="home-item mediumhome-item bp3">
        <% if(item3==true){ %>
        <div class="hoi-inner">
            <% if(listingType3 == 'BUYNOW' && textUtils.percentFormat(startPrice3,sellPrice3,discount3,discountPrice3,discountPercent3)>0){ 
                %>                
                 <span class="hoi-sale">-<%=textUtils.percentFormat(startPrice3,sellPrice3,discount3,discountPrice3,discountPercent3)%>%</span>
            <%}%>
            <a class="hoi-thumblink" href="<%= baseUrl %>/san-pham/<%= id3 %>/<%= textUtils.createAlias(name3) %>.html" target="_blank">
                <img src="<%=image3%>" alt="product" />
            </a>

            <div class="hoi-title">
                <div class="hoi-row">
                    <a href="<%= baseUrl %>/san-pham/<%= id3 %>/<%= textUtils.createAlias(name3) %>.html" target="_blank"> <%=title3%></a>  
                </div>
                <div class="hoi-row">
                   
                     <% if(listingType3 == 'BUYNOW'){ %>
                        <span class="hoi-price"><%=textUtils.sellPrice(sellPrice3,discount3,discountPrice3,discountPercent3)%><sup class="u-price">đ</sup></span>
                        <% if(textUtils.discountPrice(startPrice3,sellPrice3,discount3,discountPrice3,discountPercent3) != 0) {  %>
                            <span class="hoi-oldprice"><%=textUtils.startPrice(startPrice3,sellPrice3,discount3)%><sup class="u-price">đ</sup></span>
                        <%}%>
                    <%}%>
                </div>
            </div>
            
            <div class="hoi-view">
                <a class="hoi-btn" href="javascript:;" onclick="market.quickview('<%=id3%>')">Xem nhanh</a>
                <a class="hoi-btn" href="#"><span class="icon24-star"></span></a>
            </div>
        </div>
        <%}%>
    </div><!-- home-item -->
    <div class="home-item mediumhome-item bp4">
        <% if(item4==true){ %>
        <div class="hoi-inner">
            <% if(listingType4 == 'BUYNOW' && textUtils.percentFormat(startPrice4,sellPrice4,discount4,discountPrice4,discountPercent4)>0){ 
                %>                
                 <span class="hoi-sale">-<%=textUtils.percentFormat(startPrice4,sellPrice4,discount4,discountPrice4,discountPercent4)%>%</span>
            <%}%>
            <a class="hoi-thumblink" href="<%= baseUrl %>/san-pham/<%= id4 %>/<%= textUtils.createAlias(name4) %>.html" target="_blank">
                <img src="<%=image4%>" alt="product" />
            </a>
            <div class="hoi-title">
                <div class="hoi-row">
                    <a href="<%= baseUrl %>/san-pham/<%= id4 %>/<%= textUtils.createAlias(name4) %>.html" target="_blank"> <%=title4%></a>  
                </div>
                <div class="hoi-row">
                   
                     <% if(listingType4 == 'BUYNOW'){ %>
                        <span class="hoi-price"><%=textUtils.sellPrice(sellPrice4,discount4,discountPrice4,discountPercent4)%><sup class="u-price">đ</sup></span>
                        <% if(textUtils.discountPrice(startPrice4,sellPrice4,discount4,discountPrice4,discountPercent4) != 0) {  %>
                            <span class="hoi-oldprice"><%=textUtils.startPrice(startPrice4,sellPrice4,discount4)%><sup class="u-price">đ</sup></span>
                        <%}%>
                    <%}%>
                </div>
            </div>
            
            <div class="hoi-view">
                <a class="hoi-btn" href="javascript:;" onclick="market.quickview('<%=id4%>')">Xem nhanh</a>
                <a class="hoi-btn" href="#"><span class="icon24-star"></span></a>
            </div>
        </div>
        <%}%>
    </div><!-- home-item -->
    <div class="home-item highthome-item bp5">
        <% if(item5==true){ %>
        <div class="hoi-inner">
            <% if(listingType5 == 'BUYNOW' && textUtils.percentFormat(startPrice5,sellPrice5,discount5,discountPrice5,discountPercent5)>0){ 
                %>                
                 <span class="hoi-sale">-<%=textUtils.percentFormat(startPrice5,sellPrice5,discount5,discountPrice5,discountPercent5)%>%</span>
            <%}%>
            <a class="hoi-thumblink" href="<%= baseUrl %>/san-pham/<%= id5 %>/<%= textUtils.createAlias(name5) %>.html" target="_blank">
                <img src="<%=image5%>" alt="product" />
            </a>

            <div class="hoi-title">
                <div class="hoi-row">
                    <a href="<%= baseUrl %>/san-pham/<%= id5 %>/<%= textUtils.createAlias(name5) %>.html" target="_blank"> <%=title5%></a>  
                </div>
                <div class="hoi-row">
                   
                     <% if(listingType5 == 'BUYNOW'){ %>
                        <span class="hoi-price"><%=textUtils.sellPrice(sellPrice5,discount5,discountPrice5,discountPercent5)%><sup class="u-price">đ</sup></span>
                        <% if(textUtils.discountPrice(startPrice5,sellPrice5,discount5,discountPrice5,discountPercent5) != 0) {  %>
                            <span class="hoi-oldprice"><%=textUtils.startPrice(startPrice5,sellPrice5,discount5)%><sup class="u-price">đ</sup></span>
                        <%}%>
                    <%}%>
                </div>
            </div>
            
            <div class="hoi-view">
                <a class="hoi-btn" href="javascript:;" onclick="market.quickview('<%=id5%>')">Xem nhanh</a>
                <a class="hoi-btn" href="#"><span class="icon24-star"></span></a>
            </div>
        </div>
        <%}%>
    </div><!-- home-item -->
</div><!-- box-product -->