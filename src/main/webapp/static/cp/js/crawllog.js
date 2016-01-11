var crawllog = {};
crawllog.init = function () {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Lịch sử crawl sản phẩm", "/cp/itemcrawllog.html"],
        ["Danh sách sản phẩm crawl"]
    ]);
    $('.timeselect').timeSelect();
};
crawllog.resertForm = function () {
    $('#crawlLogSearch')[0].reset();
};
crawllog.showRequest = function (id) {
    var html = $('#request-' + id).html();
    popup.open('pop-request', 'Chi tiết Request',html, [
        {
            title: 'Đóng',
            style: 'btn-default',
            fn: function () {
                popup.close('pop-request');
            }
        }
    ]);
};