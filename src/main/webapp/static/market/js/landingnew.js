landingnew = {};
landingnew.init = function () {
    $('.slider').show();
    $('.slider').bxSlider({
        mode: 'vertical',
        slideMargin: 1,
        minSlides: 3,
        maxSlides: 3,
        moveSlides: 1,
        auto: true
    });
    $("#landingnew-slider").owlCarousel({
        items: 3,
        itemsDesktop: [1199, 3],
        itemsDesktopSmall: [980, 2],
        navigation: true,
        autoplay: true,
        slideSpeed: 200
    });
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
