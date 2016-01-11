order = {};
order.initSeller = function () {
    $('.timeFormOrderSeller').timeSelect();
    $('.timeToOrderSeller').timeSelect();
    order.loadsellerreview(orderIds, userId, true);
    order.loadcheckmessage(orderIds, userId);

    $("input[name=checkall]").change(function () {
        if ($(this).is(":checked")) {
            $("input[for=checkall]").attr("checked", true);
        } else {
            $("input[for=checkall]").attr("checked", false);
        }
    });
    order.loadColorOrderitem(orderIds);
    order.getLadingFindByIds(orderIds);
};
order.initCod = function () {
    var html = '<option value="0" >Chọn tỉnh / thành phố</option>';
    $.each(citys, function () {
        html += '<option value="' + this.id + '" >' + this.name + '</option>';
    });
    $("select[name=sellerCityId]").html(html);
    $("select[name=receiverCityId]").html(html);
    $("select[name=sellerCityId]").val($("select[name=sellerCityId]").attr("val")).change();
    $("select[name=receiverCityId]").val($("select[name=receiverCityId]").attr("val")).change();
    $("select[name=receiverDistrictId]").val($("select[name=receiverDistrictId]").attr("val")).change();

    html = '<option value="0" >Chọn quận / huyện</option>';
    $.each(districts, function () {
        if (this.cityId === $("select[name=sellerCityId]").attr("val")) {
            html += '<option value="' + this.id + '" >' + this.name + '</option>';
        }
    });
    $("select[name=sellerDistrictId]").html(html);

    html = '<option value="0" >Chọn quận / huyện</option>';
    $.each(districts, function () {
        if (this.cityId === $("select[name=receiverCityId]").attr("val")) {
            html += '<option value="' + this.id + '" >' + this.name + '</option>';
        }
    });

    $("select[name=receiverDistrictId]").html(html);
    $("select[name=sellerDistrictId]").val($("select[name=sellerDistrictId]").attr("val")).change();
    $("select[name=receiverDistrictId]").val($("select[name=receiverDistrictId]").attr("val")).change();


//    html = '<option value="0" >Chọn phường / xã</option>';
//    $.each(wards, function () {
//        if (this.districtId === $("select[name=receiverDistrictId]").attr("val")) {
//            html += '<option value="' + this.id + '" >' + this.name + '</option>';
//        }
//    });
    var html = '<option value="0" >Chọn phường / xã</option>';
    ajax({
        service: '/ward/loadwardbydistrict.json?districtId=' + $("select[name=receiverDistrictId]").attr("val"),
        loading: false,
        success: function (resp) {
            if (resp.success) {
                $.each(resp.data, function () {
                    html += '<option value="' + this.id + '" >' + this.name + '</option>';
                });
                if (resp.data === null || resp.data.length <= 0) {
                    $("select[name=receiverWardId]").parent().parent().hide();
                } else {
                    $("select[name=receiverWardId]").parent().parent().show();
                    $("select[name=receiverWardId]").html(html);
                    if ($("select[name=receiverWardId]").attr("val") !== null && $("select[name=receiverWardId]").attr("val") !== '') {
                        $("select[name=receiverWardId]").val($("select[name=receiverWardId]").attr("val")).change();
                    }

                }

            } else {
                //$(_control).html(html);
            }
        }
    });
    // alert(html);
//    $("select[name=receiverWardId]").html(html);
//    order.loadwardbydistrict($("select[name=receiverDistrictId]").val(), "select[name=receiverWardId]");
    //$("select[name=receiverWardId]").html(html);

//    $("select[name=receiverWardId]").val($("select[name=receiverWardId]").attr("val")).change();
    //s  alert("1");

    $("select[name=receiverCityId]").change(function () {
        $("div[name=RAPID]").fadeIn();
        $("div[name=FAST]").fadeIn();
        $("div[name=SLOW]").fadeIn();
        if ($(this).val() !== '0' && $("select[name=receiverDistrictId]").val() !== '0') {
            order.calculator();
        }
    });

    $("select[name=receiverDistrictId]").change(function () {
        $("select[name=receiverDistrictId]").attr("val", $("select[name=receiverDistrictId]").val());
        $("div[name=RAPID]").fadeIn();
        $("div[name=FAST]").fadeIn();
        $("div[name=SLOW]").fadeIn();
        if ($(this).val() !== '0' && $("select[name=receiverCityId]").val() !== '0') {
            order.calculator();
        }
    });

    $("select[name=receiverWardId]").change(function () {
        $("div[name=RAPID]").fadeIn();
        $("div[name=FAST]").fadeIn();
        $("div[name=SLOW]").fadeIn();
        if ($(this).val() !== '0' && $("select[name=receiverCityId]").val() !== '0' && $("select[name=receiverDistrictId]").val() !== '0') {
            order.calculator();
        }
    });
    $("select[name=sellerCityId]").change(function () {
        $("div[name=RAPID]").fadeIn();
        $("div[name=FAST]").fadeIn();
        $("div[name=SLOW]").fadeIn();
        if ($(this).val() !== '0' && $("select[name=sellerCityId]").val() !== '0') {
            order.calculator();
        }
    });
    $("select[name=sellerDistrictId]").change(function () {
        $("div[name=RAPID]").fadeIn();
        $("div[name=FAST]").fadeIn();
        $("div[name=SLOW]").fadeIn();
        if ($(this).val() !== '0' && $("select[name=receiverCityId]").val() !== '0') {
            order.calculator();
        }
    });
    $("input[name=finalPrice]").blur(function () {
        order.calculator();
    });
    $("input[name=shipmentService]").change(function () {
        order.calculator();
    });
    $("input[name=protec]").change(function () {
        order.calculator();
    });
    $("input[name=weight]").change(function () {
        order.calculator();
        if ($('input[name=weight]').val() > 2000) {
            $('.cdt-message-note').hide();
        } else {
            $('.cdt-message-note').show();
        }
    });
    $("select[name=courierId]").change(function () {
        order.calculator();
        var pickup = $("select[name=courierId]").find(':selected').attr('pickup');
        var delivery = $("select[name=courierId]").find(':selected').attr('delivery');
        if (pickup !== 'undefined' && delivery !== 'undefined') {
            var total = eval(pickup) + eval(delivery);
            if (!isNaN(total)) {
                $("label[name=courierMoney]").html('Phí cộng thêm ' + eval(total).toMoney(0, ',', '.') + ' VNĐ');
            }
        }

    });
    order.localChange();
    order.calculator();
    order.loadStock();

};

