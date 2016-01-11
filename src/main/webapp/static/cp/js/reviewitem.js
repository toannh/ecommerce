/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
reviewitem = {};

reviewitem.init = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị sửa sản phẩm", "/cp/reviewitem.html"],
        ["Danh sách sản phẩm cần duyệt"]
    ]);
    $('.timeselect').timeSelect();
    model.builtCategory(params, 'itemSearch');
    $("img.lazy").lazyload({
        effect: "fadeIn"
    });
    $("input[name=checkall]").change(function () {
        if ($(this).is(":checked")) {
            $("input[for=checkall]").prop("checked", true);
        } else {
            $("input[for=checkall]").prop("checked", false);
        }
    });
};


reviewitem.edit = function (id) {
    ajax({
        service: '/cpservice/global/item/getitem.json',
        data: {id: id},
        done: function (result) {
            result.cities = cities;
            popup.open('popup-edit-item', 'Sửa sản phẩm', template('/cp/tpl/reviewitem/form.tpl', result), [
                {
                    title: 'Lưu',
                    style: 'btn-primary',
                    fn: function () {
                        ajaxSubmit({
                            service: '/cpservice/reviewitem/edit.json',
                            id: 'item-form',
                            contentType: 'json',
                            done: function (resp) {
                                if (resp.success) {
                                    popup.msg("Sửa thành công ^_^", function () {
                                        popup.close('popup-edit-item');
                                        location.reload();
                                    });

                                } else {
                                    popup.msg(resp.message);
                                }
                            }
                        });
                    }
                },
                {
                    title: 'Hủy',
                    style: 'btn-default',
                    fn: function () {
                        popup.close('popup-edit-item');
                    }
                }
            ]);
            ajax({
                service: '/cpservice/global/category/getancestors.json',
                data: {id: result.data.categoryId},
                loading: false,
                done: function (resp) {
                    if (resp.success) {
                        var params = {};
                        params.categoryId = result.data.categoryId;
                        params.parentCategorys = [];
                        params.parentCategorys.cats = resp.data.cats;
                        params.parentCategorys.ancestors = resp.data.ancestors;

                        model.builtCategory(params, 'item-form');
                    }
                }
            });
            $('#item-form .timeselect').timeSelect();
        }
    });
};

reviewitem.editProperties = function (itemId) {
    ajax({
        service: '/cpservice/global/item/getproperties.json',
        data: {id: itemId},
        done: function (resp) {
            response = resp;
            if (resp.success) {
                popup.open('popup-item-show-properties', 'Thuộc tính sản phẩm', template('/cp/tpl/model/properties.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-primary',
                        fn: function () {
                            var list = reviewitem.getPropertiesValues(resp);
                            ajax({
                                service: '/cpservice/reviewitem/editproperties.json',
                                contentType: 'json',
                                data: {id: itemId, properties: JSON.stringify(list)},
                                type: 'post',
                                done: function (result) {
                                    if (result.success) {
                                        popup.msg(result.message, function () {
                                            popup.close('popup-item-show-properties');
                                        });
                                    } else {
                                        popup.msg(result.message);
                                    }
                                }
                            });
                        }
                    }, {
                        title: 'Đóng',
                        style: 'btn-info',
                        fn: function () {
                            popup.close('popup-item-show-properties');
                        }
                    }
                ]);
                item.setSelectedPropertiesValue(resp);
            } else {
                popup.msg(resp.message);
            }
        }
    });
};

