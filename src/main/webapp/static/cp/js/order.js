order = {};

order.init = function () {
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị đơn hàng", "/cp/order.html"],
        ["Danh sách đơn hàng"]
    ]);
    $('.createTimeForm').timeSelect();
    $('.createTimeTo').timeSelect();
    $('.paidTimeForm').timeSelect();
    $('.paidTimeTo').timeSelect();

    $("a").tooltip({placement: 'top'});
    $("span").tooltip({placement: 'top'});

    var html = '<option value="0">Chọn tỉnh / thành phố - Người Bán</option>';
    $.each(citys, function () {
        html += '<option value="' + this.id + '" >' + this.name + '</option>';
    });
    $('select[name=sellerCityId]').html(html);

    var html = '<option value="0">Chọn tỉnh / thành phố - Người nhận</option>';
    $.each(citys, function () {
        html += '<option value="' + this.id + '" >' + this.name + '</option>';
    });
    $("select[name=receiverCityId]").html(html);

    var html = '<option value="0">Chọn tỉnh / thành phố - Người mua</option>';
    $.each(citys, function () {
        html += '<option value="' + this.id + '" >' + this.name + '</option>';
    });
    $("select[name=buyerCityId]").html(html);

    if (ordershop != null && ordershop.length > 0) {
        $('.dispalay-control').show();
    }

    var html = '<option value="">Chọn đơn của shop</option>';
    $.each(ordershop, function () {
        html += '<option value="' + this.userId + '" >' + this.alias + '</option>';
    });
    $("select[name=orderShop]").html(html);
    
    if (userIds != null && userIds.length > 0) {
        $('.dispalay-control').show();
    }

    var html = '<option value="">Chọn đơn NB tích hợp NL,SC</option>';
    $.each(orderNLSC, function (i) {
        $.each(userIds, function (j) {
            if (orderNLSC[i].userId == userIds[j].id) {
                html += '<option value="' + orderNLSC[i].userId + '" >' + orderNLSC[i].userId + ' - ' + userIds[j].username + '</option>';
            }
        });
    });
    $("select[name=orderNlSc]").html(html);

    var html = '<option value="">Chọn đơn NB không tích hợp NL,SC</option>';
    $.each(orderNoNLSC, function (i) {
        $.each(userIds, function (j) {
            if (orderNoNLSC[i].userId == userIds[j].id) {
                html += '<option value="' + orderNoNLSC[i].userId + '" >' + orderNoNLSC[i].userId + ' - ' + userIds[j].username + '</option>';
            }
        });
    });
    $("select[name=orderNoNlSc]").html(html);

    $('select[name=sellerCityId]').change(function () {
        var htmlSellerCityId = '<select name="sellerDistrictId" id="sellerDistrictId" class="form-control"><option value="0">Chọn quận / huyện</option>';
        $.each(districts, function () {
            if (this.cityId === $('select[name=sellerCityId]').val()) {
                htmlSellerCityId += '<option value="' + this.id + '" >' + this.name + '</option>';
            }
        });
        htmlSellerCityId += '</select>';
        $('.sellerDistrictId').html(htmlSellerCityId);
        if ($(this).val() === '0') {
            $('.sellerDistrictId').html("");
        }
    });
    $('select[name=receiverCityId]').change(function () {
        var htmlSellerCityId = '<select name="receiverDistrictId" id="receiverDistrictId" class="form-control"><option value="0">Chọn quận / huyện</option>';
        $.each(districts, function () {
            if (this.cityId === $('select[name=receiverCityId]').val()) {
                htmlSellerCityId += '<option value="' + this.id + '" >' + this.name + '</option>';
            }
        });
        htmlSellerCityId += '</select>';
        $('.receiverDistrictId').html(htmlSellerCityId);
        if ($(this).val() === '0') {
            $('.receiverDistrictId').html("");
        }
    });
    $('select[name=buyerCityId]').change(function () {
        var htmlSellerCityId = '<select name="buyerDistrictId" id="buyerDistrictId" class="form-control"><option value="0">Chọn quận / huyện</option>';
        $.each(districts, function () {
            if (this.cityId === $('select[name=buyerCityId]').val()) {
                htmlSellerCityId += '<option value="' + this.id + '" >' + this.name + '</option>';
            }
        });
        htmlSellerCityId += '</select>';
        $('.buyerDistrictId').html(htmlSellerCityId);
        if ($(this).val() === '0') {
            $('.buyerDistrictId').html("");
        }
    });
    if (typeof $('.sellerDistrictId').attr("city") !== 'undefined' && $('.sellerDistrictId').attr("city") !== null && $('.sellerDistrictId').attr("city") !== '') {
        $('select[name=sellerCityId]').val($('.sellerDistrictId').attr("city"));
        if (typeof $('.sellerDistrictId').attr("dist") !== 'undefined' && $('.sellerDistrictId').attr("dist") !== null && $('.sellerDistrictId').attr("dist") !== '') {

            var htmlSellerCityId = '<select name="sellerDistrictId" id="sellerDistrictId" class="form-control"><option value="0">Chọn quận / huyện</option>';
            $.each(districts, function () {
                if (this.cityId === $('select[name=sellerCityId]').val()) {
                    htmlSellerCityId += '<option value="' + this.id + '" >' + this.name + '</option>';
                }
            });
            htmlSellerCityId += '</select>';
            $('.sellerDistrictId').html(htmlSellerCityId);
            $('select[name=sellerDistrictId]').val($('.sellerDistrictId').attr("dist"));
        }
    }
    if (typeof $('.receiverDistrictId').attr("city") !== 'undefined' && $('.receiverDistrictId').attr("city") !== null && $('.receiverDistrictId').attr("city") !== '') {
        $('select[name=receiverCityId]').val($('.receiverDistrictId').attr("city"));
        if (typeof $('.receiverDistrictId').attr("dist") !== 'undefined' && $('.receiverDistrictId').attr("dist") !== null && $('.receiverDistrictId').attr("dist") !== '') {

            var htmlSellerCityId = '<select name="receiverDistrictId" id="receiverDistrictId" class="form-control"><option value="0">Chọn quận / huyện</option>';
            $.each(districts, function () {
                if (this.cityId === $('select[name=receiverCityId]').val()) {
                    htmlSellerCityId += '<option value="' + this.id + '" >' + this.name + '</option>';
                }
            });
            htmlSellerCityId += '</select>';
            $('.receiverDistrictId').html(htmlSellerCityId);
            $('select[name=receiverDistrictId]').val($('.receiverDistrictId').attr("dist"));
        }
    }
    if (typeof $('.buyerDistrictId').attr("city") !== 'undefined' && $('.buyerDistrictId').attr("city") !== null && $('.buyerDistrictId').attr("city") !== '') {
        $('select[name=buyerCityId]').val($('.buyerDistrictId').attr("city"));
        if (typeof $('.buyerDistrictId').attr("dist") !== 'undefined' && $('.buyerDistrictId').attr("dist") !== null && $('.buyerDistrictId').attr("dist") !== '') {

            var htmlSellerCityId = '<select name="buyerDistrictId" id="buyerDistrictId" class="form-control"><option value="0">Chọn quận / huyện</option>';
            $.each(districts, function () {
                if (this.cityId === $('select[name=buyerCityId]').val()) {
                    htmlSellerCityId += '<option value="' + this.id + '" >' + this.name + '</option>';
                }
            });
            htmlSellerCityId += '</select>';
            $('.buyerDistrictId').html(htmlSellerCityId);
            $('select[name=buyerDistrictId]').val($('.buyerDistrictId').attr("dist"));
        }
    }

    $.each(citys, function (i) {
        $.each($('.loadLast'), function () {
            var cityId = $(this).attr("city");
            if (citys[i].id == cityId) {
                $('.loadLast[city=' + cityId + ']').html(citys[i].name);
            }
        });
    });
    $.each(districts, function (i) {
        $.each($('.loadLast'), function () {
            var distId = $(this).attr("dist");
            if (districts[i].id == distId) {
                $('.loadLast[dist=' + distId + ']').html(districts[i].name);
            }
        });
    });

    order.getLadingFindByIds(orderIds);
};