order.loadwardbydistrict = function (_districtId, _control) {
    var html = '<option value="0" >Chọn phường / xã</option>';
    ajax({
        service: '/ward/loadwardbydistrict.json?districtId=' + _districtId,
        loading: false,
        success: function (resp) {
            if (resp.success) {
                $.each(resp.data, function () {
                    html += '<option value="' + this.id + '" >' + this.name + '</option>';
                });
                if (resp.data === null || resp.data.length <= 0) {
                    $("select[name=receiverWardId]").parent().parent().hide();
                } else {
                    $("select[name=receiverWardId]").parent().parent().show();
                }
                $(_control).html(html);
            } else {
                $(_control).html(html);
            }
        }
    });
};
order.loadStock = function () {
    var sellerStock = {};
    ajax({
        service: '/payment/loadstock.json',
        data: sellerStock,
        loading: false,
        done: function (resp) {
            if (resp.success) {
                var html = '';
                $.each(resp.data, function (i) {
                    if (resp.data[i].active) {
                        html += '<option value="' + this.id + '">' + this.name + '</option>';
                    }
                });
                $('select[name=stock]').append(html);

                $('select[name=stock]').change(function () {
                    if (typeof $("select[name=stock]").val() === 'undefined' || $("select[name=stock]").val() === null || $("select[name=stock]").val() === "") {
                        return false;
                    }
                    ajax({
                        service: '/payment/getstock.json',
                        data: {id: $("select[name=stock]").val()},
                        loading: false,
                        success: function (resp) {
                            if (resp.success) {
                                $("input[name=sellerAddress]").val(resp.data.address).change();
                                $("select[name=sellerCityId]").val(resp.data.cityId).change();
                                setTimeout(function () {
                                    $("select[name=sellerDistrictId]").val(resp.data.districtId).change();
                                }, 300);
                                $("input[name=sellerName]").val(resp.data.sellerName).change();
                                $("input[name=sellerPhone]").val(resp.data.phone).change();
                            } else {
                                $("input[name=name]").val("");
                                $("input[name=phone]").val("");
                                $("input[name=address]").val("");
                            }
                        }
                    });
                });
            } else {
                // popup.msg(resp.message);
            }
        }
    });
};

order.search = function (check, page) {
    var urlParams = order.urlParam();
    if (page == null || page < 0) {
        page = 1;
    }
    urlParams.page = page;
    if ($("input[name=keyword]").val() !== "") {
        urlParams.keyword = $("input[name=keyword]").val();
    } else {
        urlParams.keyword = null;
    }
    if ($("select[name=sort]").val() !== '' && $("select[name=sort]").val() !== "0") {
        urlParams.sort = $("select[name=sort]").val();
    } else {
        urlParams.sort = null;
    }
    if ($(".timeFormOrderSeller").val() !== "") {
        urlParams.timeForm = $(".timeFormOrderSeller").val();
    } else {
        urlParams.timeForm = null;
    }
    if ($(".timeToOrderSeller").val() !== "") {
        urlParams.timeTo = $(".timeToOrderSeller").val();
    } else {
        urlParams.timeTo = null;
    }
    var queryString = "";
    var i = 1;
    $.each(urlParams, function (key, val) {
        if (val !== null) {
            if (i === 1)
                queryString += "?";
            else
                queryString += "&";
            queryString += key + "=" + val;
            i++;
        }
    });
    if (check === false) {
        location.href = "/user/don-hang-cua-toi.html" + queryString;
    } else {
        location.href = "/user/hoa-don-ban-hang.html" + queryString;
    }
};//

order.urlParam = function () {
    var urlParams;
    var match,
            pl = /\+/g, // Regex for replacing addition symbol with a space
            search = /([^&=]+)=?([^&]*)/g,
            decode = function (s) {
                return decodeURIComponent(s.replace(pl, " "));
            },
            query = window.location.search.substring(1);
    urlParams = {};
    while (match = search.exec(query))
        urlParams[decode(match[1])] = decode(match[2]);
    return urlParams;
};

order.localChange = function () {
    $("select[name=sellerCityId]").change(function () {
        var html = '<option value="0" >Chọn quận / huyện</option>';
        $.each(districts, function () {
            if (this.cityId === $("select[name=sellerCityId]").val()) {
                html += '<option value="' + this.id + '" >' + this.name + '</option>';
            }
        });
        $("select[name=sellerDistrictId]").html(html);
    });
    $("select[name=receiverCityId]").change(function () {
        var html = '<option value="0" >Chọn quận / huyện</option>';
        $.each(districts, function () {
            if (this.cityId === $("select[name=receiverCityId]").val()) {
                html += '<option value="' + this.id + '" >' + this.name + '</option>';
            }
        });
        $("select[name=receiverDistrictId]").html(html);
    });
    $("select[name=receiverDistrictId]").change(function () {
//        var html = '<option value="0" >Chọn phường / xã</option>';
//        $.each(wards, function () {
//            if (this.districtId === $("select[name=receiverDistrictId]").val()) {
//                html += '<option value="' + this.id + '" >' + this.name + '</option>';
//            }
//        });
        //$("select[name=receiverWardId]").html(html);
        order.loadwardbydistrict($("select[name=receiverDistrictId]").val(), "select[name=receiverWardId]");
    });
};

order.changePrice = function (num) {
    var val = $(num).val().replace(/\./g, '');
    if (val !== '' && !isNaN(val)) {
        $(num).val(parseFloat(val).toMoney(0, ',', '.')).change();
    } else {
        $(num).val(0).change();
    }
};

