mapcate = {};
mapcate.init = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Lịch sử map cate", "/cp/mapcate.html"],
        ["Danh sách danh mục được map"]
    ]);
    $('.timeselect').timeSelect();
    mapcate.showNewPropInput();
};

mapcate.showNewPropInput = function(){
    $("#check-new-prop").change(function () {
        if (this.checked) {
            $('#newPropName').show();
        } else {
            $('#newPropName').hide();
        }
    });
};
mapcate.add = function () {

    popup.open('popup-add-mapcate', 'Thêm mới lệnh map danh mục', template('/cp/tpl/mapcate/add.tpl', null), [
        {
            title: 'Tạo mới',
            style: 'btn-info',
            fn: function () {
                var oricate = $("input[name=origCateId]").val();
                var destcate = $("input[name=destCateId]").val();
                var active = $("select[name=active]").val();
                var check = $("#check-new-prop").is(':checked');
                var newname = $('#newPropName').val();
                ajax({
                    service: '/cpservice/mapcate/submitAdd.json',
                    data: {oriCate: oricate, destCate: destcate, active: active, newprop: check, newPropName: newname},
                    type: 'post',
                    contentType: 'json',
                    done: function (resp) {
                        if (resp.success) {
                            popup.msg(resp.message, function () {
                                popup.close('popup-add-mapcate');
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
                popup.close('popup-add-mapcate');
            }
        }
    ]);
    var params = {};
    params.categoryId = '';
    params.parentCategorys = [];
    params.parentCategorys.cats = [];
    params.parentCategorys.ancestors = [];

    model.builtCategory(params, 'model-add-form');
};

mapcate.changeActive = function (id) {
    ajax({
        service: '/cpservice/mapcate/changeActive.json',
        data: {id: id},
        type: 'post',
        contentType: 'json',
        done: function (resp) {
            if (resp.success) {
                $('#' + id + 'active').attr('checked', resp.data.active);
                popup.msg(resp.message, function () {
                    localtion.reload();
                });
            } else {
                popup.msg(resp.message);
            }
        }
    });
};