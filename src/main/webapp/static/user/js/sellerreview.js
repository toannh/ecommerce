sellerreview = {};
sellerreview.init = function () {
    var userId = $('input[name=userIdReview]').val();
    if (typeof (userId) !== 'undefined' && userId !== null && userId !== '') {
        sellerreview.changeTabReview(1, userId);
        sellerreview.loadOrder(0);
    }
};

sellerreview.changeTabReview = function (fag, userId) {
    if (fag === 1) {
        $('.reviewTrue').addClass('active');
        $('.reviewFalse').removeClass('active');
        $('.reviewAll').removeClass('active');
        $('input[name=sellerId]').val(userId);
        $('input[name=userIdReview]').val('');
        sellerreview.loadOrder(0);
    }
    if (fag === 2) {
        $('.reviewTrue').removeClass('active');
        $('.reviewFalse').addClass('active');
        $('.reviewAll').removeClass('active');
        $('input[name=userIdReview]').val(userId);
        $('input[name=sellerId]').val('');
        sellerreview.loadOrder(0);
    }
    if (fag === 3) {
        $('.reviewFalse').removeClass('active');
        $('.reviewTrue').removeClass('active');
        $('.reviewAll').addClass('active');
        $('input[name=sellerId]').val(userId);
        $('input[name=userIdReview]').val(userId);
        sellerreview.loadOrder(0);
    }
};

sellerreview.loadOrder = function (page) {
    var reviewSearch = {};
    reviewSearch.pageIndex = page;
    reviewSearch.pageSize = 10;
    if ($('input[name=sellerId]').val() === '') {
        reviewSearch.userId = $('input[name=userIdReview]').val();
    } else if ($('input[name=sellerId]').val() !== '' && $('input[name=userIdReview]').val() !== '') {
        reviewSearch.sellerId = $('input[name=sellerId]').val();
        reviewSearch.userId = $('input[name=userIdReview]').val();
    } else {
        reviewSearch.sellerId = $('input[name=sellerId]').val();
    }
    ajax({
        service: '/sellerreview/sellerreviewsearch.json',
        data: reviewSearch,
        loading: false,
        type: 'get',
        done: function (resp) {
            if (resp.success) {
                var data = resp.data['reviewPageOrder'];
                var items = resp.data['itemOrder'];
                var users = resp.data['userOrder'];
                var point = resp.data;
                if (data.data.length > 0) {
                    var htmlReview = '';
                    htmlReview += '<div class="box-control">' +
                            '<label>Hiện <b>' + eval(data.pageIndex + 1) + '-' + data.pageCount + '</b> trong <b>' + data.dataCount + '</b> đánh giá</label>' +
                            '<ul class="pagination pull-right" id="pagination">' +
                            '</ul>' +
                            '</div><!-- box-control -->' +
                            '<div class="pu-review-title">' +
                            '<div class="row">' +
                            '<div class="col-sm-4">Nội dung đánh giá/SP</div>' +
                            '<div class="col-sm-2" style="text-align: center">Người đánh giá</div>' +
                            '<div class="col-sm-2" style="text-align: center">Giá sản phẩm</div>' +
                            '<div class="col-sm-2" style="text-align: center">Thời gian đánh giá</div>' +
                            '<div class="col-sm-2" style="text-align: center"></div>' +
                            '</div><!-- row -->' +
                            '</div><!-- pu-review-title -->';
                    $('#showitemOther').html(htmlReview);
                    var contentTable = "";
                    $.each(data.data, function (i) {
                        contentTable = template('/user/tpl/listitemreview.tpl', {data: data.data[i], items: items, point: point, users: users});
                        $('#showitemOther').append(contentTable);
                    });
                    var dataPage = resp.data['reviewPageOrder'];
                    $("#pagination").html("");
                    if (dataPage.pageCount > 1) {
                        var display = 3;
                        var begin = 0;
                        var end = 0;
                        if (dataPage.pageIndex != 0) {
                            $("#pagination").append('<li><a href="javascript:;" onclick="sellerreview.loadOrder(1)">«</a></li>');
                            begin = dataPage.pageIndex;
                            end = begin + 2;
                        } else {
                            begin = 1;
                            if ((begin + 2) > dataPage.pageCount)
                                end = begin + 1;
                            else
                                end = begin + 2;
                        }
                        if (dataPage.pageIndex + 1 == dataPage.pageCount) {
                            if (dataPage.pageIndex == 1) {
                                begin = dataPage.pageCount - display + 2;
                            }
                            if (dataPage.pageIndex != 1)
                                begin = dataPage.pageCount - display + 1;
                            end = dataPage.pageCount;
                        }
                        for (var j = begin; j <= end; j++) {
                            var active = (dataPage.pageIndex + 1) == j ? 'active' : '';
                            var link = '<li class="' + active + '"><a href="javascript:;" onclick="sellerreview.loadOrder(' + j + ')">' + j + '</a></li>';
                            $("#pagination").append(link);
                        }
                        if (dataPage.pageIndex + 1 != end) {
                            $("#pagination").append('<li><a href="javascript:;" onclick="sellerreview.loadOrder(' + dataPage.pageCount + ')">»</a></li>');
                        }
                    }
                } else {
                    $('#showitemOther').html('<div class="cdt-message bg-danger text-center">Không tìm thấy đánh giá!</div>');
                    $('.others').html('');
                }
            }
        }
    });
};