order.calculator = function () {
    order.loading(true);
    setTimeout(function () {
        var id = $("input[name=id]").val();
        var weight = $("input[name=weight]").val();
        var price = $("input[name=finalPrice]").val().replace(/\./g, '');
        var sellerDistrictId = $("select[name=sellerDistrictId]").val();
        var sellerCityId = $("select[name=sellerCityId]").val();
        if (weight <= 0) {
            popup.msg("Trọng lượng và số tiền cần thu cod phải nhập");
        }

        $("div.loading-fast").fadeIn("slow");
        ajax({
            service: '/order/get.json',
            data: {"id": id},
            loading: false,
            success: function (resp) {
                if (resp.success) {
                    resp.data.codPrice = price;
                    var protec = false;

                    $.each($("input[name=protec]"), function () {
                        if ($(this).is(":checked")) {
                            protec = ($(this).val() === 'protec');
                        }
                    });

                    resp.data.shipmentService = $("input[name=shipmentService]").val();
                    $.each($("input[name=shipmentService]"), function () {
                        if ($(this).is(":checked")) {
                            resp.data.shipmentService = $(this).val();
                        }
                    });

                    resp.data.receiverCityId = $("select[name=receiverCityId]").val();
                    resp.data.receiverDistrictId = $("select[name=receiverDistrictId]").val();
                    resp.data.receiverWardId = $("select[name=receiverWardId]").val();
                    resp.data.paymentMethod = $("input[name=paymentMethod]").val();
                    resp.data.courierId = $("select[name=courierId]").val();
                    if ($("select[name=courierId]").val() === '0') {
                        resp.data.courierId = $("select[name=courierId]").attr("val");
                    }
                    ajax({
                        service: '/order/calculator.json?all=true&weight=' + weight + "&price=" + resp.data.finalPrice + "&sellerCityId=" + sellerCityId + "&sellerDistrictId=" + sellerDistrictId + "&protec=" + protec,
                        data: resp.data,
                        loading: false,
                        type: 'post',
                        contentType: 'json',
                        success: function (rp) {
                            $("div.loading-fast").hide();
                            if (rp.success) {
                                $("span[rel=codPrice]").html(eval(rp.data.codPrice).toMoney(0, ',', '.') + ' <sup class="price" >đ</sup>');
                                if (rp.data.shipmentPrice === -1) {
                                    $("span[rel=shipmentPrice]").html('Không hỗ trợ');
                                    $("div[rel=shipmentPrice] .text-right").html("Liên hệ sau");
                                    if (resp.data.shipmentService === 'RAPID') {
                                        //$("div[name=RAPID]").hide();
                                        $("input[name=shipmentService][value=FAST]").attr("checked", true).change();
                                    }
                                    var sellerCityId = $("select[name=sellerCityId]").val();
                                    if (sellerCityId === "537ef470e4b0d29973e03969" || sellerCityId === "537ef470e4b0d29973e039c8" && sellerCityId !== '0') {
                                        if (resp.data.shipmentService === 'FAST') {
                                            // $("div[name=FAST]").hide();
                                            $("input[name=shipmentService][value=SLOW]").attr("checked", true).change();
                                        }
                                        if (resp.data.shipmentService === 'SLOW') {
                                            $("div[name=FAST]").show();
                                            //$("div[name=SLOW]").hide();
                                            $("input[name=shipmentService][value=FAST]").attr("checked", true).change();
                                        }

                                    }

                                } else {
                                    $("span[rel=shipmentPrice]").html(eval(rp.data.shipmentPrice).toMoney(0, ',', '.') + ' <sup class="price" >đ</sup>');
                                    $("input[name=shipmentPrice]").val(rp.data.shipmentPrice);
                                }
                                if (protec === true) {
                                    $("[rel=_proteced]").fadeIn();
                                    if (rp.data.shipmentPCod > 0) {
                                        $("span[rel=proteced]").html(eval(rp.data.shipmentPCod).toMoney(0, ',', '.') + ' <sup class="price" >đ</sup>');
                                    } else {
                                        $("span[rel=proteced]").html('Miễn phí');
                                    }
                                } else {
                                    $("[rel=_proteced]").hide();
                                }

                                //CDT hỗ trợ phí vận chuyển

                                if (rp.data.cdtDiscountShipment > 0) {
                                    $("span[rel=cdtshipmentprice]").parent().fadeIn();
                                    $("span[rel=cdtshipmentprice]").html("-" + eval(rp.data.cdtDiscountShipment).toMoney(0, ',', '.') + ' <sup class="price" >đ</sup>');
                                } else {
                                    $("span[rel=cdtshipmentprice]").parent().hide();
                                }
                                if (rp.data.sellerDiscountShipment > 0) {
                                    $("span[rel=sellershipment]").parent().fadeIn();
                                    $("span[rel=sellershipment]").html("-" + eval(rp.data.sellerDiscountShipment).toMoney(0, ',', '.') + ' <sup class="price" >đ</sup>');
                                } else {
                                    $("span[rel=sellershipment]").parent().hide();
                                }
                                if (rp.data.sellerDiscountPayment > 0) {
                                    $("span[rel=sellerpayment]").parent().fadeIn();
                                    $("span[rel=sellerpayment]").html("-" + eval(rp.data.sellerDiscountPayment).toMoney(0, ',', '.') + ' <sup class="price" >đ</sup>');
                                } else {
                                    $("span[rel=sellerpayment]").parent().hide();
                                }
                                //draw couriers
                                var html = '<option value="0" >Chọn hãng vận chuyển</option>';
                                $.each(rp.data.couriers, function () {
                                    html += '<option value="' + this.courierId + '" pickup="' + this.moneyPickup + '" delivery="' + this.moneyDelivery + '" >' + this.courierName + '</option>';
                                });
                                $("select[name=courierId]").html(html);
                                $("select[name=courierId]").val(rp.data.courierId);
                                if (rp.data.courierId === 0) {
                                    // $("select[name=courierId]").val($("select[name=courierId]").attr("val"));
                                    $("label[name=courierMoney]").html('');
                                }
                                order.loading(false);
                            } else {
                                popup.msg(rp.message);
                            }
                            order.loading(false);
                        }
                    });
                } else {
                    $("div.loading-fast").hide();
                    popup.msg(resp.message);
                }

            }
        });
    }, 300);
};

