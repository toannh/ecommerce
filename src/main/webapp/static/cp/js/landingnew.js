var landingnew = {};
landingnew.init = function () {
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị Landing New", "/cp/landingnew.html"],
        ["Danh sách Landing New"]
    ]);
    $("img.lazy").lazyload({
        effect: "fadeIn"
    });
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
landingnew.add = function () {
    popup.open('popup-add-landingnew', 'Thêm mới Landing New', template('/cp/tpl/landingnew/editlanding.tpl'), [
        {
            title: 'Lưu lại',
            style: 'btn-info',
            fn: function () {
                ajaxUpload({
                    service: '/cpservice/landingnew/add.json',
                    id: 'landingNewForm',
                    contentType: 'json',
                    done: function (resp) {
                        if (resp.success) {
                            popup.msg(resp.message, function () {
                                popup.close('popup-add-landingnew');
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
                popup.close('popup-add-landingnew');
            }
        }
    ]);
    $('#colorpicker').ColorPicker({
        onShow: function (colpkr) {
            $(colpkr).fadeIn(500);
            return false;
        },
        onHide: function (colpkr) {
            $(colpkr).fadeOut(500);
            return false;
        },
        onChange: function (hsb, hex, rgb) {
            $('input[name=color]').val('#' + hex);
        }
    });
    $('.colorpicker').css("z-index", "99999999999");
    $('input[id=lefile]').change(function () {
        $('#photoCover').val($(this).val());
    });


};
landingnew.edit = function (landingNewId) {
    ajax({
        service: '/cpservice/landingnew/get.json',
        data: {landingNewId: landingNewId},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-add-landingnew', 'Sửa Landing New', template('/cp/tpl/landingnew/editlanding.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-info',
                        fn: function () {
                            ajaxUpload({
                                service: '/cpservice/landingnew/add.json',
                                id: 'landingNewForm',
                                contentType: 'json',
                                done: function (resp) {
                                    if (resp.success) {
                                        popup.msg(resp.message, function () {
                                            popup.close('popup-add-landingnew');
                                            location.reload();
                                        });
                                    } else {
                                        popup.msg(resp.message);
                                    }
                                }});
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('popup-add-landingnew');
                        }
                    }
                ]);
                $('input[id=lefile]').change(function () {
                    $('#photoCover').val($(this).val());
                });
                $('#colorpicker').ColorPicker({
                    onShow: function (colpkr) {
                        $(colpkr).fadeIn(500);
                        return false;
                    },
                    onHide: function (colpkr) {
                        $(colpkr).fadeOut(500);
                        return false;
                    },
                    onChange: function (hsb, hex, rgb) {
                        $('input[name=color]').val('#' + hex);
                    }
                });
                $('.colorpicker').css("z-index", "99999999999");
            }
        }
    });

};


landingnew.changeActive = function (landingNewId) {
    ajax({
        service: '/cpservice/landingnew/changeactive.json',
        data: {landingNewId: landingNewId},
        done: function (resp) {
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
landingnew.del = function (landingNewId) {
    popup.confirm("Bạn có chắc muốn xóa?", function () {
        ajax({
            service: '/cpservice/landingnew/del.json',
            data: {landingNewId: landingNewId},
            done: function (resp) {
                if (resp.success) {
                    popup.msg(resp.message);
                    setTimeout(function () {
                        window.location.reload();
                    }, 2000);
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });

};

landingnew.pagination = function (pageIndex) {
    this.listItemSearch($('input[name=bigLandingCateId]').val(), pageIndex);
}
landingnew.addItem = function (landingNewId) {
    var itemSearch = new Object();
    // Số trang cần lấy
    itemSearch.pageIndex = 0;
    itemSearch.pageSize = 10;
    ajax({
        service: '/cpservice/landingnew/getitembylanding.json?landingNewId=' + landingNewId,
        data: itemSearch,
        type: 'GET',
        done: function (resp) {
            if (resp.success) {

                popup.open('popup-add-item', 'Sửa sản phẩm', template('/cp/tpl/landingnew/additem.tpl', {landingNewId: landingNewId, data: resp.data.data}), [
                    {
                        title: 'Xong',
                        style: 'btn-default', fn: function () {
                            popup.close('popup-add-item');
                        }
                    }
                ], 'modal-lg', true);
                $('#bigLandingItemRow tr').find('input').change(function () {
                    landingnew.updateBigLandingItem(this);
                });
                landingnew.listItemSearch(landingNewId, 0);

            } else {
                popup.msg(resp.message);
            }
        }
    });

};
landingnew.listItemSearch = function (landingNewId, pageIndex) {
    var itemSearch = new Object();
    // Tìm theo id
    itemSearch.id = $('input[name=itemId]').val();
    // Số trang cần lấy
    itemSearch.pageIndex = pageIndex;
    itemSearch.pageSize = 20;

    ajax({
        service: '/cpservice/landingnew/getitembylanding.json?landingNewId=' + landingNewId,
        data: itemSearch,
        type: 'GET',
        done: function (resp) {
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
                    var html = ' <tr><td class="text-center" colspan="6"><p style="color: red ;"> Chưa có sản phẩm nào! </p></td></tr>';
                    $(".bigLandingItemRow").append(html);
                } else {
                    var tr = '';
                    for (var i = 0; i < products.length; i++) {
                        var checked = products[i].active ? 'checked="true"' : '';
                        tr = '<tr for="' + products[i].id + '" class="' + products[i].id + '">' +
                                '<td class="text-left" style="vertical-align: middle;">' + products[i].itemId + '</td>' +
                                '<td class="text-center" style="vertical-align: middle;"><input for="' + products[i].id + '" name="name" class="form-control" value="' + products[i].name + '"/></td>' + '<td class="text-center" style="vertical-align: middle;"><img class="lazy" data-original="' + products[i].image + '" width="30"/></td>' +
                                '<td class="text-center" style="vertical-align: middle;"><input for="' + products[i].id + '" name="position" class="form-control" style="width:50px; text-align: center"  value="' + products[i].position + '"/></td>' +
                                '<td class="text-center" style="vertical-align: middle;"><input for="' + products[i].id + '" name="active" type="checkbox" ' + checked + ' /></td>' +
                                '<td class="text-center" style="vertical-align: middle">' +
                                '<button type="button" class="btn btn-danger landingNewName" onclick="landingnew.delitem(' + products[i].id + ');"  ><i class="glyphicon glyphicon-trash"></i> Xóa</button>' +
                                '</td>' +
                                '</tr>';
                        $(".bigLandingItemRow").append(tr);
                    }
                }

                $('#bigLandingItemRow tr').find('input').change(function () {
                    landingnew.updateLandingNewItem(this);
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
                        var link = '<li class="' + active + '"><a href="javascript:;" onclick="landingnew.pagination(' + j + ')">' + j + '</a></li>';
                        $("#pagination").append(link);
                    }
                    if (itemSearch.pageIndex + 1 != end) {
                        $("#pagination").append('<li><a href="javascript:;" onclick="landingnew.pagination(' + dataPage.pageCount + ')">»</a></li>');
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

landingnew.updateLandingNewItem = function (ref) {
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
        service: '/cpservice/landingnew/changelandingnewitem.json',
        data: datas, contentType: 'json', loading: false,
        done: function (resp) {
            if (resp.success) {
                $('tbody tr[for=' + id + '] td').find('input').removeAttr("disabled");
            } else {
                popup.msg(resp.message);
            }
        }

    });
    $('tbody tr[for=' + id + '] td').find('input,select').attr("disabled", "disabled");
};
landingnew.changeItemName = function (ref) {
    var id = $(ref).attr('for');
    var name = $('.landingNewName' + id).val();
    var datas = new Object();
    datas.id = id;
    datas.name = name;
    ajaxSubmit({
        service: '/cpservice/landingnew/changelandingitemname.json',
        data: datas,
        contentType: 'json',
        loading: false, done: function (resp) {
            if (resp.success) {
                $('tbody tr[for=' + id + '] td input[name=name]').attr('value', resp.data.name);
            } else {
                popup.msg(resp.message);
            }
        }

    });
};
landingnew.saveItem = function (cateId) {
    var data = {};
    data.itemId = $('input[name=itemId]').val();
    data.landingNewId = cateId;
    data.active = true;
    ajax({
        service: '/cpservice/landingnew/addlandingnewitem.json',
        data: data,
        contentType: 'json', type: 'post',
        done: function (resp) {
            if (resp.success) {
                popup.open('crop-image', 'Sửa ảnh sản phẩm', template('/cp/tpl/landingnew/image.tpl', {data: resp.data, width: 250, height: 250}), [
                    {
                        title: 'Lưu',
                        style: 'btn-info',
                        fn: function () {
                            var data = {
                                targetId: resp.data.id,
                                imageId: $('#photo').attr("for"),
                                x: $('#x1').val(),
                                y: $('#y1').val(),
                                width: $('#w').val(),
                                height: $('#h').val()
                            };
                            ajax({
                                service: '/cpservice/landingnew/cropimage.json',
                                data: data,
                                contentType: 'json',
                                type: 'post',
                                done: function (rs) {
                                    if (rs.success) {
                                        popup.msg(rs.message, function () {
                                            popup.close('crop-image');
                                            $('.bIMG' + resp.data.id).attr('src', rs.data.imageUrl);
                                            var div = $('input[name=itemId]');
                                            $(div).parents('.form-group').removeClass('has-error');
                                            $(div).next('.help-block').remove();
                                            $('input[name=itemId]').val('');
                                            landingnew.pagination(1);
                                        });

                                    } else {
                                        popup.msg(rs.message);
                                    }
                                }
                            });
                        }
                    },
                    {title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('crop-image');
                        }
                    }
                ], 'modal-lg', true);
                if (resp.data.image != null) {
                    $('.thumbnail').html('<img id="photo" for="' + resp.data.item.images[0] + '" src="' + resp.data.image + '" />');
                }
                var width = $('input[name=widthF]').val();
                var height = $('input[name=heightF]').val();
                setTimeout(function () {
                    $('#photo').Jcrop({onChange: showPreview,
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
                    <button type="button" class="btn btn-danger" onclick="landingnew.delitem(' + resp.data.id + ');"  ><i class="glyphicon glyphicon-trash"></i> Xóa</button>\
                     </td></tr>';
                    $('.body').prepend(html);
                }
                $('#bigLandingItemRow tr').find('input').change(function () {
                    landingnew.updateLandingNewItem(this);
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
landingnew.uploadImageItem = function () {
    var width = $('input[name=widthF]').val();
    var height = $('input[name=heightF]').val();
    ajaxUpload({
        service: '/cpservice/landingnew/uploadimage.json',
        id: 'image-form-i',
        contentType: 'json',
        done: function (resp) {
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
landingnew.downloadImageItem = function () {
    var url = $('input[name=downloadImageItem]').val();
    var targetId = $('input[name=targetId]').val();
    var width = $('input[name=widthF]').val();
    var height = $('input[name=heightF]').val();
    ajax({data: {imageUrl: url, targetId: targetId},
        service: "/cpservice/landingnew/downloadimage.json",
        type: "post",
        contentType: "json",
        done: function (resp) {
            if (resp.success) {
                $('.thumbnail').html('<img id="photo" for="' + resp.data.imageId + '" src="' + resp.data.imageUrl + '" />');
                $('#photo').Jcrop({
                    onChange: biglandingShowPreview,
                    minSize: [width, height],
                    aspectRatio: width / height});
                $('input[name=downloadImageItem]').val('');
            } else {
                popup.msg(resp.message);
            }
        }
    });
};
landingnew.delitem = function (id) {
    popup.confirm("Bạn có chắc muốn xóa?", function () {
        ajax({
            service: '/cpservice/landingnew/delitem.json',
            data: {id: id},
            loading: false,
            done: function (resp) {
                if (resp.success) {
                    $('tbody tr[for=' + id + '] td').find('input').attr("disabled", "disabled");
                    $('tbody tr[for=' + id + ']').addClass('danger');
                    setTimeout(function () {
                        $('tbody tr[for=' + id + ']').hide(1000);
                    }, 1000);
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });

};
landingnew.addslide = function (landingNewId) {
    popup.open('popup-add-landingnewslide', 'Thêm/Sửa Landing New', template('/cp/tpl/landingnew/editlandingslide.tpl', {landingNewId: landingNewId}), [
        {
            title: 'Lưu lại',
            style: 'btn-info',
            fn: function () {
                ajaxUpload({
                    service: '/cpservice/landingnew/addslide.json',
                    id: 'landingNewSlideForm',
                    contentType: 'json',
                    done: function (resp) {
                        if (resp.success) {
                            popup.msg(resp.message, function () {
                                popup.close('popup-add-landingnewslide');
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
                popup.close('popup-add-landingnewslide');
            }
        }
    ]);
    $('input[id=lefile]').change(function () {
        $('#photoCover').val($(this).val());
    });


};

landingnew.editslide = function (landingNewId) {
    ajax({
        service: '/cpservice/landingnew/getslide.json',
        data: {id: landingNewId},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-add-landingnew', 'Sửa Landing New', template('/cp/tpl/landingnew/editlandingslide.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-info',
                        fn: function () {
                            ajaxUpload({
                                service: '/cpservice/landingnew/addslide.json',
                                id: 'landingNewSlideForm',
                                contentType: 'json',
                                done: function (resp) {
                                    if (resp.success) {
                                        popup.msg(resp.message, function () {
                                            popup.close('popup-add-landingnew');
                                            location.reload();
                                        });
                                    } else {
                                        popup.msg(resp.message);
                                    }
                                }});
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('popup-add-landingnew');
                        }
                    }
                ]);
                $('input[id=lefile]').change(function () {
                    $('#photoCover').val($(this).val());
                });

            }
        }
    });


};


landingnew.changeActiveSlide = function (id) {
    ajax({service: '/cpservice/landingnew/changeactiveslide.json',
        data: {id: id},
        done: function (resp) {
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
landingnew.delSlideD = function (landingNewId) {
    popup.confirm("Bạn có chắc muốn xóa?", function () {
        ajax({
            service: '/cpservice/landingnew/delslide.json',
            data: {id: landingNewId},
            done: function (resp) {
                if (resp.success) {
                    popup.msg(resp.message);
                    setTimeout(function () {
                        window.location.reload();
                    }, 2000);
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });

};

landingnew.changePositionSlide = function (id, position) {
    position = $('[for=' + id + ']').val();
    ajax({
        service: '/cpservice/landingnew/changepositionslide.json',
        data: {id: id, position: position},
        done: function (resp) {
            if (resp.success) {
                popup.msg(resp.message);
                setTimeout(function () {
                    window.location.reload();
                }, 2000);
            } else {
                popup.msg(resp.message);
            }
        }
    });

};
landingnew.cropSlideImage = function (id) {

    ajax({
        service: '/cpservice/landingnew/getslide.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                popup.open('crop-image', 'Sửa ảnh sản phẩm', template('/cp/tpl/landingnew/image_slide.tpl', {data: resp.data, width: 80, height: 80}), [
                    {
                        title: 'Lưu',
                        style: 'btn-info',
                        fn: function () {
                            var data = {
                                targetId: resp.data.id,
                                imageId: $('#photo').attr("for"),
                                x: $('#x1').val(),
                                y: $('#y1').val(),
                                width: $('#w').val(),
                                height: $('#h').val()
                            };
                            ajax({
                                service: '/cpservice/landingnew/cropimageselide.json',
                                data: data,
                                contentType: 'json',
                                type: 'post',
                                done: function (rs) {
                                    if (rs.success) {
                                        popup.msg(rs.message, function () {
                                            popup.close('crop-image');
                                            $('.bIMG' + resp.data.id).attr('src', rs.data.imageUrl);
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
                        fn: function () {
                            popup.close('crop-image');
                        }
                    }
                ], 'modal-lg', true);
                if (resp.data.image != null) {
                    $('.thumbnail').html('<img id="photo" for="' + resp.data.imageId + '" src="' + resp.data.image + '" />');
                }
                var width = $('input[name=widthF]').val();
                var height = $('input[name=heightF]').val();
                setTimeout(function () {
                    $('#photo').Jcrop({
                        onChange: showPreview,
                        minSize: [width, height],
                        aspectRatio: width / height
                    });
                }, 300);
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