//sellerreview.loadOther = function (page) {
//    var reviewSearch = {};
//    reviewSearch.pageIndex = page;
//    reviewSearch.pageSize = 10;
//    if ($('input[name=sellerId]').val() === '') {
//        reviewSearch.userId = $('input[name=userIdReview]').val();
//    } else {
//        reviewSearch.sellerId = $('input[name=sellerId]').val();
//    }
//    ajax({
//        service: '/itemreview/itemreviewsearchs.json',
//        data: reviewSearch,
//        loading: false,
//        type: 'get',
//        done: function (resp) {
//            if (resp.success) {
//                var data = resp.data['reviewPage'];
//                var items = resp.data['items'];
//                if (data.data.length > 0) {
//                    var htmlReview = '';
//                    htmlReview += '<div class="box-control">' +
//                            '<label>Hiện <b>' + eval(data.pageIndex + 1) + '-' + data.pageCount + '</b> trong <b>' + data.dataCount + '</b> đánh giá</label>' +
//                            '<ul class="pagination pull-right" id="pagination">' +
//                            '</ul>' +
//                            '</div><!-- box-control -->' +
//                            '<div class="pu-review-title">' +
//                            '<div class="row">' +
//                            '<div class="col-sm-6">Nội dung đánh giá/SP</div>' +
//                            '<div class="col-sm-3" style="text-align: center">Người đánh giá/Giá sản phẩm</div>' +
//                            '<div class="col-sm-3" style="text-align: center">Thời gian đánh giá</div>' +
//                            '</div><!-- row -->' +
//                            '</div><!-- pu-review-title -->';
//                    $('#showitemOther').html(htmlReview);
//                    for (var i = 0; i < data.data.length; i++) {
//                        $('#showitemOther').append(template('/user/tpl/listitemreview.tpl', {data: data.data[i], items: items}));
//                    }
//                    var dataPage = resp.data['reviewPage'];
//
//                    // Phân trang sản phẩm
//                    $("#pagination").html("");
//                    if (dataPage.pageCount > 1) {
//                        var display = 3;
//                        var begin = 0;
//                        var end = 0;
//                        // alert(dataPage.pageIndex);
//                        if (dataPage.pageIndex != 0) {
//                            $("#pagination").append('<li><a href="javascript:;" onclick="sellerreview.loadOther(1)">«</a></li>');
//                            begin = dataPage.pageIndex;
//                            end = begin + 2;
//                        } else {
//                            begin = 1;
//                            if ((begin + 2) > dataPage.pageCount)
//                                end = begin + 1;
//                            else
//                                end = begin + 2;
//                        }
//                        if (dataPage.pageIndex + 1 == dataPage.pageCount) {
//                            if (dataPage.pageIndex == 1) {
//                                begin = dataPage.pageCount - display + 2;
//                            }
//                            if (dataPage.pageIndex != 1)
//                                begin = dataPage.pageCount - display + 1;
//                            end = dataPage.pageCount;
//                        }
//                        for (var j = begin; j <= end; j++) {
//                            var active = (dataPage.pageIndex + 1) == j ? 'active' : '';
//                            var link = '<li class="' + active + '"><a href="javascript:;" onclick="sellerreview.loadOther(' + j + ')">' + j + '</a></li>';
//                            $("#pagination").append(link);
//                        }
//                        if (dataPage.pageIndex + 1 != end) {
//                            $("#pagination").append('<li><a href="javascript:;" onclick="sellerreview.loadOther(' + dataPage.pageCount + ')">»</a></li>');
//                        }
//                    }
//                } else {
//                    $('#showitemOther').html('<div class="cdt-message bg-danger text-center">Không tìm thấy đánh giá!</div>');
//                    $('.others').html('');
//                }
//            }
//        }
//    });
//};