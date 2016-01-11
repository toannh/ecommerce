biglanding = {};
biglanding.init = function () {

  var imgLoading = staticUrl + '/market/images/loading-fast.gif';
    
    $('img.lazy').attr('src', imgLoading);
    $("img.lazy").lazyload({
        effect: "fadeIn"
    });
  var height = $(window).scrollTop();
    setTimeout(function () {
        if(height===0){
            $("html, body").scrollTop(height+20);
        }else{
            $("html, body").animate({scrollTop: height - 1}, 200);
        }
    }, 4000);
    
    $("#lbhomeslider").owlCarousel({
        items: 6,
        itemsCustom: false,
        itemsDesktop: [1199, 5],
        itemsDesktopSmall: [980, 4],
        itemsTablet: [768, 3],
        itemsMobile: [533, 1],
        navigation: true,
        autoPlay: false
    });
  

};
biglanding.getbiglandingpromotion = function (promotionId) {
    $('.lb-seller').removeClass('active');
    $('.' + promotionId).addClass('active');
    ajax({
        data: {promotionId: promotionId},
        service: "/biglanding/getbiglandingpromotion.json",
        loading: false,
        done: function (resp) {
            if (resp.success) {
                var itemHTML = '';
                var i = 0;
                $.each(resp.data.items, function () {
                    i++;
                    if (i <= 5) {
                        var link = baseUrl + "/san-pham/" + this.id + "/" + textUtils.createAlias(this.name) + ".html";
                        var priceNow = '';
                        if (this.listingType == 'BUYNOW') {
                            priceNow = '<div class="lsi-row"><span class="lsi-price">' + textUtils.sellPrice(this.sellPrice, this.discount, this.discountPrice, this.discountPercent) + '<sup class="u-price">đ</sup></span>';
                            if (textUtils.discountPrice(this.startPrice, this.sellPrice, this.discount, this.discountPrice, this.discountPercent) != 0) {
                                priceNow += '<span class="lsi-oldprice">' + textUtils.startPrice(this.startPrice, this.sellPrice, this.discount) + '<sup class="u-price">đ</sup></span>';
                            }
                            priceNow += '</div>';
                        }
                        itemHTML += '<div class="lb-salesock-item">'
                                + '<div class="lsi-thumb"><a href="' + link + '"><img src="' + this.images[0] + '" alt="img"></a></div>'
                                + '<div class="lsi-row"><a class="lsi-title" href="' + link + '">' + this.name + '</a></div>'
                                + priceNow + '</div>';
                    }
                });
                itemHTML += '<div class="clearfix"></div>';
                $('#bigLandingItem').html(itemHTML);
                $('.promotionName').html(resp.data.promotionName);
                $('.bigLandingItemCount').html("Có " + resp.data.items.length + " sản phẩm");
                var linkP = baseUrl + "/" + resp.data.alias + "/browse.html?promotionId=" + resp.data.promotionId;
                $('.bigLandingLinkPromotion').attr("href", linkP);
            } else {
                popup.msg(resp.message);
            }
        }
    });
};