reviewitem.getPropertiesValues = function (resp) {
    var list = new Array();
    $.each(resp.data.categoryProperties, function (i) {
        var listproperties = {};
        listproperties.categoryPropertyId = this.id;
        listproperties.categoryPropertyValueIds = [];
        if (resp.data.categoryProperties[i].type === 'INPUT') {
            $.each($('#viewPropertys input[for=' + this.id + ']'), function () {
                var inputValues = this.value;
                if (inputValues !== 'undefined' && inputValues !== null && inputValues.trim() !== '') {
                    listproperties.inputValue = inputValues;
                    //list.push(listproperties);
                }
            });
            if (typeof listproperties.inputValue !== 'undefined' && listproperties.inputValue !== null && listproperties.inputValue !== '') {
                list.push(listproperties);
            }
        }
        if (resp.data.categoryProperties[i].type === 'MULTIPLE') {
            $.each($('#viewPropertys .row[for=' + this.id + ']').find('input[type="checkbox"]:checked'), function (n) {
                listproperties.categoryPropertyValueIds.push($(this).attr('for'));
                //list.push(listproperties);
            });
            if (listproperties.categoryPropertyValueIds.length > 0) {
                list.push(listproperties);
            }

        }
        if (resp.data.categoryProperties[i].type === 'SINGLE_OR_INPUT') {
            $.each($('#viewPropertys select[name=selectinput][for=' + this.id + ']'), function () {
                var inputValues = $('#viewPropertys input[name=inputselect][for=' + listproperties.categoryPropertyId + ']').val();
                if (inputValues !== 'undefined' && inputValues !== null && inputValues.trim() !== '') {
                    listproperties.inputValue = inputValues;
                    //list.push(listproperties);
                } else {
                    if ($(this).val() !== '0') {
                        listproperties.categoryPropertyValueIds.push($(this).val());
                        //list.push(listproperties);
                    }
                }
            });
            if (typeof listproperties.inputValue !== 'undefined' && listproperties.inputValue !== null && listproperties.inputValue !== '') {
                list.push(listproperties);
            } else {
                if (listproperties.categoryPropertyValueIds.length > 0) {
                    list.push(listproperties);
                }
            }
        }
        if (resp.data.categoryProperties[i].type === 'SINGLE') {
            $.each($('#viewPropertys select[name=selectPro][for=' + this.id + ']'), function () {
                if ($(this).val() !== '0') {
                    listproperties.categoryPropertyValueIds.push($(this).val());
                    //list.push(listproperties);
                }
            });
            if (listproperties.categoryPropertyValueIds.length > 0) {
                list.push(listproperties);
            }
        }
    });

    console.log(list);

    return list;
};

reviewitem.editFeeShip = function (itemId) {
    if (itemId !== null && itemId !== 'undefined' && itemId !== '') {
        ajax({
            service: '/cpservice/global/item/getitem.json',
            data: {id: itemId},
            done: function (resp) {
                if (resp.success) {
                    popup.open('popup-edit-feeship', 'Sửa phí vận chuyển', template('/cp/tpl/reviewitem/editfeeship.tpl', resp), [
                        {
                            title: 'Lưu lại',
                            style: 'btn-success',
                            fn: function () {
                                if ($('#free').is(':checked')) {
                                    $('input[name=shipmentPrice]').val(0);
                                }
                                var data = {};
                                data.id = itemId;
                                data.shipmentPrice = parseInt($('input[name=shipmentPrice]').val());
                                data.shipmentType = $('input[name=shipmentType]:checked').val();
                                ajax({
                                    service: '/cpservice/reviewitem/editfeeship.json',
                                    data: data,
                                    contentType: 'json',
                                    type: 'post',
                                    done: function (response) {
                                        if (response.success) {
                                            popup.msg(response.message, function () {
                                                popup.close('popup-edit-feeship');
                                            });
                                        }
                                        else {
                                            popup.msg(response.message);
                                        }
                                    }
                                });
                            }
                        },
                        {
                            title: 'Hủy',
                            style: 'btn-default',
                            fn: function () {
                                popup.close('popup-edit-feeship');
                            }
                        }
                    ]);
                    $.each($('input[name=shipmentType]'), function () {
                        if ($(this).val() === resp.data.shipmentType) {
                            if ($(this).val() === 'FIXED') {
                                if (resp.data.shipmentPrice > 0 && $(this).attr("id") === "fixed") {
                                    $(this).attr("checked", "true");
                                } else if (resp.data.shipmentPrice <= 0 && $(this).attr("id") === "free") {
                                    $(this).attr("checked", "true");
                                }
                            } else {
                                $(this).attr("checked", "true");
                            }
                        }
                    });

                    $('input[name=shipmentPrice]').val(resp.data.shipmentPrice <= 0 ? 0 : resp.data.shipmentPrice);
                }
            }
        });
    }

};

