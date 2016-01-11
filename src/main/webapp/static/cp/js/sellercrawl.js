var sellercrawl = {};
sellercrawl.init = function () {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Lịch sử crawl sản phẩm", "/cp/itemsellercrawl.html"],
        ["Danh sách sản phẩm crawl"]
    ]);
    $('.timeselect').timeSelect();
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
};
sellercrawl.resertForm = function () {
    $('#sellerCrawlLogSearch')[0].reset();
};