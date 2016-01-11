
var notification = {};
notification.repData = [];
notification.config = function () {
    ajax({
        service: '/market/getlistshownotify.json',
        loading: false,
        done: function (resp) {
            if (resp.success) {
                notification.repData = resp.data;
            } else {
                popup.msg(resp.message);
            }
        }
    });
    var data = [];
    setTimeout(function () {
        $.each(notification.repData, function (i) {
            var arr = [];
            arr[0] = notification.repData[i].id;
            arr[1] = notification.repData[i].title;
            var url = baseUrl + '/tin-tuc/' + textUtils.createAlias(arr[1]) + '-' + arr[0] + '.html';
            arr[2] = url;
            data.push(arr);
        });
        notification.write(data);
        $(".sn-button").click(function () {
            $('.sn-button').css("bottom", "-45px");
            $('.sn-view').slideDown();
            return false;
        });
        $(".sn-title").click(function () {
            $('.sn-view').slideUp();
            $('.sn-button').css("bottom", "0");
            return false;
        });
    }, 2000);
};

notification.write = function (datas) {
    var data = datas;
    var nf = textUtils.getCookie("cdtnotification");
    if (typeof nf === "undefined" || nf === '') {
        nf = {};
    } else {
        nf = JSON.parse(base64.decode(nf));
    }
    if (typeof data === "undefined" || data.length === 0) {
        return;
    }

    var history = [];
    $.each(nf, function (key, val) {
        history.push(val);
    });

    for (var i = 0; i < history.length; i++) {
        var hs = history[i];
        for (var j = 0; j < data.length; j++) {
            var index = data[j][0].indexOf(hs);
            if (index !== -1) {
                data.splice(j, 1);
            }
        }
    }

    if (typeof data !== "undefined" && data.length > 0) {
        var text = '';
        $.each(data, function (aaa, news) {
            var url = news[2] + "?rel=notification&code=" + news[0] + "";
//            var url = baseUrl + '/tin-tuc/' + textUtils.createAlias(news[1]) + '-' + news[0] + '.html?rel=notification&code=' + news[0] + '';
            text += '<li><a href="' + url + '" target="_blank">' + news[1] + '</a></li>';
        });
        var html = '<div class="smart-notification">\
                    <div class="sn-inner">\
                        <div class="sn-button active"><i class="fa fa-globe"></i><span class="sn-nummer">' + data.length + '</span></div>\
                        <div class="sn-view">\
                            <div class="sn-title">\
                                <i class="fa fa-globe"></i>   \
                            </div>\
                            <div class="sn-content">\
                                <ul data-rel="Notification"> ' + text + '\
                                </ul>\
                            </div>\
                        </div>\
                    </div>\
                    </div>';
        var html1= '<a href="#"><i class="fa fa-bell-o"></i><span class="msg-red">' + data.length + '</span></a>\
                        <div class="popmenu popmenu-right">\
                            <span class="popmenu-bullet"></span>\
                            <div class="popmenu-small">\
                        <ul class="popmenu-ul popmenu-line-ul"> ' + text + '\
                                </ul>\
                            </div>\
                        </div>';
    }
    $("li[for=notifi]").html(html1);

};

notification.read = function () {
    var code = textUtils.urlParam().code;
    if (typeof code === "undefined" || code === '')
        return;
    var nf = textUtils.getCookie("cdtnotification");
    if (typeof nf === "undefined" || nf === '') {
        nf = base64.encode(JSON.stringify({}));
    }
    nf = JSON.parse(base64.decode(nf));
    nf[textUtils.createAlias("code_" + code)] = code;
    textUtils.setCookie("cdtnotification", base64.encode(JSON.stringify(nf)), 360);
};

$(document).ready(function () {
    //notification.write();
    notification.config();
});