order.getLadingFindByIds = function (orderIds) {
    ajax({
        service: '/invoice/getladingfindbyids.json',
        data: {orderIds: JSON.stringify(orderIds)},
        loading: false,
        done: function (resp) {
            var ladings = resp.data;
            $.each(ladings, function (i) {
                var paymentMethod = '';
                var shipmentService = '';
                if (ladings[i].type === 'NL') {
                    paymentMethod = ' vận chuyển';
                } else if (ladings[i].type === 'COD') {
                    paymentMethod = ' giao hàng - Thu tiền tại nhà(CoD)';
                } else if (ladings[i].type === 'NONE') {
                    paymentMethod = ' vận chuyển';
                } else if (ladings[i].type === 'VISA' || ladings[i].type === 'MASTER') {
                    paymentMethod = ' vận chuyển';
                } else {
                    paymentMethod = ' vận chuyển';
                }
                if (ladings[i].shipmentService === 'SLOW') {
                    shipmentService = 'Tiết kiệm';
                }
                if (ladings[i].shipmentService === 'FAST') {
                    shipmentService = 'Nhanh';
                }

                var innerHTML = '<p style="text-align: center"><b>Người bán duyệt:</b></p>\n\
                            <p>Vận đơn ' + paymentMethod + '</p>\n\
                            <p>Hình thức vận chuyển: ' + shipmentService + '</p>';

                $('.ladingStatus' + ladings[i].orderId).html(innerHTML);
            });

        }
    });
};
order.exExcel = function () {
    var seftUrl = baseUrl + "/cp/order/excel.html";
    urlParams = new Object();
    urlParams.id = $('input[name=id]').val();
    urlParams.itemId = $('input[name=itemId]').val();
    urlParams.sellerId = $('input[name=sellerId]').val();
    urlParams.sellerEmail = $('input[name=sellerEmail]').val();
    urlParams.sellerPhone = $('input[name=sellerPhone]').val();
    urlParams.sellerCityId = $('select[name=sellerCityId]').val();
    urlParams.sellerDistrictId = $('select[name=sellerDistrictId]').val();
    urlParams.paymentMethod = $('select[name=paymentMethod]').val();
    urlParams.paymentStatusSearch = $('select[name=paymentStatusSearch]').val();
    urlParams.shipmentStatusSearch = $('select[name=shipmentStatusSearch]').val();
    urlParams.refundStatus = $('select[name=refundStatus]').val();
    urlParams.createTimeFrom = $('input[name=createTimeFrom]').val();
    urlParams.createTimeTo = $('input[name=createTimeTo]').val();
    urlParams.paidTimeFrom = $('input[name=paidTimeFrom]').val();
    urlParams.paidTimeTo = $('input[name=paidTimeTo]').val();
    urlParams.shipmentCreateTimeFrom = $('input[name=shipmentCreateTimeFrom]').val();
    urlParams.shipmentCreateTo = $('input[name=shipmentCreateTo]').val();
    urlParams.receiverEmail = $('input[name=receiverEmail]').val();
    urlParams.receiverPhone = $('input[name=receiverPhone]').val();
    urlParams.receiverName = $('input[name=receiverName]').val();
    urlParams.receiverCityId = $('input[name=receiverCityId]').val();
    urlParams.receiverDistrictId = $('input[name=receiverDistrictId]').val();
    urlParams.buyEmail = $('input[name=buyEmail]').val();
    urlParams.buyPhone = $('input[name=buyPhone]').val();
    urlParams.buyName = $('input[name=buyName]').val();
    urlParams.buyerCityId = $('input[name=buyerCityId]').val();
    urlParams.buyerDistrictId = $('input[name=buyerDistrictId]').val();
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

order.reviewAdmin = function (id) {
    ajax({
        service: '/cpservice/sellerreview/getorderreview.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-add', 'Đánh giá uy tín', template('/cp/tpl/sellerreview/sellerreview.tpl', resp), [
                    {
                        title: 'ĐÁNH GIÁ',
                        style: 'btn-primary',
                        fn: function () {
                            ajaxSubmit({
                                service: '/cpservice/sellerreview/reviewAdmin.json',
                                id: 'form-review',
                                contentType: 'json',
                                done: function (rs) {
                                    if (rs.success) {
                                        popup.close('popup-add');
                                        popup.msg("<p>Cám ơn bạn đã đánh giá uy tín!</p><p>Điều này góp phần tạo nên cộng đồng mua bán và môi trường giao dịch an toàn, lành mạnh tại ChợĐiệnTử.</p>");
                                    } else {
                                        popup.msg(resp.message);
                                    }
                                }
                            });
                        }
                    },
                    {
                        title: 'CANCEL',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('popup-add');
                        }
                    }
                ], 'modal-lg', true);
            } else {
                popup.msg(resp.message);
            }

            $("input[name=reviewType]").change(function () {
                var op1 = '<p>Lý do mà bạn <span class="blue text-uppercase">"nên mua"</span> sản phẩm này</p>';
                var op2 = '<p>Bạn <span class="yellow text-uppercase">"không ý kiến"</span> với người bán hàng</p>';
                var op3 = '<p>Lý do mà bạn <span class="red text-uppercase">"không nên mua"</span> sản phẩm này</p>';
                if ($(this).val() === '1') {
                    $('.rel-name').html(op1);
                    $('#table-danhgia').show(1000);
                }
                if ($(this).val() === '2') {
                    $('.rel-name').html(op2);
                    $('#table-danhgia').hide(1000);
                }
                if ($(this).val() === '3') {
                    $('.rel-name').html(op3);
                    $('#table-danhgia').show(1000);
                }
            });
        }
    });
};