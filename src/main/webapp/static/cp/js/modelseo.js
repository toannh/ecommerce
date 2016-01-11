var modelseo = {};
modelseo.init = function () {

    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Quản trị model", "/cp/modelseo.html"],
        ["Danh sách model seo"]
    ]);
    $('.timestamp').timeSelect();
};
modelseo.editSeo = function (id) {

    ajax({
        service: '/cpservice/global/model/getmodel.json',
        data: {id: id},
        done: function (resp) {
            if (resp.success) {
                ajax({
                    service: '/cpservice/model/getcatepropertyvalues.json',
                    data: {id: resp.data.categoryId},
                    done: function (global) {
                        if (global.success) {
                            resp.data.catePropertyValues = global.data.propertyValuesWithCategory;
                            resp.data.cateProperties = global.data.cateProperties;
                            var contentProperties = "";
                            if (resp.data.properties !== null && resp.data.properties !== '') {
                                if (typeof resp.data.modelSeo !== 'undefined' && resp.data.modelSeo !== null && resp.data.modelSeo.contentProperties != null) {
                                    contentProperties = resp.data.modelSeo.contentProperties;
                                } else {
                                    contentProperties = template('/cp/tpl/model/content.tpl', resp);
                                }
                                resp.data.contentProperties = contentProperties;
                            }
                            popup.open('popup-category-edit', 'Cập nhật SEO Model', template('/cp/tpl/model/editseo.tpl', resp), [
                                {
                                    title: 'Lưu lại',
                                    style: 'btn-primary',
                                    fn: function () {
                                        ajaxSubmit({
                                            service: '/cpservice/model/updatereviewseo.json',
                                            id: 'model-form-edit-seo',
                                            contentType: 'json',
                                            done: function (reg) {
                                                if (reg.success) {
                                                    popup.msg(reg.message, function () {
                                                        popup.close('popup-category-edit');
                                                    });
                                                } else {
                                                    popup.msg(reg.message);
                                                }
                                            }
                                        });
                                    }
                                },
                                {
                                    title: 'Hủy',
                                    style: 'btn-default',
                                    fn: function () {
                                        popup.close('popup-category-edit');
                                    }
                                }
                            ], 'modal-lg', true);
                            editor('txt_content');
                            editor('txt_contents');
                        }
                    }});
            } else {
                popup.msg(resp.message);
            }

        }
    });
};

modelseo.reviewModelSeo = function (id) {
    ajax({
        service: '/cpservice/model/getmodelseo.json',
        data: {id: id},
        done: function (global) {
            if (global.success) {
                popup.open('review-model', 'Duyệt Model', template('/cp/tpl/model/reviewmodelseo.tpl', global), [
                    {
                        title: 'Lưu lại',
                        style: 'btn-primary',
                        fn: function () {
                            popup.confirm('Bạn đã chắc chắn thao tác duyệt xong Model Seo?', function () {
                                ajaxSubmit({
                                    service: '/cpservice/model/reviewmodelseo.json',
                                    id: 'review-model',
                                    contentType: 'json',
                                    done: function (resp) {
                                        if (resp.success) {
                                            popup.msg(resp.message,function(){
                                                popup.close('review-model');
                                                location.reload();
                                            });
                                             
                                        } else {
                                            popup.msg(resp.message);
                                        }
                                    }
                                });
                            });
                        }
                    },
                    {
                        title: 'Hủy',
                        style: 'btn-default',
                        fn: function () {
                            popup.close('review-model');
                        }
                    }
                ]);
            }
        }
    });




};