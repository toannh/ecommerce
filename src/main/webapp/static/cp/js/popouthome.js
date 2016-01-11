popouthome = {};
popouthome.init = function () {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị POPOUT trang chủ", "/cp/popouthome.html"],
        ["Danh sách POPOUT trang chủ"]
    ]);

};
popouthome.add = function () {
    popup.open('popup-add', 'Thêm mới POPOUT', template('/cp/tpl/popouthome/add.tpl'), [
        {
            title: 'Lưu lại',
            style: 'btn-primary',
            fn: function () {
                ajaxUpload({
                    service: '/cpservice/popouthome/add.json',
                    id: 'form-add',
                    contentType: 'json',
                    done: function (rs) {
                        if (rs.success) {
                            popup.msg(rs.message, function () {
                                setTimeout(function () {
                                    window.location.reload();
                                }, 1000);
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
                popup.close('popup-add');
            }
        }
    ]);
    $('input[id=lefile]').change(function () {
        $('#photoCover').val($(this).val());
    });

};

popouthome.edit = function (id) {
    ajax({
        service: '/cpservice/popouthome/get.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                popup.open('popup-add', 'Sửa POPOUT', template('/cp/tpl/popouthome/add.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-primary',
                        fn: function () {
                            ajaxUpload({
                                service: '/cpservice/popouthome/edit.json',
                                id: 'form-add',
                                contentType: 'json',
                                done: function (rs) {
                                    if (rs.success) {
                                        popup.msg(rs.message, function () {
                                            setTimeout(function () {
                                                window.location.reload();
                                            }, 1000);
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
                            popup.close('popup-add');
                        }
                    }
                ]);
                $('input[id=lefile]').change(function () {
                    $('#photoCover').val($(this).val());
                });

            } else {
                popup.msg(resp.message);
            }
        }
    });
};

popouthome.changeActive = function (id) {
    ajax({
        service: '/cpservice/popouthome/editstatus.json',
        data: {id: id},
        loading:false,
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
popouthome.del = function (id) {
    popup.confirm("Bạn có chắc muốn xóa POPOUT?", function () {
        ajax({
            service: '/cpservice/popouthome/del.json',
            data: {id: id},
            done: function (resp) {
                if (resp.success) {
                       $('#'+id).addClass('danger');
                } else {
                    popup.msg(resp.message);
                }
            }
        });
    });
};
