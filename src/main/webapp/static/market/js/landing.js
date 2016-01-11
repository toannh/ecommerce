landing = {};
landing.init = function () {
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