order.loading = function (load) {
    if (load) {
        $('.btn-lg').addClass('disabled');
        $('.btn-lg').html("<img src='" + baseUrl + "/static/user/images/loading-fast.gif' style='height: 19px;' /> Vui lòng đợi...");
    } else {
        $('.btn-lg').html("Duyệt vận đơn");
        $('.btn-lg').removeClass('disabled');
    }
};
order.createLading = function () {
    order.loading(true);
//    if (!$("input[name=yes]").is(":checked")) {
//        popup.msg("Bạn cần đồng ý tuân thủ chính sách và quy định của dịch vụ Giao hàng - Thu tiền (CoD)");
//        return false;
//    }
    $("div.loading-fast").fadeIn("slow");
    var lading = {};
    lading.type = $("input[name=paymentMethod]").val();
    lading.orderId = $("input[name=id]").val();
    lading.sellerName = $("input[name=sellerName]").val();
    lading.sellerPhone = $("input[name=sellerPhone]").val();
    lading.sellerAddress = $("input[name=sellerAddress]").val();
    lading.sellerCityId = $("select[name=sellerCityId]").val();
    lading.sellerDistrictId = $("select[name=sellerDistrictId]").val();
    lading.receiverName = $("input[name=receiverName]").val();
    lading.receiverPhone = $("input[name=receiverPhone]").val();
    lading.receiverEmail = $("input[name=receiverEmail]").val();
    lading.receiverAddress = $("input[name=receiverAddress]").val();
    lading.receiverCityId = $("select[name=receiverCityId]").val();
    lading.receiverDistrictId = $("select[name=receiverDistrictId]").val();
    lading.receiverWardId = $("select[name=receiverWardId]").val();
    lading.shipmentService = $("input[name=shipmentService]").val();
    lading.courierId = $("select[name=courierId]").val();
    $.each($("input[name=shipmentService]"), function () {
        if ($(this).is(":checked")) {
            lading.shipmentService = $(this).val();
        }
    });
    lading.name = $("input[name=name]").val();
    lading.description = $("textarea[name=description]").val();
    lading.weight = $("input[name=weight]").val();
    lading.codPrice = $("input[name=finalPrice]").val().replace(/\./g, '');
    lading.protec = false;
    lading.shipmentPrice = $("input[name=shipmentPrice]").val();
    $.each($("input[name=protec]"), function () {
        if ($(this).is(":checked")) {
            lading.protec = ($(this).val() === 'protec');
        }
    });
    $("input[name=weight]").parents('.form-group').removeClass('has-error');
    if (lading.weight <= 0) {
        popup.msg("Tổng khối lượng phải là số lớn hơn 0");
        $("input[name=weight]").parents('.form-group').addClass('has-error');
        return false;
    }
    ajax({
        service: '/order/checkbiglanding.json',
        data: lading,
        loading: false,
        type: 'post',
        contentType: 'json',
        success: function (res) {
            if (res.success) {
                popup.confirm("Nếu duyệt ngày hôm nay bạn phải chịu 100% phí vận chuyển. Bạn có đồng ý duyệt vận đơn hay không?", function () {
                    ajax({
                        service: '/order/createlading.json',
                        data: lading,
                        loading: false,
                        type: 'post',
                        contentType: 'json',
                        success: function (resp) {
                            if (resp.success) {
                                if (resp.message !== 'BROWSE_LADING_FAIL') {
                                    xengplus.plus(100);
                                    order.loading(false);
                                    popup.msg("Bạn đã duyệt thành công! Xem chi tiết <a target='_blank' href='http://seller.shipchung.vn/#/detail/" + resp.data.scId + "'>" + resp.data.scId + "</a>", function () {
                                        location.href = baseUrl + "/user/hoa-don-ban-hang.html";
                                    });
                                } else {
                                    order.loading(false);
                                    popup.msg("Bạn đã duyệt thành công! Xem chi tiết <a target='_blank' href='http://seller.shipchung.vn/#/detail/" + resp.data.scId + "'>" + resp.data.scId + "</a>", function () {
                                        location.href = baseUrl + "/user/hoa-don-ban-hang.html";
                                    });
                                }
                            } else {
                                popup.msg(resp.message);
                                $("._error").removeClass('has-error');
                                $(".help-block").remove();
                                $.each(resp.data, function (key, message) {
                                    $("[name=" + key + "]").after('<div class="help-block">' + message + '</div>');
                                    $("[name=" + key + "]").attr("placeholder", message);
                                    $("[name=" + key + "]").parent().parent().addClass('has-error _error');
                                });
                                order.loading(false);
                            }

                            $("div.loading-fast").hide("slow");
                        }
                    });
                });
            }
            else {
                var sellerCityId = $("select[name=sellerCityId]").val();
                var ladingType = $("input[name=paymentMethod]").val();
                if (sellerCityId !== "537ef470e4b0d29973e03969" && sellerCityId !== "537ef470e4b0d29973e039c8" && sellerCityId !== '0') {
                    if (ladingType === "COD") {
                        popup.msg("Địa chỉ của quý khách CĐT chưa hỗ trợ hình thức giao hàng thu tiền, kính mong quý khách thông cảm");
                    } else {
                        popup.msg("Địa chỉ của quý khách CĐT chưa hỗ trợ vận chuyển hàng, kính mong quý khách thông cảm");
                    }
                    order.loading(false);
                    return false;
                }
                ajax({
                    service: '/order/createlading.json',
                    data: lading,
                    loading: false,
                    type: 'post',
                    contentType: 'json',
                    success: function (resp) {
                        if (resp.success) {
                            if (resp.message !== 'BROWSE_LADING_FAIL') {
                                xengplus.plus(100);
                                order.loading(false);
                                popup.msg("Bạn đã duyệt thành công! Xem chi tiết <a target='_blank' href='http://seller.shipchung.vn/#/detail/" + resp.data.scId + "'>" + resp.data.scId + "</a>", function () {
                                    location.href = baseUrl + "/user/hoa-don-ban-hang.html";
                                });
                            } else {
                                order.loading(false);
                                popup.msg("Bạn đã duyệt thành công! Xem chi tiết <a target='_blank' href='http://seller.shipchung.vn/#/detail/" + resp.data.scId + "'>" + resp.data.scId + "</a>", function () {
                                    location.href = baseUrl + "/user/hoa-don-ban-hang.html";
                                });
                            }
                        } else {
                            popup.msg(resp.message);
                            order.loading(false);
                            $(".help-block").remove();
                            $("._error").removeClass('has-error');
                            $.each(resp.data, function (key, message) {
                                $("[name=" + key + "]").after('<div class="help-block">' + message + '</div>');
                                $("[name=" + key + "]").attr("placeholder", message);
                                $("[name=" + key + "]").parent().parent().addClass('has-error _error');
                            });



                        }
                        $("div.loading-fast").hide("slow");
                    }
                });

            }
        }});
};

