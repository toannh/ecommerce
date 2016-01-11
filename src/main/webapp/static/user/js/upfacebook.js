upfacebook = {};
upfacebook.init = function() {
    $("input[for=grFace]").change(function() {
        var count = upfacebook.getSelectedGroup().length;
        $("span[for=count]").html(count);
        $("span[for=total]").html(count * 20);
    });
    $("input[name=checkall]").change(function() {
        if ($(this).is(":checked")) {
            $("input[for=grFace]").attr("checked", true);
        } else {
            $("input[for=grFace]").attr("checked", false);
        }
        var count = upfacebook.getSelectedGroup().length;
        $("span[for=count]").html(count);
        $("span[for=total]").html(count * 20);
    });
};
upfacebook.detailHis = function() {
    $("#detailHis").click(function() {
        ajax({
            service: '/item/loadHisFacebook.json',
            loading: false,
            done: function(res) {
                if (res.success) {
                    popup.open('popup-hisfacebook', 'Đăng sản phẩm lên facebook', template('/user/tpl/facebookstep4.tpl', {data: res.data}), [
                        {
                            title: 'Đóng',
                            style: 'btn-primary',
                            fn: function() {

                                popup.close('popup-hisfacebook');

                            }
                        }
                    ], 'modal-lg', true);
                } else {
                    popup.msg("Có lỗi xảy ra");
                }
            }
        });
    });
}
upfacebook.detailLink = function(link) {
    popup.open('popup-hislinkfacebook', 'Chi tiết đăng Facebook', template('/user/tpl/facebookstep5.tpl', {data: link}), [
                        {
                            title: 'Đóng',
                            style: 'btn-primary',
                            fn: function() {

                                popup.close('popup-hislinkfacebook');

                            }
                        }
                    ], 'modal-lg', true);
}
upfacebook.getParameter = function(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
};
upfacebook.getSelectedGroup = function() {
    var listGroup = new Array();
    $('input[for=grFace]:checked').each(function(i, el) {
        listGroup.push($(el).attr("value"));
    });
    return listGroup;
};
upfacebook.upItem = function(items) {
//    facebookClient.signin(function(response) {
//        console.log(response);

        var maxquantity = Math.floor(($("input[name=maxquantity]").val() !== "") ? $("input[name=maxquantity]").val() : 1);
        if (maxquantity <= 0) {
            popup.msg('Số xèng trong tài khoản không đủ để thực hiện giao dịch này');
            return false;
        }
        if (items !== "" && items.length > 0) {
            ajax({
                service: '/item/geturlfacebook.json?id='+items,
                loading: false,
                done: function(res) {
                    var url = res.data;
                    if (url !== 'Logged') {
                        //var fId = upfacebook.getParameter('fid');
                        //var fName = upfacebook.getParameter('fname');
                        location.href = url;
                        return;
                    } else {

                        ajax({
                            service: '/item/getitems.json',
                            data: {ids: JSON.stringify(items)},
                            loading: false,
                            done: function(resp) {
                                if (resp.success) {
                                    var xengUp = (resp.data['xeng'] > 0) ? resp.data['xeng'] : 0;
                                    var productId = resp.data['items'][0].id;
                                    popup.open('popup-upfacebook', 'Đăng bán sản phẩm của bạn lên các group facebook', template('/user/tpl/facebookstep1.tpl', {data: resp.data['items'], xeng: xengUp}), [
                                        {
                                            title: 'Thực hiện',
                                            style: 'btn-primary',
                                            fn: function() {
                                                ajax({
                                                    service: '/item/getgroupfacebook.json',
                                                    data: {after: ''},
                                                    loading: false,
                                                    done: function(respG) {
                                                        if (respG.success) {
                                                            var des = $('textarea[name=message]').val();
                                                            if (des === '') {
                                                                popup.msg("Bạn chưa nhập mô tả");
                                                                return;
                                                            }
                                                            //popup.close('popup-upfacebook');
                                                            popup.open('popup-groupfacebook', 'Lựa chọn nhóm facebook', template('/user/tpl/facebookstep2.tpl', {data: respG.data['groups'], xeng: xengUp, productId: productId}), [
                                                                {
                                                                    title: 'Quay về',
                                                                    style: 'btn-primary',
                                                                    fn: function() {
                                                                        popup.close('popup-groupfacebook');

                                                                    }
                                                                },
                                                                {
                                                                    title: 'Thực hiện',
                                                                    style: 'btn-primary',
                                                                    fn: function() {
                                                                        listGroup = upfacebook.getSelectedGroup();

                                                                        if (listGroup.length > 0) {
                                                                            ajax({
                                                                                service: '/item/bookingfacebook.json',
                                                                                data: {message: des, productId: productId, groups: JSON.stringify(listGroup), total: listGroup.length},
                                                                                loading: false,
                                                                                done: function(respB) {
                                                                                    if (respB.success) {
                                                                                        popup.close('popup-upfacebook');
                                                                                        popup.close('popup-groupfacebook');
                                                                                        var tt = listGroup.length * 20;
                                                                                        popup.open('popup-sucessfacebook', 'Đăng sản phẩm lên facebook', template('/user/tpl/facebookstep3.tpl', {time: respB.data, total: tt}), [
                                                                                            {
                                                                                                title: 'Đóng',
                                                                                                style: 'btn-primary',
                                                                                                fn: function() {

                                                                                                    popup.close('popup-sucessfacebook');

                                                                                                }
                                                                                            }
                                                                                        ], 'modal-lg', true);
                                                                                        upfacebook.detailHis();
                                                                                    } else {
                                                                                        popup.msg("Không thể đăng sản phẩm");
                                                                                    }
                                                                                }
                                                                            });


                                                                        } else {
                                                                            popup.msg("Chưa có nhóm nào được chọn");
                                                                        }
                                                                    }
                                                                }

                                                            ], 'modal-lg', true);
                                                            upfacebook.init();
                                                        }

                                                    }
                                                });
                                            }
                                        }
                                    ], 'modal-lg', true);

                                } else {
                                    popup.msg(resp.message);
                                }
                            }
                        });
                    }
                }
            });
        } else {
            popup.msg("Bạn cần chọn danh sách sản phẩm để đăng facebook");
        }

//    });
};
