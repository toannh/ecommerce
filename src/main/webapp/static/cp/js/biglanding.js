var biglanding = {};
biglanding.init = function() {
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị BigLanding", "/cp/biglanding.html"],
        ["Danh sách BigLanding"]
    ]);

};
function biglandingShowPreview(coords)
{
    if (parseInt(coords.w) > 0) {
        // get image
        var container = $('#photo');
        var images = document.getElementById('photo');
        // get ti le anh
        var tiLeAnh = images.naturalWidth / container.width();
        // calculate
        var calculatex1 = Math.round(coords.x * tiLeAnh);
        var calculatey1 = Math.round(coords.y * tiLeAnh);
        var calculatewidth = Math.round(coords.w * tiLeAnh);
        var calculateheight = Math.round(coords.h * tiLeAnh);
        $('#x1').val(calculatex1);
        $('#y1').val(calculatey1);
        $('#w').val(calculatewidth);
        $('#h').val(calculateheight);
    }

}
biglanding.addlanding = function() {
    popup.open('popup-add-landing', 'Thêm mới landing', template('/cp/tpl/biglanding/editlanding.tpl'), [
        {
            title: 'Lưu lại',
            style: 'btn-info',
            fn: function() {
                ajaxUpload({
                    service: '/cpservice/biglanding/addlanding.json',
                    id: 'lcategory-form-edit',
                    contentType: 'json',
                    done: function(resp) {
                        if (resp.success) {
                            popup.msg(resp.message, function() {
                                popup.close('popup-add-landing');
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
            fn: function() {
                popup.close('popup-add-landing');
            }
        }
    ]);
    $('.timeSelect').timeSelect();
    //$('.templateHTML').html('<image src="' + staticUrl + '/cp/images/template/' + resp.data.template + '.png" width="348"/>');
    $('select[name=landingTemplate]').change(function() {
        if ($(this).val() !== '0') {
            $('.templateHTML').html('<image src="' + staticUrl + '/cp/images/template/' + $(this).val() + '/' + $(this).val() + '.png" width="340"/>');
        } else {
            $('.templateHTML').html('');
        }
    });

};
biglanding.editlanding = function(biglandingId) {
    ajax({
        service: '/cpservice/biglanding/getbiglanding.json',
        data: {biglandingId: biglandingId},
        done: function(resp) {
            if (resp.success) {
                popup.open('popup-add-landing', 'Sửa Biglanding', template('/cp/tpl/biglanding/editlanding.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-info',
                        fn: function() {
                            ajaxUpload({
                                service: '/cpservice/biglanding/addlanding.json',
                                id: 'lcategory-form-edit',
                                contentType: 'json',
                                done: function(resp) {
                                    if (resp.success) {
                                        popup.msg(resp.message, function() {
                                            popup.close('popup-add-landing');
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
                        fn: function() {
                            popup.close('popup-add-landing');
                        }
                    }
                ]);
                $('.timeSelect').timeSelect();
            }
        }

    });

};
biglanding.changeActive = function(id) {
    ajax({
        service: '/cpservice/biglanding/changeactivebiglanding.json',
        data: {id: id},
        done: function(resp) {
            if (resp.success) {
                if (resp.data.active) {
                    $('a[editStatus=' + resp.data.id + ']').html('<img src="' + staticUrl + '/cp/img/icon-enable.png">');
                } else {
                    $('a[editStatus=' + resp.data.id + ']').html('<img src="' + staticUrl + '/cp/img/icon-disable.png">');
                }
            } else {
                popup.msg(resp.message);
            }
        }
    });

};
biglanding.delbiglanding = function(id) {
    popup.confirm("Bạn có chắc muốn xóa?", function() {
        ajax({
            service: '/cpservice/biglanding/delbiglanding.json',
            data: {id: id},
            done: function(resp) {
                if (resp.success) {
                    popup.msg(resp.message);
                    setTimeout(function() {
                        window.location.reload();
                    }, 2000);
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });

};


biglanding.addCategory = function(biglandingid) {
    popup.open('popup-add-category', 'Thêm mới danh mục BigLanding', template('/cp/tpl/biglanding/editcategory.tpl', {biglandingid: biglandingid}), [
        {
            title: 'Thêm',
            style: 'btn-info',
            fn: function() {
                if ($('select[name=template]').val() === '0') {
                    popup.msg("Bạn chưa chọn template");
                    return false;
                }
                ajaxSubmit({
                    service: '/cpservice/biglanding/addbiglandingcategory.json',
                    id: 'lcategory-form-edit',
                    contentType: 'json',
                    done: function(resp) {
                        if (resp.success) {
                            popup.msg(resp.message, function() {
                                popup.close('popup-add-category');
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
            fn: function() {
                popup.close('popup-add-category');
            }
        }
    ]);
    ajax({
        service: '/cpservice/biglanding/getbiglanding.json',
        data: {biglandingId: biglandingid},
        type: 'get',
        done: function(resp) {
            if (resp.success) {
                $('select[name=template]').change(function() {
                    if ($(this).val() !== '0') {
                        $('.templateHTML').html('<image src="' + staticUrl + '/cp/images/template/' + resp.data.landingTemplate + '/box/' + $(this).val() + '.png" width="348"/>');
                    } else {
                        $('.templateHTML').html('');
                    }
                });
            }
        }
    });


};

biglanding.editCategory = function(bigLandingCateId) {
    ajax({
        service: '/cpservice/biglanding/getbiglandingcatebyid.json',
        data: {bigLandingCateId: bigLandingCateId},
        done: function(resp) {
            if (resp.success) {
                popup.open('popup-add-category', 'Sửa danh mục BigLanding', template('/cp/tpl/biglanding/editcategory.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-info',
                        fn: function() {
                            if ($('select[name=template]').val() === '0') {
                                popup.msg("Bạn chưa chọn template");
                                return false;
                            }
                            ajaxSubmit({
                                service: '/cpservice/biglanding/addbiglandingcategory.json',
                                id: 'lcategory-form-edit',
                                contentType: 'json',
                                done: function(resp) {
                                    if (resp.success) {
                                        popup.msg(resp.message, function() {
                                            popup.close('popup-add-category');
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
                        fn: function() {
                            popup.close('popup-add-category');
                        }
                    }
                ]);
                $('.templateHTML').html('<image src="' + staticUrl + '/cp/images/template/' + resp.data.template + '.png" width="348"/>');
                $('select[name=template]').change(function() {
                    if ($(this).val() !== '0') {
                        $('.templateHTML').html('<image src="' + staticUrl + '/cp/images/template/' + $(this).val() + '.png" width="348"/>');
                    } else {
                        $('.templateHTML').html('');
                    }
                });
            }
        }
    });
};
biglanding.addCategorySub = function(biglandingId, parentId) {
    popup.open('popup-add-category', 'Thêm mới danh mục BigLanding', template('/cp/tpl/biglanding/editcategory.tpl', {biglandingid: biglandingId, parentId: parentId}), [
        {
            title: 'Thêm',
            style: 'btn-info',
            fn: function() {
                ajaxSubmit({
                    service: '/cpservice/biglanding/addbiglandingcategory.json',
                    id: 'lcategory-form-edit',
                    contentType: 'json',
                    done: function(resp) {
                        if (resp.success) {
                            popup.msg(resp.message, function() {
                                popup.close('popup-add-category');
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
            fn: function() {
                popup.close('popup-add-category');
            }
        }
    ]);
};
biglanding.editCategorySub = function(bigLandingCateId) {
    ajax({
        service: '/cpservice/biglanding/getbiglandingcatebyid.json',
        data: {bigLandingCateId: bigLandingCateId},
        done: function(resp) {
            if (resp.success) {
                popup.open('popup-add-category', 'Sửa danh mục BigLanding', template('/cp/tpl/biglanding/editcategory.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-info',
                        fn: function() {
                            ajaxSubmit({
                                service: '/cpservice/biglanding/addbiglandingcategory.json',
                                id: 'lcategory-form-edit',
                                contentType: 'json',
                                done: function(resp) {
                                    if (resp.success) {
                                        popup.msg(resp.message, function() {
                                            popup.close('popup-add-category');
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
                        fn: function() {
                            popup.close('popup-add-category');
                        }
                    }
                ]);

            }
        }
    });
};
biglanding.changeActiveCate = function(id) {
    ajax({
        service: '/cpservice/biglanding/changeactivebiglandingcategory.json',
        data: {id: id},
        done: function(resp) {
            if (resp.success) {
                if (resp.data.active) {
                    $('a[editStatus=' + resp.data.id + ']').html('<img src="' + staticUrl + '/cp/img/icon-enable.png">');
                } else {
                    $('a[editStatus=' + resp.data.id + ']').html('<img src="' + staticUrl + '/cp/img/icon-disable.png">');
                }
            } else {
                popup.msg(resp.message);
            }
        }
    });

};
biglanding.changePositionCate = function(cateId) {
    ajax({
        service: '/cpservice/biglanding/changepositionbiglandingcategory.json',
        data: {id: cateId, position: $('input[for=' + cateId + ']').val()},
        type: 'get',
        done: function(resp) {
            popup.msg(resp.message, function() {
                location.reload();
            });
        }
    });
};
biglanding.loadtemplate = function(bigLandingCateId, landingTemplate) {
    ajax({
        service: '/cpservice/biglanding/getbiglandingcatebyid.json',
        data: {bigLandingCateId: bigLandingCateId},
        done: function(resp) {
            if (resp.success) {
                var temHTML = template('/cp/tpl/biglanding/' + landingTemplate + '/' + resp.data.template + '.tpl', resp);
                $('table.tbl-heartbanner').hide();
                $('#temBigLanding').html(temHTML);
                $('.fd-item').css('width', '221px');
            }
        }
    });

};
biglanding.pagination = function(pageIndex) {
    this.listItemSearch($('input[name=bigLandingCateId]').val(), pageIndex);
}
biglanding.addItem = function(bigLandingCateId) {
    var itemSearch = new Object();
    // Số trang cần lấy
    itemSearch.pageIndex = 0;
    itemSearch.pageSize = 10;
    ajax({
        service: '/cpservice/biglanding/getitembycate.json?bigLandingCateId=' + bigLandingCateId,
        data: itemSearch,
        type: 'GET',
        done: function(resp) {
            if (resp.success) {

                popup.open('popup-add-item', 'Sửa sản phẩm', template('/cp/tpl/biglanding/additem.tpl', {bigLandingCateId: bigLandingCateId, data: resp.data.data}), [
                    {
                        title: 'Xong',
                        style: 'btn-default',
                        fn: function() {
                            popup.close('popup-add-item');
                        }
                    }
                ]);
                $('#bigLandingItemRow tr').find('input').change(function() {
                    biglanding.updateBigLandingItem(this);
                });
                biglanding.listItemSearch(bigLandingCateId, 0);

            } else {
                popup.msg(resp.message);

            }
        }
    });

};
biglanding.listItemSearch = function(bigLandingCateId, pageIndex) {
    var itemSearch = new Object();
    // Tìm theo id
    itemSearch.id = $('input[name=itemId]').val();
    // Số trang cần lấy
    itemSearch.pageIndex = pageIndex;
    itemSearch.pageSize = 10;

    ajax({
        service: '/cpservice/biglanding/getitembycate.json?bigLandingCateId=' + bigLandingCateId,
        data: itemSearch,
        type: 'GET',
        done: function(resp) {
            if (resp.success) {
                var firstTr = '<tr class="success">' +
                        '<th style="text-align: center; vertical-align: middle;" >ID</th>' +
                        '<th style="text-align: center; vertical-align: middle;" >Tên sản phẩm</th>' +
                        '<th style="text-align: center; vertical-align: middle;" >Ảnh</th>' +
                        '<th style="text-align: center; vertical-align: middle;" >Vị trí</th>' +
                        '<th style="text-align: center; vertical-align: middle;" >Hiển thị</th>' +
                        '<th style="text-align: center; vertical-align: middle;" >Chức năng</th>' +
                        '</tr>';
                $(".bigLandingItemRow").html(firstTr);
                var products = resp.data.data;
                var productsLength = products.length;
                if (productsLength === 0) {
                    var html = '<tr><td colspan="3">Không có sản phẩm !</td></tr>';
                    $(".bigLandingItemRow").append(html);
                } else {
                    var tr = '';
                    for (var i = 0; i < products.length; i++) {
                        var checked = products[i].active ? 'checked="true"' : '';
                        tr = '<tr for="' + products[i].id + '" class="' + products[i].id + '">' +
                                '<td class="text-left" style="vertical-align: middle;">' + products[i].itemId + '</td>' +
                                '<td class="text-center" style="vertical-align: middle;"><input for="' + products[i].id + '" name="name" class="form-control" value="' + products[i].name + '"/></td>' +
                                '<td class="text-center" style="vertical-align: middle;"><img class="lazy" data-original="' + products[i].image + '" width="30"/></td>' +
                                '<td class="text-center" style="vertical-align: middle;"><input for="' + products[i].id + '" name="position" class="form-control" style="width:50px; text-align: center"  value="' + products[i].position + '"/></td>' +
                                '<td class="text-center" style="vertical-align: middle;"><input for="' + products[i].id + '" name="active" type="checkbox" ' + checked + ' /></td>' +
                                '<td class="text-center" style="vertical-align: middle">' +
                                '<button type="button" class="btn btn-danger bigLandingName" onclick="biglanding.delbiglandingitem(' + products[i].id + ');"  ><i class="glyphicon glyphicon-trash"></i> Xóa</button>' +
                                '</td>' +
                                '</tr>';
                        $(".bigLandingItemRow").append(tr);
                    }
                }

                $('#bigLandingItemRow tr').find('input').change(function() {
                    biglanding.updateBigLandingItem(this);
                });
                var dataPage = resp.data;
                var itemSearch = resp.data;
                // Phân trang sản phẩm
                $("#pagination").html("");
                if (dataPage.pageCount > 1) {
                    var display = 3;
                    var begin = 0;
                    var end = 0;
                    if (itemSearch.pageIndex != 0) {
                        $("#pagination").append('<li><a href="javascript:;" onclick="biglanding.pagination(1)">«</a></li>');
                        begin = itemSearch.pageIndex;
                        end = begin + 2;
                    } else {
                        begin = 1;
                        if ((begin + 2) > dataPage.pageCount)
                            end = begin + 1;
                        else
                            end = begin + 2;
                    }
                    if (itemSearch.pageIndex + 1 == dataPage.pageCount) {
                        if (itemSearch.pageIndex == 1) {
                            begin = dataPage.pageCount - display + 2;
                        }
                        if (itemSearch.pageIndex != 1)
                            begin = dataPage.pageCount - display + 1;
                        end = dataPage.pageCount;
                    }
                    for (var j = begin; j <= end; j++) {
                        var active = (itemSearch.pageIndex + 1) == j ? 'active' : '';
                        var link = '<li class="' + active + '"><a href="javascript:;" onclick="biglanding.pagination(' + j + ')">' + j + '</a></li>';
                        $("#pagination").append(link);
                    }
                    if (itemSearch.pageIndex + 1 != end) {
                        $("#pagination").append('<li><a href="javascript:;" onclick="biglanding.pagination(' + dataPage.pageCount + ')">»</a></li>');
                    }
                }
                $("img.lazy").lazyload({
                    effect: "fadeIn"
                });

            } else {
                popup.msg(resp.message);

            }
        }
    });

};

biglanding.updateBigLandingItem = function(ref) {
    var id = $(ref).attr('for');
    var position = $('#bigLandingItemRow .' + id + ' input[name=position]').val();
    var name = $('#bigLandingItemRow .' + id + ' input[name=name]').val();
    var active = $('#bigLandingItemRow .' + id + ' input[type=checkbox]').is(':checked') ? true : false;

    var datas = new Object();
    datas.id = id;
    datas.name = name;
    datas.position = position;
    datas.active = active;

    ajaxSubmit({
        service: '/cpservice/biglanding/changebiglandingitem.json',
        data: datas,
        contentType: 'json',
        loading: false,
        done: function(resp) {
            if (resp.success) {
                $('tbody tr[for=' + id + '] td').find('input').removeAttr("disabled");
            } else {
                popup.msg(resp.message);
            }
        }

    });
    $('tbody tr[for=' + id + '] td').find('input,select').attr("disabled", "disabled");
};
biglanding.changeBLItemName = function(ref) {
    var id = $(ref).attr('for');
    var name = $('.bigLandingName' + id).val();
    var datas = new Object();
    datas.id = id;
    datas.name = name;
    ajaxSubmit({
        service: '/cpservice/biglanding/changebiglandingitemname.json',
        data: datas,
        contentType: 'json',
        loading: false,
        done: function(resp) {
            if (resp.success) {
                $('tbody tr[for=' + id + '] td input[name=name]').attr('value', resp.data.name);
            } else {
                popup.msg(resp.message);
            }
        }

    });
};
biglanding.saveItem = function(cateId) {
    var data = {};
    data.itemId = $('input[name=itemId]').val();
    data.bigLandingCategoryId = cateId;
    data.active = true;
    ajax({
        service: '/cpservice/biglanding/addbiglandingitem.json',
        data: data,
        contentType: 'json',
        type: 'post',
        done: function(resp) {
            if (resp.success) {
                popup.open('crop-image', 'Sửa ảnh sản phẩm', template('/cp/tpl/biglanding/image.tpl', {data: resp.data, width: 250, height: 250}), [
                    {
                        title: 'Lưu',
                        style: 'btn-info',
                        fn: function() {
                            var data = {
                                targetId: resp.data.id,
                                imageId: $('#photo').attr("for"),
                                x: $('#x1').val(),
                                y: $('#y1').val(),
                                width: $('#w').val(),
                                height: $('#h').val()
                            };
                            ajax({
                                service: '/cpservice/biglanding/cropimage.json',
                                data: data,
                                contentType: 'json',
                                type: 'post',
                                done: function(rs) {
                                    if (rs.success) {
                                        popup.msg(rs.message, function() {
                                            popup.close('crop-image');
                                            $('.bIMG' + resp.data.id).attr('src', rs.data.imageUrl);
                                            var div = $('input[name=itemId]');
                                            $(div).parents('.form-group').removeClass('has-error');
                                            $(div).next('.help-block').remove();
                                            $('input[name=itemId]').val('');
                                            biglanding.pagination(1);
                                        });

                                    } else {
                                        popup.msg(rs.message);
                                    }
                                }
                            });
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function() {
                            popup.close('crop-image');
                        }
                    }
                ], 'modal-lg', true);
                if (resp.data.image != null) {
                    $('.thumbnail').html('<img id="photo" for="' + resp.data.item.images[0] + '" src="' + resp.data.image + '" />');
                }
                var width = $('input[name=widthF]').val();
                var height = $('input[name=heightF]').val();
                setTimeout(function() {
                    $('#photo').Jcrop({
                        onChange: showPreview,
                        minSize: [width, height],
                        aspectRatio: width / height
                    });
                }, 300);

                if ($('.body tr[for]').length <= 0) {
                    $('.body').html('');
                }
                var el = $('tr[for="' + resp.data.id + '"]');
                if (typeof $(el).html() == 'undefined' || $(el).html().trim().length <= 0) {
                    var status = '';
                    if (resp.data.active) {
                        status = 'checked="true"';
                    }
                    var html = '<tr for="' + resp.data.id + '" class="' + resp.data.id + '"><td class="text-center" style="vertical-align: middle;">' + resp.data.item.id + '</td>\
                           <td class="text-center" style="vertical-align: middle;"><input name="name" for="' + resp.data.id + '" class="form-control" onchange=""  value="' + resp.data.item.name + '"/></td>\
                            <td class = "text-center" style = "vertical-align: middle;" > <img class="bIMG' + resp.data.id + '" src="' + resp.data.image + '" width="30"/></td>\
                            <td class="text-center" style="vertical-align: middle;"><input name="position" for="' + resp.data.id + '" class="form-control" style="width:50px; text-align: center" onchange=""  value="' + resp.data.position + '"/></td>\
                          <td class="text-center" style="vertical-align: middle;"><input for="' + resp.data.id + '" name="active" type="checkbox" ' + status + ' /></td>';
                    html += '<td class="text-center" style="vertical-align: middle">\
                        <button type="button" class="btn btn-danger" onclick="biglanding.delbiglandingitem(' + resp.data.id + ');"  ><i class="glyphicon glyphicon-trash"></i> Xóa</button>\
                     </td></tr>';
                    $('.body').prepend(html);
                }
                $('#bigLandingItemRow tr').find('input').change(function() {
                    biglanding.updateBigLandingItem(this);
                });


            } else {
                var div = $('input[name=itemId]');
                if (typeof resp.data['itemId'] !== 'undefined') {
                    $(div).parents('.form-group').removeClass('has-error');
                    $(div).next('.help-block').remove();
                    $(div).parents('.form-group').addClass('has-error');
                    $(div).after('<span class="help-block">' + resp.data['itemId'] + '</span>');
                } else {
                    $(div).parents('.form-group').removeClass('has-error');
                    $(div).next('.help-block').remove();
                }
            }
        }
    });
};
biglanding.uploadImageItem = function() {
    var width = $('input[name=widthF]').val();
    var height = $('input[name=heightF]').val();
    ajaxUpload({
        service: '/cpservice/biglanding/uploadimage.json',
        id: 'image-form-i',
        contentType: 'json',
        done: function(resp) {
            if (resp.success) {
                $('.thumbnail').html('<img id="photo" for="' + resp.data.imageId + '" src="' + resp.data.imageUrl + '" />');
                $('#photo').Jcrop({
                    onChange: biglandingShowPreview,
                    minSize: [width, height],
                    aspectRatio: width / height
                });

                $('input[name=downloadImageItem]').val('');

            } else {
                popup.msg(resp.message);
            }
        }
    });
};
biglanding.downloadImageItem = function() {
    var url = $('input[name=downloadImageItem]').val();
    var targetId = $('input[name=targetId]').val();

    var width = $('input[name=widthF]').val();
    var height = $('input[name=heightF]').val();
    ajax({
        data: {imageUrl: url, targetId: targetId},
        service: "/cpservice/biglanding/downloadimage.json",
        type: "post",
        contentType: "json",
        done: function(resp) {
            if (resp.success) {
                $('.thumbnail').html('<img id="photo" for="' + resp.data.imageId + '" src="' + resp.data.imageUrl + '" />');
                $('#photo').Jcrop({
                    onChange: biglandingShowPreview,
                    minSize: [width, height],
                    aspectRatio: width / height
                });
                $('input[name=downloadImageItem]').val('');
            } else {
                popup.msg(resp.message);
            }
        }
    });
};
biglanding.delbiglandingitem = function(id) {
    popup.confirm("Bạn có chắc muốn xóa?", function() {
        ajax({
            service: '/cpservice/biglanding/delbiglandingitem.json',
            data: {id: id},
            loading: false,
            done: function(resp) {
                if (resp.success) {
                    $('tbody tr[for=' + id + '] td').find('input').attr("disabled", "disabled");
                    $('tbody tr[for=' + id + ']').addClass('danger');
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });

};
biglanding.delbiglandingcate = function(id) {
    popup.confirm("Bạn có chắc chắn muốn xóa danh mục không?", function() {
        ajax({
            service: '/cpservice/biglanding/delbiglandingcategory.json',
            data: {id: id},
            loading: false,
            done: function(resp) {
                if (resp.success) {
                    popup.msg(resp.message, function() {
                        location.reload();
                    });
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });

};

biglanding.uploadBanner = function() {
    var targetId = $('input[name=targetIdBanner]').val();
    var bigLandingId = $('input[name=bigLandingId]').val();
    ajaxUpload({
        service: '/cpservice/biglanding/uploadbanner.json?targetId=' + targetId,
        id: 'image-form-banner',
        contentType: 'json',
        done: function(resp) {
            if (resp.success) {
                ajax({
                    service: '/cpservice/biglanding/getbiglanding.json',
                    data: {biglandingId: bigLandingId},
                    type: 'get',
                    done: function(resp) {
                        if (resp.success) {
                            biglanding.loadtemplate(targetId, resp.data.landingTemplate);
                        }
                    }
                });

                $('input[name=downloadImageItem]').val('');
            } else {
                popup.msg(resp.message);
            }
        }
    });
};
biglanding.addItemNB = function(bigLandingCateId, position, tem, landingTem) {
    var widthB = 0;
    var heightB = 0;
    if (landingTem === 'LT1') {
        widthB = 220;
        heightB = 220;
    }
    ajax({
        service: '/cpservice/biglanding/getbiglandingitembycustom.json',
        data: {bigLandingCateId: bigLandingCateId, position: position},
        loading: true,
        done: function(resp) {
            popup.open('popup-add-item', 'Sửa sản phẩm', template('/cp/tpl/biglanding/imagenb.tpl',
                    {data: resp.data, bigLandingCateId: bigLandingCateId, position: position, tem: tem, width: widthB, height: heightB}), [
                {
                    title: 'Lưu',
                    style: 'btn-info',
                    fn: function() {
                        var data = {
                            targetId: $('input[name=targetIdFix]').val(),
                            imageId: $('#photo').attr("for"),
                            name: $('input[name=title]').val(),
                            x: $('#x1').val(),
                            y: $('#y1').val(),
                            position: $('#position').val(),
                            template: $('#template').val(),
                            width: $('#w').val(),
                            height: $('#h').val()
                        };
                        ajax({
                            service: '/cpservice/biglanding/cropimage.json',
                            data: data,
                            contentType: 'json',
                            type: 'post',
                            done: function(rs) {
                                if (rs.success) {
                                    popup.msg(rs.message, function() {
                                        popup.close('popup-add-item');
                                        var div = $('input[name=itemId]');
                                        $(div).parents('.form-group').removeClass('has-error');
                                        $(div).next('.help-block').remove();
                                        $('input[name=itemId]').val('');
                                        var bigLandingId = $('input[name=bigLandingId]').val();
                                        ajax({
                                            service: '/cpservice/biglanding/getbiglanding.json',
                                            data: {biglandingId: bigLandingId},
                                            loading: false,
                                            type: 'get',
                                            done: function(resp) {
                                                if (resp.success) {
                                                    biglanding.loadtemplate(bigLandingCateId, resp.data.landingTemplate);
                                                }
                                            }
                                        });
                                    });

                                } else {
                                    popup.msg(rs.message);
                                }
                            }
                        });
                    }
                },
                {
                    title: 'Hủy',
                    style: 'btn-default',
                    fn: function() {
                        popup.close('popup-add-item');
                    }
                }
            ], 'modal-lg', true);
            var width = $('input[name=widthF]').val();
            var height = $('input[name=heightF]').val();
            setTimeout(function() {
                $('#photo').Jcrop({
                    onChange: biglandingShowPreview,
                    minSize: [width, height],
                    aspectRatio: width / height
                });
            }, 300);
        }
    });

};
biglanding.saveItemNB = function(cateId, position) {
    var data = {};
    data.itemId = $('input[name=itemId]').val();
    data.bigLandingCategoryId = cateId;
    data.featured = true;
    data.active = true;
    data.position = position;
    ajax({
        service: '/cpservice/biglanding/addbiglandingitem.json',
        data: data,
        contentType: 'json',
        type: 'post',
        done: function(resp) {
            if (resp.success) {
                if (resp.data.image != null) {
                    $('.thumbnail').html('<img id="photo" for="' + resp.data.item.images[0] + '" src="' + resp.data.image + '" />');
                }
                var width = $('input[name=widthF]').val();
                var height = $('input[name=heightF]').val();
                setTimeout(function() {
                    $('#photo').Jcrop({
                        onChange: biglandingShowPreview,
                        minSize: [width, height],
                        aspectRatio: width / height
                    });
                }, 300);
                $('input[name=itemIdFix]').val(resp.data.itemId);
                $('input[name=title]').val(resp.data.name);
                $('input[name=targetId]').val(resp.data.id);
                $('input[name=targetIdFix]').val(resp.data.id);
                var div = $('input[name=itemId]');
                $(div).parents('.form-group').removeClass('has-error');
                $(div).next('.help-block').remove();
            } else {
                var div = $('input[name=itemId]');
                if (typeof resp.data['itemId'] !== 'undefined') {
                    $(div).parents('.form-group').removeClass('has-error');
                    $(div).next('.help-block').remove();
                    $(div).parents('.form-group').addClass('has-error');
                    $(div).after('<span class="help-block">' + resp.data['itemId'] + '</span>');
                } else {
                    $(div).parents('.form-group').removeClass('has-error');
                    $(div).next('.help-block').remove();
                }
            }
        }
    });
};

biglanding.changeBannerLogo = function(bigLandingId) {
    ajax({
        service: '/cpservice/biglanding/getbiglanding.json',
        data: {biglandingId: bigLandingId},
        type: 'get',
        done: function(resp) {
            if (resp.success) {
                popup.open('popup-add-item', 'Thay Banner và Logo Landing', template('/cp/tpl/biglanding/bannerlogo.tpl', resp), [
                    {
                        title: 'Xong',
                        style: 'btn-default',
                        fn: function() {
                            popup.close('popup-add-item');
                        }
                    }
                ]);
                $('#bigLandingSellerRow tr').find('input').change(function() {
                    biglanding.updateBigLandingSeller(this);
                });

            } else {
                popup.msg(resp.message);

            }
        }
    });

};

biglanding.uploadBannerLD = function(type) {
    var targetId = $('input[name=targetIdBanner]').val();
    ajaxUpload({
        service: '/cpservice/biglanding/uploadlandingbanner.json?targetId=' + targetId + '&type=' + type,
        id: 'image-form-banner' + type,
        contentType: 'json',
        done: function(resp) {
            if (resp.success) {
                $('#image-form-banner' + type + ' img').show();
                $('#image-form-banner' + type + ' img').attr("src", resp.data.imageUrl);
            } else {
                popup.msg(resp.message);
            }
        }
    });
};
biglanding.changeBannerCenterActive = function(id) {
    ajax({
        service: '/cpservice/biglanding/changebannercenteractive.json',
        data: {id: id},
        done: function(resp) {
            if (resp.success) {

            } else {
                popup.msg(resp.message);
            }
        }
    });

};
biglanding.changeBackground = function(id) {
    var background = $('#background-form input[name=background]').val();
    ajax({
        service: '/cpservice/biglanding/changebackground.json',
        data: {id: id, background: background},
        done: function(resp) {
            if (resp.success) {

            } else {
                popup.msg(resp.message);
            }
        }
    });

};