reviewitem.editImages = function (id) {
    ajax({
        service: '/cpservice/global/item/getitem.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-item-show-images', 'Ảnh sản phẩm', template('/cp/tpl/reviewitem/imagesEdit.tpl', {data: resp.data}), [
                    {
                        title: 'Đóng',
                        style: 'btn-info',
                        fn: function () {
                            popup.close('popup-item-show-images');
                        }
                    }
                ]);
            } else {
                popup.msg(resp.message);
            }
        }
    });
};
reviewitem.addImage = function () {
    ajaxUpload({
        service: '/cpservice/reviewitem/addimage.json',
        id: 'image-model',
        done: function (resp) {
            if (resp.success) {
                var imghtml = '';
                for (var i in resp.data.images) {
                    imghtml += '<li style="float: left; list-style: none; padding: 0px 5px; margin: 0px 5px; height: 100px; width: 100px; margin-bottom: 50px;" for="' + i + '">\
                                    <a style="cursor:pointer" >\
                                        <img height="100px" width="100px" src="' + resp.data.images[i] + '" class="grayscale" title="image ' + i + '" alt="Image ' + i + '">\
                                        <div align="center" title="Xóa ảnh này" onclick="reviewitem.deleteImage(\'' + resp.data.images[i] + '\', ' + i + ')">\
                                        <span style="margin-top:10px; text-align:center" class="glyphicon glyphicon-trash"></span> xóa\</div>\
                            </div></a></li>';
                }
                imghtml += '<div class="clearfix"></div>';
                $('#imageModel').html(imghtml);
                $('#imageModel').nextAll().remove();
                $('input[name=imageUrl]').val('');
                $('input[name=image]').val('');
            } else {
                popup.msg(resp.message);
            }
        }
    });
};
reviewitem.deleteImage = function (name, i) {
    popup.confirm('Bạn có chắc chắn muốn xóa?', function () {
        ajax({
            service: '/cpservice/reviewitem/delimage.json',
            data: {
                id: $('#image-model input[name=id]').val(),
                name: name
            },
            loading: false,
            done: function (resp) {
                if (resp.success) {
                    $('li[for=' + i + ']').remove();
                    if (resp.data.images.length < 1) {
                        $('#imageModel').parent().html('<div class="nodata" style="color: red; margin:10px 150px;">Hiện tại sản phẩm này chưa có ảnh</div><div class="clearfix"></div>');
                    }
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });
};
reviewitem.submitEdit = function (itemId) {
    popup.confirm("Bạn có chắc chắn muốn duyệt sản phẩm này lên sàn?", function () {
        ajax({
            service: '/cpservice/reviewitem/submitedit.json',
            data: {id: itemId},
            done: function (resp) {
                if (resp.success) {
                    popup.msg("Sửa thành công");
                    location.reload();
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });
};
reviewitem.editDetails = function (id) {
    ajax({
        service: '/cpservice/reviewitem/getdetail.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-item-edit', 'Sửa chi tiết sản phẩm', template('/cp/tpl/reviewitem/editdetail.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-success',
                        fn: function () {
                            var data = new Object();
                            data.itemId = id;
                            data.detail = CKEDITOR.instances['detail'].getData();
                            ajaxSubmit({
                                service: '/cpservice/reviewitem/editdetail.json',
                                data: data,
                                contentType: 'json',
                                done: function (resp) {
                                    popup.msg(resp.message, function () {
                                        popup.close('popup-item-edit');
                                    });
                                }
                            });
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('popup-item-edit');
                        }
                    }
                 ], 'modal-lg', true);
                 

                editor("detail", null);
                setTimeout(function(){
                    $('html a').css('background','#FFFF66');
                },5000);
                
            }
        }
    });
};

reviewitem.changeType = function (obj, IdShow, IdNone) {
    if ($(obj).is(":checked")) {
        $("#" + IdShow).css({'display': 'block'});
        $("#" + IdNone).css({'display': 'none'});
    }
};
reviewitem.getSelectedItems = function () {
    var listItems = new Array();
    $('input[for=checkall]:checked').each(function (i, el) {
        listItems.push($(el).attr("value"));
    });
    return listItems;
};
reviewitem.genTable = function (num, data) {
    var tableHTML = "";
    tableHTML = '<table class="table table-striped table-bordered table-responsive">';
    tableHTML += '<tbody>';
    tableHTML += '<tr>';
    tableHTML += '<th class="text-center">Id</th>';
    tableHTML += '<th class="text-center">Lý do không được duyệt</th>';
    tableHTML += '</tr>';
    for (var elem in data) {
        if (data.hasOwnProperty(elem)) {
            tableHTML += '<tr>';
            tableHTML += '<td>' + elem;
            tableHTML += '</td>';
            tableHTML += '<td>';
            for (var e in data[elem]) {
                if (data[elem].hasOwnProperty(e)) {
                    tableHTML += '-' + data[elem][e] + '<br>';
                }
            }
            tableHTML += '</td>';
            tableHTML += '</tr>';
        }
    }
    tableHTML += '</tbody>';
    tableHTML += '</table>';
    return tableHTML;

}
reviewitem.approveItems = function () {
    listItems = reviewitem.getSelectedItems();
    if (listItems.length > 0) {
        // show popup dong y va gui ajax
        popup.confirm("Bạn có chắc chắn muốn đăng bán tất cả sản phẩm đã chọn", function () {
            ajax({
                service: "/cpservice/reviewitem/submitApprove.json",
                data: {ids: JSON.stringify(listItems)},
                type : 'post',
                contentType: 'json',
                done: function (resp) {
                    if (resp.success) {
                        popup.msg("Duyệt sản phẩm thành công.");
                        location.reload();
                    } else {
                        popup.msg("" + resp.message + reviewitem.genTable(reviewitem.getSelectedItems().length, resp.data),function(){
                           location.reload();
                        });
                        
                    }
                }
            });
        });
    } else {
        popup.msg("Bạn hãy chọn sản phẩm để tiếp tục");
    }
};
reviewitem.mapCateItems = function () {
    listItems = reviewitem.getSelectedItems();
    if (listItems.length > 0) {
        ajax({
            service: '/cpservice/global/item/getitem.json',
            data: {id: listItems[0]},
            done: function (result) {
                popup.open('popup-map-item', 'Bạn muốn sửa danh mục của tất cả các sản phẩm đã chọn?', template('/cp/tpl/reviewitem/editmapitems.tpl', result), [
                    {
                        title: 'Lưu',
                        style: 'btn-primary',
                        fn: function () {
                            var selected_categoryId = $('.cateChose_5').find(":selected").val();
                            if (typeof selected_categoryId === 'undefined') {
                                selected_categoryId = $('.cateChose_4').find(":selected").val();
                            }
                            if (typeof selected_categoryId === 'undefined') {
                                selected_categoryId = $('.cateChose_3').find(":selected").val();
                            }
                            if (typeof selected_categoryId === 'undefined') {
                                selected_categoryId = $('.cateChose_2').find(":selected").val();
                            }
                            if (typeof selected_categoryId === 'undefined') {
                                selected_categoryId = $('.cateChose_1').find(":selected").val();
                            }
                            if (selected_categoryId === "") {
                                $("#message_err_map").show();
                            } else {
                                $("#message_err_map").hide();
                                ajax({
                                    service: "/cpservice/reviewitem/mapCateItems.json",
                                    data: {ids: JSON.stringify(listItems), cateId: selected_categoryId},
                                    type : 'post',
                                    contentType: 'json',
                                    done: function (resp) {
                                        if (resp.success) {
                                            popup.msg("Map danh mục sản phẩm thành công.");
                                            popup.close('popup-map-item');
                                            location.reload();
                                        } else {
                                            popup.msg(resp.message);
                                        }
                                    }
                                });

                            }
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('popup-map-item');
                        }
                    }
                ]);
                ajax({
                    service: '/cpservice/global/category/getancestors.json',
                    data: {id: result.data.categoryId},
                    loading: false,
                    done: function (resp) {
                        if (resp.success) {
                            var params = {};
                            params.categoryId = result.data.categoryId;
                            params.parentCategorys = [];
                            params.parentCategorys.cats = resp.data.cats;
                            params.parentCategorys.ancestors = resp.data.ancestors;
                            model.builtCategory(params, 'item-form');
                        }
                    }
                });
                $('#item-form .timeselect').timeSelect();
            }
        });
    } else {
        popup.msg("Bạn hãy chọn sản phẩm để tiếp tục");
    }
};

reviewitem.mapShopCateItems = function () {
    listItems = reviewitem.getSelectedItems();
    if (listItems.length > 0) {
        ajax({
            service: '/cpservice/global/item/getitem.json',
            data: {id: listItems[0]},
            done: function (result) {
                popup.open('popup-map-shop-item', 'Bạn muốn sửa danh mục shop của tất cả các sản phẩm đã chọn?', template('/cp/tpl/reviewitem/editmapshopcateitems.tpl', result), [
                    {
                        title: 'Lưu',
                        style: 'btn-primary',
                        fn: function () {
                            var selected_shopCategoryId = $('.shopCateChose_5').find(":selected").val();
                            if (typeof selected_shopCategoryId === 'undefined') {
                                selected_shopCategoryId = $('.shopCateChose_4').find(":selected").val();
                            }
                            if (typeof selected_shopCategoryId === 'undefined') {
                                selected_shopCategoryId = $('.shopCateChose_3').find(":selected").val();
                            }
                            if (typeof selected_shopCategoryId === 'undefined') {
                                selected_shopCategoryId = $('.shopCateChose_2').find(":selected").val();
                            }
                            if (typeof selected_shopCategoryId === 'undefined') {
                                selected_shopCategoryId = $('.shopCateChose_1').find(":selected").val();
                            }
                            if (selected_shopCategoryId === "") {
                                $("#message_err_shopmap").show();
                            } else {
                                $("#message_err_shopmap").hide();
                                ajax({
                                    service: "/cpservice/reviewitem/mapShopCateItems.json",
                                    data: {ids: JSON.stringify(listItems), shopCateId: selected_shopCategoryId},
                                    done: function (resp) {
                                        if (resp.success) {
                                            popup.msg("Map danh mục sản phẩm thành công.");
                                            popup.close('popup-map-shop-item');
                                            location.reload();
                                        } else {
                                            popup.msg(resp.message);
                                        }
                                    }
                                });
                            }
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('popup-map-shop-item');
                        }
                    }
                ]);
                ajax({
                    service: '/cpservice/global/shopcategory/getancestors.json',
                    data: {id: result.data.shopCategoryId},
                    loading: false,
                    done: function (resp) {
                        if (resp.success) {
                            var params = {};
                            params.shopCategoryId = result.data.shopCategoryId;
                            params.parentShopCategorys = [];
                            params.parentShopCategorys.shopCats = resp.data.shopCates;
                            params.parentShopCategorys.ancestors = resp.data.ancestors;
                            model.builtCategory(params, 'item-form-shopCate');
                        }
                    }
                });
                $('#item-form-shopCate .timeselect').timeSelect();
            }
        });
    } else {
        popup.msg("Bạn hãy chọn sản phẩm để tiếp tục");
    }
};

reviewitem.editNameItems = function () {
    listItems = reviewitem.getSelectedItems();
    if (listItems.length > 0) {
        popup.open('popup-editname-item', 'Bạn muốn sửa tên của tất cả các sản phẩm đã chọn?', template('/cp/tpl/reviewitem/editnameitems.tpl'), [
            {
                title: 'Lưu',
                style: 'btn-primary',
                fn: function () {
                    var prefix_name = $('#prefix_name').val();
                    var suffix_name = $('#suffix_name').val();
                    if (prefix_name == '' && suffix_name == '') {
                        $("#message_err_name").show();
                    } else {
                        $("#message_err_name").hide();
                        ajax({
                            service: "/cpservice/reviewitem/editNameItems.json",
                            data: {ids: JSON.stringify(listItems), prename: prefix_name, sufname: suffix_name},
                            type : 'post',
                            contentType: 'json',
                            done: function (resp) {
                                if (resp.success) {
                                    popup.msg("Sửa tên sản phẩm thành công.");
                                    popup.close('popup-editname-item');
                                    location.reload();
                                } else {
                                    popup.msg(resp.message);
                                }
                            }
                        });
                    }
                }
            },
            {
                title: 'Hủy',
                style: 'btn-default',
                fn: function () {
                    popup.close('popup-editname-item');
                }
            }
        ]);
    } else {
        popup.msg("Bạn hãy chọn sản phẩm để tiếp tục");
    }
};

reviewitem.replaceNameItems = function () {
    listItems = reviewitem.getSelectedItems();
    if (listItems.length > 0) {
        popup.open('popup-replacename-item', 'Bạn muốn sửa tên của tất cả các sản phẩm đã chọn?', template('/cp/tpl/reviewitem/replacenameitems.tpl'), [
            {
                title: 'Lưu',
                style: 'btn-primary',
                fn: function () {
                    var pattern_name = $('#pattern_name').val();
                    var replace_name = $('#replace_name').val();
                    if (pattern_name === '' && replace_name === '') {
                        $("#message_err_name").show();
                    } else {
                        $("#message_err_name").hide();
                        ajax({
                            service: "/cpservice/reviewitem/replaceNameItems.json",
                            data: {ids: JSON.stringify(listItems), pattern_name: pattern_name, replace_name: replace_name},
                            type : 'post',
                            contentType: 'json',
                            done: function (resp) {
                                if (resp.success) {
                                    popup.msg("Sửa tên sản phẩm thành công.");
                                    popup.close('popup-replacename-item');
                                    location.reload();
                                } else {
                                    popup.msg(resp.message);
                                }
                            }
                        });
                    }
                }
            },
            {
                title: 'Hủy',
                style: 'btn-default',
                fn: function () {
                    popup.close('popup-replacename-item');
                }
            }
        ]);
    } else {
        popup.msg("Bạn hãy chọn sản phẩm để tiếp tục");
    }
};

reviewitem.delNameItems = function () {
    listItems = reviewitem.getSelectedItems();
    if (listItems.length > 0) {
        // show popup dong y va gui ajax
        popup.confirm("Bạn có chắc chắn muốn Xóa tất cả sản phẩm đã chọn", function () {
            ajax({
                service: "/cpservice/reviewitem/delItems.json",
                data: {ids: JSON.stringify(listItems)},
                type : 'post',
                contentType: 'json',            
                done: function (resp) {
                    if (resp.success) {
                        popup.msg("Xóa sản phẩm thành công.");
                        location.reload();
                    } else {
                        popup.msg(resp.message);
                    }
                }
            });
        });
    } else {
        popup.msg("Bạn hãy chọn sản phẩm để tiếp tục");
    }
};

reviewitem.editWeight = function () {
    listItems = reviewitem.getSelectedItems();
    if (listItems.length > 0) {
        popup.open('popup-editweight-item', 'Bạn muốn sửa trọng lượng của tất cả các sản phẩm đã chọn?', template('/cp/tpl/reviewitem/editweight.tpl'), [
            {
                title: 'Lưu',
                style: 'btn-primary',
                fn: function () {
                    var weight_net = $('#weight_net').val();
                    if (weight_net === '') {
                        $("#message_err_weight").show();
                    } else {
                        $("#message_err_weight").hide();
                        ajax({
                            service: "/cpservice/reviewitem/editWeightItems.json",
                            data: {ids: JSON.stringify(listItems), weight_net: weight_net},
                            type : 'post',
                            contentType: 'json',            
                            done: function (resp) {
                                if (resp.success) {
                                    popup.msg("Sửa trọng lượng sản phẩm thành công.");
                                    popup.close('popup-editweight-item');
                                    location.reload();
                                } else {
//                                    popup.close('popup-editweight-item');
                                    popup.msg(resp.message);
                                }
                            }
                        });
                    }
                }
            },
            {
                title: 'Hủy',
                style: 'btn-default',
                fn: function () {
                    popup.close('popup-editweight-item');
                }
            }
        ]);
    } else {
        popup.msg("Bạn hãy chọn sản phẩm để tiếp tục");
    }
};