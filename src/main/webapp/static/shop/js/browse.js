browse = {};
browse.init = function (params) {
    browse.itemSearch = params.itemSearch;
};

browse.quickView = function (id) {
    ajax({
        service: '/item/getitem.json',
        loading: false,
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                $('#popup-quick-view .container-fluid').html(template('/shop/tpl/quickview.tpl', resp));
            } else {
                popup.msg(resp.message);
            }
            $('.cloud-zoom, .cloud-zoom-gallery').CloudZoom();
            $('.imgdetail-slider').jcarousel({
                scroll: 1,
                auto: 0,
                animation: 800,
                wrap: 'last'
            });
        }

    });
};

browse.changeOrder = function (element) {
    document.location = baseUrl + urlUtils.shopBrowseUrl(browse.itemSearch, shop.alias, [{key: "order", val: $(element).val()}, {key: "page", val: eval(browse.itemSearch.pageIndex) + 1}]);
};

browse.redirect = function (_url, _time, _baseUrl) {
    var tmpHTML = '<form class="form-horizontal" id="changepass-form">Bạn cần tích hợp NgânLượng hoặc ShipChung. Hãy vào <a href="'+baseUrl+'/user/cau-hinh-tich-hop.html"> đây </a>để tích hợp.</form>';
    popup.open("popup-confirm", "Thông báo", tmpHTML, []);
    //popup.open("popup-confirm", "Thông báo",'<div class="container" style="min-width: 300px; max-width: 300px;">' + "Bạn cần tích hợp NgânLượng hoặc ShipChung <br\>Hãy vào <a href=\""+baseUrl+"/user/cau-hinh-tich-hop.html\">đây </a>để tích hợp</div>",[]);
    _time = _time > 0 ? _time : 0;
    _baseUrl = _baseUrl != null ? _baseUrl : baseUrl;
    var t = _time / 1000;
    setInterval(function () {
        t = eval(t - 1);
        if (t >= 0)
            $('span.r-time').html('<div class="help-block">Hệ thống sẽ chuyển tự động chuyển trang trong vòng ' + eval(t) + ' giây</div>')
    }, 1000);

    setTimeout("location.href = '" + _baseUrl + _url + "';", _time);
};