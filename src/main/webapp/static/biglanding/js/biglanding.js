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

};
biglanding.ladingfreeday = function () {
    $("#countdown-freedayy").jCounter({
        date: "19 january 2015 0:00:00",
        format: "dd:hh:mm:ss",
        twoDigits: 'on',
        fallback: function () {
            console.log("Đến giờ mua hàng!")
        }
    });
};
biglanding.getCounter = function (putTime){
    var a = new Date(putTime);
  var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
  var year = a.getFullYear();
  var month = months[a.getMonth()];
  var date = a.getDate();
  var hour = a.getHours();
  var min = a.getMinutes();
  var sec = a.getSeconds();
  var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec ;
  return time;
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