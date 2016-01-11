topup = {};
topup.init = function() {
    topup.changeTab('topupCard');
};
topup.changeAmount = function(tab) {
    var amount = $("#" + tab + " select[rel=parValue]").val();
    var cashPayment = 0;
    if (amount == 'CARD_10000') {
        cashPayment = 10000 * 2;
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(10000) + "<sup>đ</sup>");
    } else if (amount == 'CARD_20000') {
        cashPayment = 20000 * 2;
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(20000) + "<sup>đ</sup>");
    } else if (amount == 'CARD_30000') {
        cashPayment = 30000 * 2;
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(30000) + "<sup>đ</sup>");
    } else if (amount == 'CARD_50000') {
        cashPayment = 50000 * 2;
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(50000) + "<sup>đ</sup>");
    } else if (amount == 'CARD_100000') {
        cashPayment = 100000 * 2;
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(100000) + "<sup>đ</sup>");
    } else if (amount == 'CARD_200000') {
        cashPayment = 200000 * 2;
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(200000) + "<sup>đ</sup>");
    } else if (amount == 'CARD_300000') {
        cashPayment = 300000 * 2;
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(300000) + "<sup>đ</sup>");
    } else if (amount == 'CARD_500000') {
        cashPayment = 500000 * 2;
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(500000) + "<sup>đ</sup>");
    } else {
        $("#" + tab + " p[name=amount]").html("" + cash.moneyFormat(0) + "<sup>đ</sup>");
    }
    $("#" + tab + " p[name=cashPayment]").text("" + cash.moneyFormat(cashPayment) + " xèng");
    if (amount != null && amount != '') {
        $("#" + tab + " div[name=cashPayment]").show();
        $("#" + tab + " div[name=amount]").show();
    } else {
        $("#" + tab + " div[name=cashPayment]").hide();
        $("#" + tab + " div[name=amount]").hide();
    }
};
topup.changeAmountTopUpCard = function(tab) {
    var service = $("#topupCard select[rel=service]").val();
    if (service != null && service != '') {
        $("#topupCard div[name=service]").show();
        $("#topupCard p[name=service]").text(service);
    } else {
        $("#topupCard div[name=service]").hide();
    }
    topup.changeAmount(tab);
};
topup.changeAmountTopUpTel = function(tab) {
    topup.changeAmount(tab);
};
topup.loading = function(load) {
    if (load) {
        $('.btn-loading').addClass('disabled');
        //$('.btn-loading').html("<img src='" + baseUrl + "/static/user/images/loading-fast.gif' style='height: 19px;' /> Vui lòng đợi...");
    } else {
        $('.btn-loading').html("Đồng ý");
        $('.btn-loading').removeClass('disabled');
    }
};
topup.paymentTopupCard = function() {
    var amount = $("#topupCard select[rel=parValue]").val();
    var service = $("#topupCard select[rel=service]").val();
    if (service === null || service === '') {
        popup.msg("Bạn cần chọn nhà mạng.");
    } else if (amount === null || amount === '') {
        popup.msg("Bạn chưa chọn mệnh giá.");
    } else {
        topup.loading(true);
        ajax({
            service: '/topup/buycardtelco.json',
            data: {amount: amount, service: service},
            type: 'post',
            done: function(resp) {
                if (resp.success) {
                    if (resp.data !== null) {
                        if (resp.data.errorCode === '00') {
                            popup.msg("Bạn vừa đổi thành công mã thẻ " + cash.moneyFormat(resp.data.amount) + " nghìn,tài khoản của bạn bị trừ :" + cash.moneyFormat(resp.data.amount * 2) + " xèng.", function() {
                                location.reload();
                            });
                        } else {
                            popup.msg(resp.data.message);
                        }
                    }

                } else {
                    popup.msg(resp.message);
                }
                topup.loading(false);
            }
        });
    }
};
topup.paymentTopupTel = function() {
    var amount = $("#topupTel select[rel=parValue]").val();
    var phone = $("#topupTel input[name=phone]").val();
    if (phone == null || phone == '') {
        popup.msg("Bạn phải nhập số điện thoại muốn nạp thẻ!");
    } else {
        topup.loading(true);
        ajax({
            service: '/topup/buytelco.json',
            data: {phone: phone, amount: amount},
            type: 'post',
            done: function(resp) {
                if (resp.success) {
                    if (resp.data !== null) {
                        if (resp.data.errorCode === '00') {
                            popup.msg("Bạn đã nạp thành công cho số điện " + resp.data.phone + " số tiền : " + cash.moneyFormat(resp.data.amount) + " VND", function() {
                                location.reload();
                            });
                        } else {
                            popup.msg(resp.data.message);
                        }
                    }

                } else {
                    popup.msg(resp.message);
                }
                topup.loading(false);
            }
        });
    }
};
topup.changeTab = function(tab) {
    if (tab == 'topupCard') {
        $('div[rel=topupCard]').show();
        $('div[rel=topupTel]').hide();
        $('div[rel=topupHs]').hide();
        $('li[rel=topupTel]').removeClass('active');
        $('li[rel=topupHs]').removeClass('active');
        $('li[rel=topupCard]').addClass('active');
        $('li[rel=text-menu]').text("Đổi xèng lấy mã thẻ điện thoại");
        $('h1[rel=text-header]').text("Đổi xèng lấy mã thẻ điện thoại");
    } else if (tab == 'topupTel') {
        $('div[rel=topupCard]').hide();
        $('div[rel=topupHs]').hide();
        $('div[rel=topupTel]').show();
        $('li[rel=topupTel]').addClass('active');
        $('li[rel=topupCard]').removeClass('active');
        $('li[rel=topupHs]').removeClass('active');
        $('li[rel=text-menu]').text("Nạp thẻ điện thoại");
        $('h1[rel=text-header]').text("Nạp thẻ điện thoại");
    }
    else if (tab == 'topupHs') {
        $('div[rel=topupHs]').show();
        $('div[rel=topupTel]').hide();
        $('div[rel=topupCard]').hide();
        $('li[rel=topupTel]').removeClass('active');
        $('li[rel=topupCard]').removeClass('active');
        $('li[rel=topupHs]').addClass('active');
        $('li[rel=text-menu]').text("Lịch sử đổi mã thẻ");
        $('h1[rel=text-header]').text("Lịch sử đổi mã thẻ");
    }
};
topup.cancelPayment = function() {
    location.reload();
};

topup.viewcard = function(cardCode, cardSeri, cardType, cardValue, expiryDate) {
    send = {};

    send.cardCode = cardCode;
    send.cardSeri = cardSeri;
    send.cardType = cardType;
    send.cardValue = cardValue;
    send.expiryDate = expiryDate;

    if (cardCode == '' && cardSeri == '' && cardType == '' && cardValue == '' && expiryDate == '') {
        popup.msg("Không có lịch sử nạp thẻ tương ứng");
    } else {
        popup.open('popup-viewmail', 'Xem chi tiết nạp thẻ', template('/user/tpl/topupcard.tpl', send),
                [
                    {
                        title: 'Thoát',
                        style: 'btn-default',
                        fn: function() {
                            popup.close('popup-viewmail');
                        }
                    }
                ]
                );
    }
};

topup.search = function(page) {
    location.href = "/user/topup-by-cash.html?page=" + page;
};