order.initOrderBuyer = function () {
    order.loadsellerreview(orderIds, userId, false);
    order.loadcheckmessage(orderIds, userId);
    order.loadColorOrderitem(orderIds);
    order.getLadingFindByIds(orderIds);
};
order.statusNote = function (id, type) {
    if (type == 'display') {
        $('.statusNote[for=' + id + ']').css({'display': 'block'});
        $('.textNote[for=' + id + ']').css({'display': 'none'});
    } else {
        $('.statusNote[for=' + id + ']').css({'display': 'none'});
        if ($('span[rel=noteText_' + id + ']').text().trim() === '') {
            $('.textNote[for=' + id + ']').css({'display': 'none'});
        }
        if ($('span[rel=noteText_' + id + ']').text().trim() !== '') {
            $('.textNote[for=' + id + ']').css({'display': 'block'});
        }
    }
};
order.saveNote = function (id) {
    var id = id;
    var note = $('input[name=note][rel=' + id + ']').val();
    if (note === null || note === '') {
        $('div #hasError').addClass('has-error');
        $('input[name=note][rel=' + id + ']').attr("placeholder", "Nội dung ghi chú không được để trống!");
    } else {
        $('div #hasError').removeClass('has-error');
        $('input[name=note][rel=' + id + ']').removeAttr("placeholder");
        ajaxSubmit({
            service: '/order/savenote.json',
            data: {id: id, note: note},
            loading: false,
            type: 'POST',
            done: function (resp) {
                if (resp.success) {
                    $('.statusNote[for=' + id + ']').css({'display': 'none'});
                    $('.textNote[for=' + id + ']').css({'display': 'block'});
                    $('span[rel=noteText_' + id + ']').text(' ' + resp.data.note + '');
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    }
};

order.nextPage = function (page, check) {
    var urlParams = item.urlParam();
    urlParams.page = page;
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
    if (check == false) {
        location.href = "/user/don-hang-cua-toi.html" + queryString;
    } else {
        location.href = "/user/hoa-don-ban-hang.html" + queryString;
    }
};
order.saveNoteSeller = function (id) {
    var id = id;
    var note = $('input[name=note][rel=' + id + ']').val();
    if (note == null || note == '') {
        $('div #hasError').addClass('has-error');
        $('input[name=note][rel=' + id + ']').attr("placeholder", "Nội dung ghi chú không được để trống!");
    } else {
        $('div #hasError').removeClass('has-error');
        $('input[name=note][rel=' + id + ']').removeAttr("placeholder");
        ajaxSubmit({
            service: '/order/savenoteseller.json',
            data: {id: id, note: note},
            loading: false,
            type: 'POST',
            done: function (resp) {
                if (resp.success) {
                    $('.statusNote[for=' + id + ']').css({'display': 'none'});
                    $('.textNote[for=' + id + ']').css({'display': 'block'});
                    $('span[rel=noteText_' + id + ']').text(' ' + resp.data.sellerNote + '');
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    }
};
order.linkLadingShip = function (id) {
    ajax({
        service: '/order/get.json',
        data: {id: id},
        loading: false,
        done: function (resp) {
            if (resp.success) {
                if (resp.data.paymentMethod === 'COD') {
                    popup.confirm("Người mua đặt mua giao hàng thu tiền tại nhà. Bạn đang duyệt vận đơn vận chuyển.<br/> Bạn sẽ không thu tiền người mua. Bạn có đồng ý duyệt vận đơn?", function () {
                        var url = baseUrl + "/user/" + id + "/tao-van-don-van-chuyen.html";
                        location.target = "_blank";
                        location.href = url;
                    });
                } else {
                    var url = baseUrl + "/user/" + id + "/tao-van-don-van-chuyen.html";
                    location.target = "_blank";
                    location.href = url;
                }
            } else {
                popup.msg(resp.message);
            }
        }
    });
};
order.linkLading = function (id) {
    ajax({
        service: '/order/get.json',
        data: {id: id},
        loading: false,
        done: function (resp) {
            if (resp.success) {
                if (resp.data.paymentStatus === 'PAID') {
                    popup.confirm("Người mua đã thanh toán tiền hàng. Bạn duyệt vận đơn thu tiền tại nhà. Bạn vẫn thu thêm tiền người mua?", function () {
                        var url = baseUrl + "/user/" + id + "/tao-van-don-cod.html";
                        location.target = "_blank";
                        location.href = url;

                    });
                } else {
                    var url = baseUrl + "/user/" + id + "/tao-van-don-cod.html";
                    location.target = "_blank";
                    location.href = url;
                }
            } else {
                popup.msg(resp.message);
            }
        }
    });

};
order.linkEditOrder = function (id) {
    if (id !== '') {
        var url = baseUrl + "/" + id + "/thanh-toan-gio-hang.html";
        window.open(url, '_blank');
    }
};

order.removeOrder = function (id) {
    popup.confirm("Bạn có chắc muốn xóa đơn hàng này ?", function () {
        ajax({
            service: '/order/removeorder.json',
            data: {id: id},
            done: function (resp) {
                if (resp.success) {
                    popup.msg(resp.message, function () {
                        location.reload();
                    });
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });
};
order.removeOrderBuyer = function (id) {
    popup.confirm("Bạn có chắc muốn hủy đơn hàng này ?", function () {
        ajax({
            service: '/order/removeorderbuyer.json',
            data: {id: id},
            done: function (resp) {
                if (resp.success) {
                    popup.msg(resp.message, function () {
                        location.reload();
                    });
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });
};
order.recoveryOrder = function (id) {
    popup.confirm("Bạn có chắc muốn khôi phục đơn hàng này ?", function () {
        ajax({
            service: '/order/removeorder.json',
            data: {id: id},
            done: function (resp) {
                if (resp.success) {
                    popup.msg("Khôi phục đơn hàng thành công", function () {
                        location.reload();
                    });
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });
};
order.markPaymentStatus = function (orderId, seller) {
    ajax({
        service: '/order/markpaymentstatus.json',
        data: {orderId: orderId, seller: seller},
        loading: false,
        done: function (resp) {
            if (resp.success) {
                popup.msg("Thay đổi trạng thái thanh toán thành công", function () {
                    location.reload();
                });

            } else {
                popup.msg(resp.message);
            }
        }
    });

};
order.markShipmentStatus = function (orderId, seller) {
    ajax({
        service: '/order/markshipmentstatus.json',
        data: {orderId: orderId, seller: seller},
        loading: false,
        done: function (resp) {
            if (resp.success) {
                popup.msg("Thay đổi trạng thái vận chuyển thành công", function () {
                    location.reload();
                });

            } else {
                popup.msg(resp.message);
            }
        }
    });

};
order.unmarkPaymentStatus = function (orderId, seller) {
    ajax({
        service: '/order/unmarkpaymentstatus.json',
        data: {orderId: orderId, seller: seller},
        loading: false,
        done: function (resp) {
            if (resp.success) {
                popup.msg("Thay đổi trạng thái thanh toán thành công", function () {
                    location.reload();
                });

            } else {
                popup.msg(resp.message);
            }
        }
    });

};
order.unmarkShipmentStatus = function (orderId, seller) {
    ajax({
        service: '/order/unmarkshipmentstatus.json',
        data: {orderId: orderId, seller: seller},
        loading: false,
        done: function (resp) {
            if (resp.success) {
                popup.msg("Thay đổi trạng thái vận chuyển thành công", function () {
                    location.reload();
                });

            } else {
                popup.msg(resp.message);
            }
        }
    });

};
order.refund = function (orderId) {
    ajax({
        service: '/order/refund.json',
        data: {orderId: orderId},
        loading: false,
        done: function (resp) {
            if (resp.success) {
                popup.msg(resp.message, function () {
                    location.reload();
                });

            } else {
                popup.msg(resp.message);
            }
        }
    });

};

order.review = function (id) {
    ajax({
        service: '/sellerreview/getorderreview.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-add', 'Đánh giá uy tín', template('/user/tpl/sellerreview.tpl', resp), [
                    {
                        title: 'ĐÁNH GIÁ',
                        style: 'btn-primary',
                        fn: function () {
                            ajaxSubmit({
                                service: '/sellerreview/review.json',
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

//            if ($('input[name=shipmentPrice]').attr('value') > 0) {
//                $('#abc').show();
                $("input[name=reviewType]").change(function () {
                    var op1 = '<p>Lý do mà bạn <span class="blue text-uppercase">"nên mua"</span> sản phẩm này</p>';
                    var op2 = '<p>Bạn <span class="yellow text-uppercase">"không ý kiến"</span> với người bán hàng</p>';
                    var op3 = '<p>Lý do mà bạn <span class="red text-uppercase">"không nên mua"</span> sản phẩm này</p>';
                    if ($(this).val() === '1') {
                        $('.rel-name').html(op1);
                        $('#table-danhgia').show(1000);
                        $('.disable-select').show(1000);
                    }
                    if ($(this).val() === '2') {
                        $('.rel-name').html(op2);
                        $('#table-danhgia').hide(1000);
                        $('.disable-select').hide(1000);
                        $('input[name=productQuality]').val(0);
                        $('input[name=interactive]').val(0);
                        $('input[name=shippingCosts]').val(0);
                    }
                    if ($(this).val() === '3') {
                        $('.rel-name').html(op3);
                        $('#table-danhgia').show(1000);
                        $('.disable-select').show(1000);
                    }
                });
//            } else {
//                var op4 = '<p>Đơn hàng miễn phí vận chuyển <span class="red text-uppercase">"+2"</span> điểm</p>';
//                $('.rel-name').html(op4);
//                $('#table-danhgia').hide(1000);
//                $('.disable-select').hide(1000);
//                $('#abc').hide();
//            }

            var point = 0;
            var chatluong = 0, tuongtac = 0, thoigian = 0, chiphi = 0;
            $("i[rel=chatluong]").click(function () {
                var op1 = '<span class="left" style="color: red; font-size: 14px"> -1 </span><span style="font-size: 14px">Kém</span>';
                var op2 = '<span class="left" style="color: red; font-size: 14px"> -2 </span><span style="font-size: 14px">Rất kém</span>';
                var op3 = '<span class="center" style="color: yellow; font-size: 14px"> 0 </span><span style="font-size: 14px">Bình thường</span>';
                var op4 = '<span class="right" style="color: green; font-size: 14px"> +1 </span><span style="font-size: 14px">Tốt</span>';
                var op5 = '<span class="right" style="color: green; font-size: 14px"> +2 </span><span style="font-size: 14px">Rất tốt</span>';
                var val = $(this).attr('value');
                chatluong = parseInt(val);
                $('#ratkem').tooltip({placement: 'right'});
                $('#kem').tooltip({placement: 'right'});
                $('#trungbinh').tooltip({placement: 'right'});
                $('#tot').tooltip({placement: 'right'});
                $('#rattot').tooltip({placement: 'right'});
                if (val === '-1') {
                    $('.rel-number').html(op1);
                    $('#ratkem').addClass('active');
                    $('#kem').removeClass('active');
                    $('#trungbinh').removeClass('active');
                    $('#tot').removeClass('active');
                    $('#rattot').removeClass('active');
                }
                if (val === '-2') {
                    $('.rel-number').html(op2);
                    $('#ratkem').addClass('active');
                    $('#kem').addClass('active');
                    $('#trungbinh').removeClass('active');
                    $('#tot').removeClass('active');
                    $('#rattot').removeClass('active');
                }
                if (val === '0') {
                    $('.rel-number').html(op3);
                    $('#ratkem').removeClass('active');
                    $('#kem').removeClass('active');
                    $('#trungbinh').addClass('active');
                    $('#tot').removeClass('active');
                    $('#rattot').removeClass('active');
                }
                if (val === '1') {
                    $('.rel-number').html(op4);
                    $('#ratkem').removeClass('active');
                    $('#kem').removeClass('active');
                    $('#trungbinh').removeClass('active');
                    $('#tot').addClass('active');
                    $('#rattot').removeClass('active');
                }
                if (val === '2') {
                    $('.rel-number').html(op5);
                    $('#ratkem').removeClass('active');
                    $('#kem').removeClass('active');
                    $('#trungbinh').removeClass('active');
                    $('#tot').addClass('active');
                    $('#rattot').addClass('active');
                }
                point = (chatluong + tuongtac + thoigian + chiphi);
                $('.rel-point').html(point);
                $('input[name=productQuality]').val(chatluong);
                $('input[name=interactive]').val(tuongtac);
                $('input[name=shippingCosts]').val(chiphi);
            });

            $('i[rel=tuongtac]').click(function () {
                var op1 = '<span class="left" style="color: red; font-size: 14px"> -1 </span><span style="font-size: 14px">Không nhiệt tình</span>';
                var op2 = '<span class="left" style="color: red; font-size: 14px"> -2 </span><span style="font-size: 14px">Không liên hệ được</span>';
                var op3 = '<span class="left" style="color: yellow; font-size: 14px"> 0 </span><span style="font-size: 14px">Bình thường</span>';
                var op4 = '<span class="left" style="color: green; font-size: 14px"> +1 </span><span style="font-size: 14px">Nhiệt tình giúp đỡ</span>';
                var op5 = '<span class="left" style="color: green; font-size: 14px"> +2 </span><span style="font-size: 14px">Rất hài lòng</span>';
                var val = $(this).attr('value');
                tuongtac = parseInt(val);
                $('#klienhedc').tooltip({placement: 'right'});
                $('#knhiettinh').tooltip({placement: 'right'});
                $('#binhthuong').tooltip({placement: 'right'});
                $('#nhiettinh').tooltip({placement: 'right'});
                $('#hailong').tooltip({placement: 'right'});
                if (val === '-1') {
                    $('.rel-tuongtac').html(op1);
                    $('#klienhedc').addClass('active');
                    $('#knhiettinh').removeClass('active');
                    $('#binhthuong').removeClass('active');
                    $('#nhiettinh').removeClass('active');
                    $('#hailong').removeClass('active');
                }
                if (val === '-2') {
                    $('.rel-tuongtac').html(op2);
                    $('#klienhedc').addClass('active');
                    $('#knhiettinh').addClass('active');
                    $('#binhthuong').removeClass('active');
                    $('#nhiettinh').removeClass('active');
                    $('#hailong').removeClass('active');
                }
                if (val === '0') {
                    $('.rel-tuongtac').html(op3);
                    $('#klienhedc').removeClass('active');
                    $('#knhiettinh').removeClass('active');
                    $('#binhthuong').addClass('active');
                    $('#nhiettinh').removeClass('active');
                    $('#hailong').removeClass('active');
                }
                if (val === '1') {
                    $('.rel-tuongtac').html(op4);
                    $('#klienhedc').removeClass('active');
                    $('#knhiettinh').removeClass('active');
                    $('#binhthuong').removeClass('active');
                    $('#nhiettinh').addClass('active');
                    $('#hailong').removeClass('active');
                }
                if (val === '2') {
                    $('.rel-tuongtac').html(op5);
                    $('#klienhedc').removeClass('active');
                    $('#knhiettinh').removeClass('active');
                    $('#binhthuong').removeClass('active');
                    $('#nhiettinh').addClass('active');
                    $('#hailong').addClass('active');
                }
                point = (chatluong + tuongtac + thoigian + chiphi);
                $('.rel-point').html(point);
                $('input[name=productQuality]').val(chatluong);
                $('input[name=interactive]').val(tuongtac);
                $('input[name=shippingCosts]').val(chiphi);
            });

            $('i[rel=thoigian]').click(function () {
                var op1 = '<span class="left" style="color: red; font-size: 14px"> -1 </span><span style="font-size: 14px">Giao hàng chậm</span>';
                var op2 = '<span class="left" style="color: red; font-size: 14px"> -2 </span><span style="font-size: 14px">Giao hàng quá chậm</span>';
                var op3 = '<span class="left" style="color: yellow; font-size: 14px"> 0 </span><span style="font-size: 14px">Giao hàng đúng thời gian</span>';
                var op4 = '<span class="left" style="color: green; font-size: 14px"> +1 </span><span style="font-size: 14px">Giao hàng nhanh</span>';
                var op5 = '<span class="left" style="color: green; font-size: 14px"> +2 </span><span style="font-size: 14px">Giao hàng rất nhanh</span>';
                var val = $(this).attr('value');
                thoigian = parseInt(val);
                $('#quacham').tooltip({placement: 'right'});
                $('#cham').tooltip({placement: 'right'});
                $('#dung').tooltip({placement: 'right'});
                $('#nhanh').tooltip({placement: 'right'});
                $('#ratnhanh').tooltip({placement: 'right'});
                if (val === '-1') {
                    $('.rel-thoigian').html(op1);
                    $('#quacham').addClass('active');
                    $('#cham').removeClass('active');
                    $('#dung').removeClass('active');
                    $('#nhanh').removeClass('active');
                    $('#ratnhanh').removeClass('active');
                }
                if (val === '-2') {
                    $('.rel-thoigian').html(op2);
                    $('#quacham').addClass('active');
                    $('#cham').addClass('active');
                    $('#dung').removeClass('active');
                    $('#nhanh').removeClass('active');
                    $('#ratnhanh').removeClass('active');
                }
                if (val === '0') {
                    $('.rel-thoigian').html(op3);
                    $('#quacham').removeClass('active');
                    $('#cham').removeClass('active');
                    $('#dung').addClass('active');
                    $('#nhanh').removeClass('active');
                    $('#ratnhanh').removeClass('active');
                }
                if (val === '1') {
                    $('.rel-thoigian').html(op4);
                    $('#quacham').removeClass('active');
                    $('#cham').removeClass('active');
                    $('#dung').removeClass('active');
                    $('#nhanh').addClass('active');
                    $('#ratnhanh').removeClass('active');
                }
                if (val === '2') {
                    $('.rel-thoigian').html(op5);
                    $('#quacham').removeClass('active');
                    $('#cham').removeClass('active');
                    $('#dung').removeClass('active');
                    $('#nhanh').addClass('active');
                    $('#ratnhanh').addClass('active');
                }
                point = (chatluong + tuongtac + thoigian + chiphi);
                $('.rel-point').html(point);
                $('input[name=point]').val(point);
            });

            $('i[rel=chiphi]').click(function () {
                var op1 = '<span class="left" style="color: red; font-size: 14px"> -1 </span><span style="font-size: 14px">Đắt</span>';
                var op2 = '<span class="left" style="color: red; font-size: 14px"> -2 </span><span style="font-size: 14px">Rất đắt</span>';
                var op3 = '<span class="left" style="color: yellow; font-size: 14px"> 0 </span><span style="font-size: 14px">Đúng giá thị trường</span>';
                var op4 = '<span class="left" style="color: green; font-size: 14px"> +1 </span><span style="font-size: 14px">Rẻ</span>';
                var op5 = '<span class="left" style="color: green; font-size: 14px"> +2 </span><span style="font-size: 14px">Rất rẻ</span>';
                var val = $(this).attr('value');
                chiphi = parseInt(val);
                $('#ratdat').tooltip({placement: 'right'});
                $('#dat').tooltip({placement: 'right'});
                $('#dunggia').tooltip({placement: 'right'});
                $('#re').tooltip({placement: 'right'});
                $('#ratre').tooltip({placement: 'right'});
                if (val === '-1') {
                    $('.rel-chiphi').html(op1);
                    $('#ratdat').addClass('active');
                    $('#dat').removeClass('active');
                    $('#dunggia').removeClass('active');
                    $('#re').removeClass('active');
                    $('#ratre').removeClass('active');
                }
                if (val === '-2') {
                    $('.rel-chiphi').html(op2);
                    $('#ratdat').addClass('active');
                    $('#dat').addClass('active');
                    $('#dunggia').removeClass('active');
                    $('#re').removeClass('active');
                    $('#ratre').removeClass('active');
                }
                if (val === '0') {
                    $('.rel-chiphi').html(op3);
                    $('#ratdat').removeClass('active');
                    $('#dat').removeClass('active');
                    $('#dunggia').addClass('active');
                    $('#re').removeClass('active');
                    $('#ratre').removeClass('active');
                }
                if (val === '1') {
                    $('.rel-chiphi').html(op4);
                    $('#ratdat').removeClass('active');
                    $('#dat').removeClass('active');
                    $('#dunggia').removeClass('active');
                    $('#re').addClass('active');
                    $('#ratre').removeClass('active');
                }
                if (val === '2') {
                    $('.rel-chiphi').html(op5);
                    $('#ratdat').removeClass('active');
                    $('#dat').removeClass('active');
                    $('#dunggia').removeClass('active');
                    $('#re').addClass('active');
                    $('#ratre').addClass('active');
                }
                point = (chatluong + tuongtac + thoigian + chiphi);
                $('.rel-point').html(point);
                $('input[name=productQuality]').val(chatluong);
                $('input[name=interactive]').val(tuongtac);
                $('input[name=shippingCosts]').val(chiphi);
            });
            var pointStar = $('i[rel=pointStar]').attr('value');
            if (pointStar >= 20) {
                $('#uytin1').addClass('red');
            }
            if (pointStar >= 40) {
                $('#uytin2').addClass('red');
            }
            if (pointStar >= 60) {
                $('#uytin3').addClass('red');
            }
            if (pointStar >= 80) {
                $('#uytin4').addClass('red');
            }
            if (pointStar >= 100) {
                $('#uytin5').addClass('red');
            }
        }
    });
};

order.sendMessge = function (id) {
    ajax({
        service: '/order/get.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-add', 'Nhắn người bán', template('/user/tpl/sendmess.tpl', resp), [
                    {
                        title: 'Gửi tin nhắn',
                        style: 'btn-primary',
                        fn: function () {
                            ajaxSubmit({
                                service: '/order/sendmessge.json',
                                id: 'form-message',
                                contentType: 'json',
                                done: function (rs) {
                                    if (rs.success) {
                                        popup.msg(rs.message, function () {
                                            location.reload();
                                        });
                                    } else {
                                        // popup.msg(rs.message);
                                    }
                                }
                            });
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('popup-add');
                        }
                    }
                ]);
            } else {
                popup.msg(resp.message);
            }
        }
    });

};
order.sendMessgeBuyer = function (id) {
    ajax({
        service: '/order/get.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-add', 'Nhắn người mua', template('/user/tpl/sendmessbuyer.tpl', resp), [
                    {
                        title: 'Gửi tin nhắn',
                        style: 'btn-primary',
                        fn: function () {
                            ajaxSubmit({
                                service: '/order/sendmessge.json',
                                id: 'form-message',
                                contentType: 'json',
                                done: function (rs) {
                                    if (rs.success) {
                                        popup.msg(rs.message, function () {
                                            location.reload();
                                        });
                                    } else {
                                        // popup.msg(rs.message);
                                    }
                                }
                            });
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('popup-add');
                        }
                    }
                ]);
            } else {
                popup.msg(resp.message);
            }
        }
    });

};

order.loadsellerreview = function (orderIds, userId, seller) {
    if (typeof orderIds !== 'undefined' && orderIds !== null && orderIds.length > 0) {
        ajax({
            service: '/sellerreview/getbyorderids.json?userId=' + userId + '&seller=' + seller,
            data: orderIds,
            type: 'post',
            contentType: 'json',
            loading: false,
            done: function (resp) {
                if (resp.success) {
                    $(resp.data).each(function (i) {
                        var timeH = textUtils.formatTime(resp.data[i].updateTime, 'hour');
                        $('#' + resp.data[i].orderId + 'review').html('<span class="icon16-review tool-tip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Đã viết đánh giá lúc: ' + timeH + '"></span>');
                    });
                    $('.tool-tip').tooltip();
                } else {

                }
            }
        });
    }

};
order.loadcheckmessage = function (orderIds, fromUserId) {
    if (typeof orderIds !== 'undefined' && orderIds !== null && orderIds.length > 0) {
        ajax({
            service: '/message/getbyorderids.json?fromUserId=' + fromUserId,
            data: orderIds,
            type: 'post',
            contentType: 'json',
            loading: false,
            done: function (resp) {
                if (resp.success) {
                    $(resp.data).each(function (i) {
                        $('#' + resp.data[i] + 'message').html('<span class="icon16-getreview tool-tip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Đã gửi tin nhắn"></span>');
                    });
                    $('.tool-tip').tooltip();
                } else {

                }
            }
        });
    }

};

order.linkSearchItem = function (idCate, nameCate) {
    var data = {};
    data.categoryIds = [];
    data.categoryIds.push(idCate);
    var url = baseUrl + urlUtils.browseUrl(data, nameCate);
    window.open(url, '_blank');
};

order.loadColorOrderitem = function (orderIds) {
    $.each(orderIds, function (i) {
        $.each($('#' + orderIds[i] + ' .colorsizeVal'), function () {
            var color = property.getCollor(textUtils.createAlias($(this).attr('color')));
            $(this).html('<div style="background-color:' + color + '" class="tc-item cdt-tooltip" data-toggle="tooltip" data-placement="top" data-original-title="' + $(this).attr('color') + '"></div>');
        });
    });
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

                var innerHTML = '<p class="mgt-10"><b>Trạng thái vận đơn:</b></p>\n\
                            <p>Vận đơn ' + paymentMethod + '</p>\n\
                            <p>Hình thức vận chuyển: ' + shipmentService + '</p>';

                $('#' + ladings[i].orderId + ' .sellerMethod').html(innerHTML);
            });

        }
    });
};