sellerMarketing = {};
sellerMarketing.init = function() {
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị marketting người bán", "/cp/administrator.html"],
        ["Danh sách quản trị viên"]
    ]);
    $(".timeFrom").timeSelect();
    $(".timeTo").timeSelect();
};
sellerMarketing.activeEmail = function(id, type) {
    var msg = "Bạn có muốn thay đổi trạng thái hiện tại ?";
    if (type == "inactive")
        msg = "Không duyệt email marketing này ?";
    popup.confirm(msg, function() {
        ajax({
            service: '/cpservice/emailmarketing/changeactiveemail.json',
            data: {id: id},
            loading: false,
            done: function(resp) {
                if (resp.success) {
                    var data = resp.data;
                    var btn = $('button[rel-data-btn=' + id + ']');
                    if (data.active) {
                        if (data.done) {
                            var crc = btn.attr('class');
                            btn.removeClass(crc);
                            btn.addClass("btn btn-danger disabled");
                        }
                        if (!data.done) {
                            var crc = btn.attr('class');
                            btn.removeClass(crc);
                            btn.addClass("btn btn-danger");
                            btn.empty();
                            btn.html('<span class="glyphicon glyphicon-refresh"></span> Khóa');
                        }
                    }
                    else if (!data.active) {
                        if (data.done) {
                            var crc = btn.attr('class');
                            btn.removeClass(crc);
                            btn.addClass("btn btn-success disabled");
                        }
                        else if (data.done && data.run) {
                            btn.empty();
                            btn.html('<span class="glyphicon glyphicon-refresh"></span> Gửi thử');
                        } else {
                            var crc = btn.attr('class');
                            btn.removeClass(crc);
                            btn.addClass("btn btn-success");
                            btn.empty();
                            btn.html('<span class="glyphicon glyphicon-refresh"></span> Duyệt');
                        }
                    }
                    popup.msg(resp.message);
                } else {
                    if (resp.message != null)
                        popup.msg(resp.message);
                    else
                        popup.msg("Email marketing này chưa có danh sách người gửi !");
                }
            }
        });
    });
};

sellerMarketing.viewMail = function(email, shopname, run, done) {
    send = {};
    var replace = email.replace("]", '').replace("[", '');
    var arr = [];
    arr = replace.split(',');
    var html = '<tr class="text-center"><td><label class="control-label"><b> Email </b></label></td></td>';
    for(var i = 0; i < arr.length; i++){
        html += "<tr><td class='text-center'>"+ arr[i] +"</td></tr>"
    }
    html += '</tr>';
    send.email = html;
    send.shopname = shopname;
    send.run = run;
    send.done = done;
    popup.open('popup-viewmail', 'Xem chi tiết Email', template('/cp/tpl/sellermaketing/list.tpl', send),
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

sellerMarketing.viewSmS = function(phone, name, run, done) {
    send = {};
    var replace = phone.replace("]", '').replace("[", '');
    var arr = [];
    arr = replace.split(',');
    var html = '<tr class="text-center"><td><label class="control-label"><b> Phone </b></label></td></td>';
    for(var i = 0; i < arr.length; i++){
        html += "<tr><td class='text-center'>"+ arr[i] +"</td></tr>"
    }
    html += '</tr>';
    send.phone = html;
    send.name = name;
    send.run = run;
    send.done = done;
    popup.open('popup-viewmail', 'Xem chi tiết SMS', template('/cp/tpl/sellermaketing/listsms.tpl', send),
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

sellerMarketing.previewEmail = function(id) {
    var emailMarketing = new Object();
    emailMarketing.name = $("." + id).children('td[for=name]').text().trim();
    emailMarketing.sendTime = $("." + id).children("td[for=sendTime]").text().trim();
    emailMarketing.content = $("." + id).children("td[for=content]").text().trim();
    emailMarketing.template = $("." + id).children("td[for=template]").text().trim();
    emailMarketing.sellerId = $("." + id).children("td[for=sellerId]").text().trim();
    ajax({
        service: '/cpservice/emailmarketing/preview.json',
        data: emailMarketing,
        loading: true,
        contentType: 'json',
        type: 'post',
        done: function(response) {
            if (response.success) {
                popup.open('popup-email-preview', 'Xem trước nội dung email', template('/user/tpl/emailpreview.tpl', {template: response.data}), [
                    {
                        title: 'Thoát',
                        style: 'btn-default',
                        fn: function() {
                            popup.close('popup-email-preview');
                        }
                    }
                ], 'modal-lg');
            } else {
                popup.msg(response.message);
            }
        }
    });
};

sellerMarketing.activeSms = function(id, type) {
    var msg = "Bạn có muốn thay đổi trạng thái hiện tại ?";
    if (type == "inactive")
        msg = "Bạn có muốn thay đổi trạng thái hiện tại ?";
    popup.confirm(msg, function() {
        ajax({
            service: '/cpservice/smsmarketing/changeactivesms.json',
            data: {id: id},
            done: function(resp) {
                if (resp.success) {
                    var data = resp.data;
                    var btn = $('button[rel-data-sms=' + id + ']');
                    var check = $('td[rel-data-check=' + id + ']');
                    if (data.active) {
                        if (data.done) {
                            var crc = btn.attr('class');
                            btn.removeClass(crc);
                            btn.addClass("btn btn-danger disabled");
                            btn.empty();
                            btn.html('<span class="glyphicon glyphicon-refresh"></span> Khóa');
                        }
                        if (!data.done) {
                            var crc = btn.attr('class');
                            btn.removeClass(crc);
                            btn.addClass("btn btn-danger");
                            btn.empty();
                            btn.html('<span class="glyphicon glyphicon-refresh"></span> Khóa');
                            check.empty();
                            check.html('<i class="glyphicon glyphicon-check"></i>');
                        }
                    }
                    else if (!data.active) {
                        if (data.done) {
                            var crc = btn.attr('class');
                            btn.removeClass(crc);
                            btn.addClass("btn btn-success disabled");
                            btn.empty();
                            btn.html('<span class="glyphicon glyphicon-refresh"></span> Duyệt');
                        } else {
                            var crc = btn.attr('class');
                            btn.removeClass(crc);
                            btn.addClass("btn btn-success");
                            btn.empty();
                            btn.html('<span class="glyphicon glyphicon-refresh"></span> Duyệt');
                            check.empty();
                            check.html('<i class="glyphicon glyphicon-unchecked"></i>');
                        }
                    }
                    popup.msg(resp.message);
                } else {
                    if (resp.message != null)
                        popup.msg(resp.message);
                    else
                        popup.msg("Sms marketing này chưa có danh sách số điện thoại cần gửi !");
                }
            }
        });
    });
};
