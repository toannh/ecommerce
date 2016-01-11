/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
topup = {};
topup.init = function() {
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị nạp thẻ điện thoại", "/cp/topup.html"],
        ["Danh sách quản lý nạp thẻ điện thoại"]
    ]);
    $('.timeselect').timeSelect();
    topup.getUserByIds();
};

topup.resetForm = function() {
    $('input[type=text]').val("");
    $('select[name=type]').val("");
    $('input[name=createTimeFrom]').val("0");
    $('input[name=createTimeTo]').val("0");
};

topup.getUserByIds = function() {
    ajax({
        service: '/cpservice/topup/getbyids.json',
        data: userIds,
        type: 'post',
        loading: false,
        contentType: 'json',
        done: function(resp) {
            if (resp.success) {
                $.each(resp.data, function() {
                    $('td[rel=' + this.id + ']').text(this.email);
                });
            } else {
                popup.msg(resp.message);
            }
        }
    });
};

topup.view = function(id) {
    ajax({
        service: '/cpservice/topup/getcard.json',
        data: {id: id},
        type: "post",
        loading: false,
        done: function(resp) {
            if (resp.success) {
                popup.open('popup-cardview', 'Xem thông tin mua thẻ', template('/cp/tpl/topupview/note.tpl', resp), [
                    {
                        title: 'Đóng',
                        style: 'btn-primary',
                        fn: function() {
                            popup.close('popup-cardview');
                        }
                    },
                ]);
            } else {
                popup.msg(resp.message);
            }
        }
    });
};

topup.exPortExcelTopup = function () {
    var seftUrl = baseUrl + "/cp/topup/exceltopup.html";
    urlParams = new Object();
    urlParams.startTime = ($('input[name=createTimeFrom]').val() == 0) ? null : $('input[name=createTimeFrom]').val();
    urlParams.endTime = ($('input[name=createTimeTo]').val() == 0) ? null : $('input[name=createTimeTo]').val();
    urlParams.requestId = $('input[name=requestId]').val();
    urlParams.phone = $('input[name=phone]').val();
    urlParams.success = $('select[name=success]').val();
    urlParams.email = $('input[name=email]').val();
    urlParams.type = $('select[name=type]').val();
    var queryString = "";
    var i = 1;
    $.each(urlParams, function (key, val) {
        if (val != null) {
            if (i == 1)
                queryString += "?";
            else
                queryString += "&";
            queryString += key + "=" + val;
            i++;
        }
    });
    location.href = seftUrl + queryString;
};