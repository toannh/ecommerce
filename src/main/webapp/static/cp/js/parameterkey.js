parameter = {};

parameter.init = function() {
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị thông số", "/cp/parameter.html"],
        ["Danh sách thông số"]
    ]);
};

parameter.add = function() {
    popup.open('popup-add-parameter', 'Thêm mới thông số', template('/cp/tpl/parameter/add.tpl', null), [
        {
            title: 'Tạo mới',
            style: 'btn-info',
            fn: function() {
                ajaxSubmit({
                    service: '/cpservice/parameterkey/addparameterkey.json',
                    id: 'parameter-add-form',
                    contentType: 'json',
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
            }
        },
        {
            title: 'Hủy',
            style: 'btn-default',
            fn: function() {
                popup.close('popup-add-parameter');
            }
        }
    ]);
};

parameter.edit = function(keyConvention) {
    ajax({
        service: '/cpservice/parameterkey/getparameterkeybyid.json',
        data: {keyConvention: keyConvention},
        done: function(resp) {
            if (resp.success) {
                popup.open('popup-edit-parameter', 'Sửa thông số', template('/cp/tpl/parameter/add.tpl', resp), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-info',
                        fn: function() {
                            ajaxSubmit({
                                service: '/cpservice/parameterkey/editparameterkey.json',
                                id: 'parameter-add-form',
                                contentType: 'json',
                                done: function(rs) {
                                    if (rs.success) {
                                        popup.msg("Sửa thông số thành công", function() {
                                            location.reload();
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
                            popup.close('popup-edit-parameter');
                        }
                    }
                ]);
            }
        }
    });
};

parameter.delete = function(keyConvention) {
    popup.confirm("Bạn muốn xóa thông số này không?", function() {
        ajax({
            service: '/cpservice/parameterkey/deleteparameterkey.json',
            data: {keyConvention: keyConvention},
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

