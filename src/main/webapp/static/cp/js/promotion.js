promotion = {};
promotion.init = function () {
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị khuyến mãi", "/cp/promotion.html"],
        ["Danh sách khuyến mãi"]
    ]);
    $('.timeselect').timeSelect();
    promotion.initListCare(promotionIds);
   
};
promotion.addNote = function (id) {

    popup.open('popup-add', 'Ghi chú chăm sóc', template('/cp/tpl/promotion/note.tpl', {id: id}), [
        {
            title: 'Lưu lại',
            style: 'btn-primary',
            fn: function () {
                ajaxSubmit({
                    service: '/cpservice/promotion/addcare.json',
                    id: 'add-note',
                    contentType: 'json',
                    done: function (rs) {
                        if (rs.success) {
                            popup.msg("Cập nhật ghi chú thành công", function () {
                                popup.close('popup-add');
                                var careHTML = '<button type="submit" class="btn btn-success" onclick="promotion.editNote(' + id + ');"><i class="glyphicon glyphicon-ok"></i> Đã nhận CS</button>\n\
                                                <button type="submit" class="btn btn-danger" onclick="promotion.viewNote(' + id + ');"><i class="glyphicon glyphicon-tasks"></i> <span class="viewNote' + id + '">Xem</span></button>';
                                $('.care' + id).html(careHTML);
                                promotion.initListCareCount(promotionIds);
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
};
promotion.initListCare = function (promotionIds) {
    ajaxSubmit({
        service: '/cpservice/promotion/getlistcare.json',
        data: promotionIds,
        contentType: 'json',
        loading: false,
        done: function (rs) {
            if (rs.success) {
                setTimeout(function () {
                    $.each(rs.data, function (j, i) {
                        if (rs.data[j]) {
                            var careHTML = '<button type="submit" class="btn btn-success" onclick="promotion.editNote(' + j + ');"><i class="glyphicon glyphicon-ok"></i> Đã nhận CS</button>\n\
                                                <button type="submit" class="btn btn-danger" onclick="promotion.viewNote(' + j + ');"><i class="glyphicon glyphicon-tasks"></i> <span class="viewNote' + j + '">Xem</span></button>';
                            $('.care' + j).html(careHTML);
                        }
                    });
                    promotion.initListCareCount(promotionIds);
                }, 2000);

            } else {
                popup.msg(rs.message);
            }

        }
    });
};
promotion.editNote = function (id) {
    ajax({
        service: '/cpservice/promotion/getcare.json',
        data: {id: id},
        done: function (resp) {
            popup.open('popup-add', 'Ghi chú chăm sóc', template('/cp/tpl/promotion/editnote.tpl', resp), [
                {
                    title: 'Lưu lại',
                    style: 'btn-primary',
                    fn: function () {
                        ajaxSubmit({
                            service: '/cpservice/promotion/editcare.json',
                            id: 'add-notes',
                            contentType: 'json',
                            done: function (rs) {
                                if (rs.success) {
                                    popup.msg("Cập nhật ghi chú thành công", function () {
                                        popup.close('popup-add');
                                        var careHTML = '<button type="submit" class="btn btn-success" onclick="promotion.editNote(' + id + ');"><i class="glyphicon glyphicon-ok"></i> Đã nhận CS</button>\n\
                                                <button type="submit" class="btn btn-danger" onclick="promotion.viewNote(' + id + ');"><i class="glyphicon glyphicon-tasks"></i> <span class="viewNote' + id + '">Xem</span></button>';
                                        $('.care' + id).html(careHTML);
                                        promotion.initListCareCount(promotionIds);
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
        }
    });
};
promotion.viewNote = function (id) {
    ajax({
        service: '/cpservice/promotion/getlistcarebypromotionid.json',
        data: {id: id},
        done: function (resp) {
            popup.open('popup-adds', 'Danh sách người chăm sóc', template('/cp/tpl/promotion/viewnote.tpl', resp), [
                {
                    title: 'Đóng',
                    style: 'btn-default',
                    fn: function () {
                        popup.close('popup-adds');
                    }
                }
            ]);
        }
    });
};
promotion.initListCareCount = function (promotionIds) {
    ajaxSubmit({
        service: '/cpservice/promotion/getcountlistcare.json',
        data: promotionIds,
        contentType: 'json',
        loading: false,
        done: function (rs) {
            if (rs.success) {
                $.each(rs.data, function (j, i) {
                    var careHTML = 'Xem(' + rs.data[j] + ')';
                    $('.viewNote' + j).html(careHTML);
                    
                });
            } else {
                popup.msg(rs.message);
            }

        }
    });
};