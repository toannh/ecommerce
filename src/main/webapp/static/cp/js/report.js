var report = {};
report.init = function () {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo"]
    ]);
    $("[data-rel=startTime]").timeSelect();
    $("[data-rel=endTime]").timeSelect();
    //create object draw chart
    google.load("visualization", "1", {packages: ["corechart"]});
    google.setOnLoadCallback(report.draw);
    $(window).bind('hashchange', function () {
        report.draw();
    });
};
report.draw = function () {
    var params = $(location).attr("hash").replace('#', '').split('/');
    if (params.length <= 1 && params[0] === '') {
        params = ["user"];
    }
    //select nav
    $("ul[data-rel=nav] li").removeClass("active")
    $("ul[data-rel=nav] li[data-hash=" + params[0] + "]").addClass("active");
    var action = report[params[0]];
    action(params);
};
report.search = function () {
    var search = new Object();
    search.startTime = $("input[data-rel=startTime]").val();
    search.endTime = $("input[data-rel=endTime]").val();
    return search;
};
report.user = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo người dùng"]
    ]);
    var search = report.search();
    ajax({
        service: '/cpservice/report/user.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            // Set chart options
            var options = {
                'title': 'Thống kê user theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);


            var table = '<div class="report-content row">\
                            <div class="col-md-1"></div>\
                            <div class="col-md-5">\
                            	<h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>';
            $.each(resp.data.dataRow, function () {
                table += '<p>' + this + '</p>';
            });
            table += '<button onclick="changeTable();" type="button" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>Xem chi tiết</button>';
            table += '</div>';
            table += '<div class="col-md-5">\
                            	<h6><b>Tích lũy tới thời điểm hiện tại</b></h6>';
            $.each(resp.data.dataRowTotal, function () {
                table += '<p>' + this + '</p>';
            });
            table += '</div>';
            table += '</div>';
            table += '<table id="table-details" style="display:none;" class="table table-striped table-bordered table-responsive" style="margin-left:10px;width:1135px")>';
            $.each(resp.data.chart, function () {
                table += "<tr><td>" + this[0] + "</td><td>" + this[1] + "</td><td>" + this[2] + "</td><td>" + this[3] + "</td><td>" + this[4] + "</td></tr>";
            });
            table += "</table>";
            $("div[data-rel=data]").html(table);
        }
    });
};
function changeTable() {
    if ($("#table-details").is(":visible")) {
        $("#table-details").hide("fast");
    } else {
        $("#table-details").show("fast");
    }
}

report.buyer = function (params) {
    //vẽ breadcrumd
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo người mua"]
    ]);
    var search = report.search();
    ajax({
        service: '/cpservice/report/buyer.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            //set chart options
            var options = {
                'title': 'Thống kê người mua theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);
            var table = '<div class="report-content row">\
                            <div class="col-md-1"></div>\
                            <div class="col-md-5">\
                            	<h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>';
            $.each(resp.data.dataRow, function () {
                table += '<p>' + this + '</p>';
            });
            table += '<button onclick="changeTable();" type="button" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>Xem chi tiết</button>';
            table += '</div>';
            table += '<div class="col-md-5">\
                            	<h6><b>Tích lũy tới thời điểm hiện tại</b></h6>';
            $.each(resp.data.dataRowTotal, function () {
                table += '<p>' + this + '</p>';
            });
            table += '</div>';
            table += '<table id="table-details" style="display:none;" class="table table-striped table-bordered table-responsive" style="margin-left:10px;width:1135px")>';

            $.each(resp.data.chart, function () {
                table += "<tr><td>" + this[0] + "</td><td>" + this[1] + "</td><td>" + this[2] + "</td><td>" + this[3] + "</td><td>" + this[4] + "</td></tr>";
            });
            table += "</table>";
            $("div[data-rel=data]").html(table);
        }
    });
}
function downloadNewSeller() {
    var search = report.search();
    ajax({
        service: '/cpservice/report/downloadnewSeller.json',
        data: {st: search.startTime, et: search.endTime},
        done: function (resp) {
            alert("download danh sách người bán mới thành công!");
        }
    });
}
report.seller = function (params) {
    //vẽ breadcrumd
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo người bán"]
    ]);
    var search = report.search();
    ajax({
        service: '/cpservice/report/seller.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            //set chart options
            var options = {
                'title': 'Thống kê người bán theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);
            //<a onclick="downloadNewSeller();"href="#" class="btn btn-sm btn-default"><span class="glyphicon glyphicon-floppy-save"></span> Danh sách người bán mới</a>\
            var table = '<div class="report-content row">\
                            <div class="col-md-1"></div>\
                            <div class="col-md-5">\\n\
                            	<h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>';
            $.each(resp.data.dataRow, function () {
                table += '<p>' + this + '</p>';
            });
            table += '<button onclick="changeTable();" type="button" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>Xem chi tiết</button>';
            table += '</div>';
            table += '<div class="col-md-5">\
                            	<h6><b>Tích lũy tới thời điểm hiện tại</b></h6>';
            $.each(resp.data.dataRowTotal, function () {
                table += '<p>' + this + '</p>';
            });
            table += '</div>';
            table += '<table id="table-details" style="display:none;" class="table table-striped table-bordered table-responsive" style="margin-left:10px;width:1135px")>';

            $.each(resp.data.chart, function () {
                table += "<tr><td>" + this[0] + "</td><td>" + this[1] + "</td><td>" + this[2] + "</td><td>" + this[3] + "</td><td>" + this[4] + "</td></tr>";
            });
            table += "</table>";
            $("div[data-rel=data]").html(table);
        }
    });
}
report.shop = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo shop"]
    ]);
    //find data
    var search = report.search();
    ajax({
        service: '/cpservice/report/shop.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            // Set chart options
            var options = {
                'title': 'Thống kê shop theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);
            table = '<div class="report-content row">\
                            <div class="col-md-1"></div>\
                            <div class="col-md-5">\
                            	<h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>';
            $.each(resp.data.dataRow, function () {
                table += '<p>' + this + '</p>';
            });
            table += '<button onclick="changeTable();" type="button" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>Xem chi tiết</button>';
            table += '</div>';
            table += '<div class="col-md-5">\
                            	<h6><b>Tích lũy tới thời điểm hiện tại</b></h6>';
            $.each(resp.data.dataRowTotal, function () {
                table += '<p>' + this + '</p>';
            });
            table += '</div>';
            table += '<table id="table-details" style="display:none;" class="table table-striped table-bordered table-responsive" style="margin-top: 10px">';
            var shopLockedYesterday;
            $.each(resp.data.chart, function () {
//                if (typeof this[3] === "number" && this[3] !== NaN) {
//                    if ((this[3] - shopLockedYesterday) >= 0) {
//                        table += "<tr><td>" + this[0] + "</td><td>" + this[1] + "</td><td>" + this[2] + "</td><td>" + (this[3] - shopLockedYesterday) + "</td></tr>";
//                    } else {
//                        table += "<tr><td>" + this[0] + "</td><td>" + this[1] + "</td><td>" + this[2] + "</td><td>" + (shopLockedYesterday - this[3]) + " (mở khóa) " + "</td></tr>";
//                    }
//                    shopLockedYesterday = this[3];
//                } else if (this[3] === NaN) {
//                    table += "<tr><td>" + this[0] + "</td><td>" + this[1] + "</td><td>" + this[2] + "</td><td>" + 0 + "</td></tr>";
//                    shopLockedYesterday = 0;
//                } else {
//                    table += "<tr><td>" + this[0] + "</td><td>" + this[1] + "</td><td>" + this[2] + "</td><td>" + this[3] + "</td></tr>";
//                    shopLockedYesterday = 0;
//                }
                table += "<tr><td>" + this[0] + "</td><td>" + this[1] + "</td><td>" + this[2] + "</td></tr>";
            });
            table += "</table>";
            $("div[data-rel=data]").html(table);
        }
    });
};
report.order = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo số lượng đơn hàng"]
    ]);
    //find data
    var search = report.search();
    ajax({
        service: '/cpservice/report/order.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            // Set chart options
            var options = {
                'title': 'Thống kê đơn hàng theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);
            var dataFinal = resp.data.dataFinal;
            var dataTotal = resp.data.dataTotal;
            var timeInMs = Date.now();
            var rowHTML = '<div class="col-md-1"></div>' +
                    '<div class="col-md-5">' +
                    '<h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>' +
                    '<p>Hóa đơn đặt hàng: ' + (dataTotal.quantity) + '</p>' +
                    '<p>Hóa đơn đặt hàng NgânLượng: ' + dataTotal.nlPayment + '</p>' +
                    '<p>Hóa đơn đặt hàng CoD: ' + dataTotal.codPayment + '</p>' +
                    '<p>Hóa đơn đặt hàng tự liên hệ: ' + dataTotal.nonePayment + '</p>' +
                    '<p>Hóa đơn thanh toán thành công: ' + dataTotal.paidStatus + '</p>' +
                    '<p>Hóa đơn thanh toán thành công qua NL: ' + dataTotal.nlPaymentPaid + '</p>' +
                    '<p>Hóa đơn thanh toán thành công qua Cod: ' + dataTotal.codPaymentPaid + '</p>' +
                    '<p>Hóa đơn refund: ' + dataTotal.orderReturn + '</p>' +
                    '</div><!-- /col -->' +
                    '<div class="col-md-1"></div>' +
                    '<div class="col-md-5">' +
                    '<h6><b>Tích lũy tới thời điểm ' + textUtils.formatTime(timeInMs, 'hour') + '</b></h6>' +
                    '<p>Tổng đơn hàng: ' + (dataFinal.quantity) + '</p>' +
                    '<p>Hóa đơn đặt hàng NgânLượng: ' + dataFinal.nlPayment + '</p>' +
                    '<p>Hóa đơn đặt hàng CoD: ' + dataFinal.codPayment + '</p>' +
                    '<p>Hóa đơn đặt hàng tự liên hệ: ' + dataFinal.nonePayment + '</p>' +
                    '<p>Hóa đơn thanh toán thành công: ' + dataFinal.paidStatus + '</p>' +
                    '<p>Hóa đơn thanh toán thành công qua NL: ' + dataFinal.nlPaymentPaid + '</p>' +
                    '<p>Hóa đơn thanh toán thành công qua Cod: ' + dataFinal.codPaymentPaid + '</p>' +
                    '<p>Hóa đơn refund: ' + dataFinal.orderReturn + '</p>' +
                    '</div><!-- /col -->';

            $("div[data-rel=data]").html(rowHTML);
        }
    });
};

report.item = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo sản phẩm"]
    ]);

    //find data
    var search = report.search();
    ajax({
        service: '/cpservice/report/item.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            // Set chart options
            var options = {
                'title': 'Thống kê sản phẩm theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);

            var html = '<div class="col-md-1"></div>\
                        <div class="col-md-5">\
                            <h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>';
            $.each(resp.data.dataSearch, function () {
                html += '<p>' + this + '</p>';
            });
            html += '</div>\
                        <div class="col-md-1"></div>\
                        <div class="col-md-5">\
                            <h6><b>Tích lũy tới thời điểm ' + textUtils.formatTime(resp.data.timeNow, 'hour') + '</b></h6>';
            $.each(resp.data.dataNow, function () {
                html += '<p>' + this + '</p>';
            });
            html += '</div>';
            $("div[data-rel=data]").html(html);
        }
    });
};

report.lading = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo vận đơn"]
    ]);

    //find data
    var search = report.search();
    ajax({
        service: '/cpservice/report/lading.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            // Set chart options
            var options = {
                'title': 'Thống kê vận đơn theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);

            var html = '<div class="col-md-1"></div>\
                        <div class="col-md-5">\
                            <h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>';
            $.each(resp.data.dataSearch, function () {
                html += '<p>' + this + '</p>';
            });
            html += '</div>\
                        <div class="col-md-1"></div>\
                        <div class="col-md-5">\
                            <h6><b>Tích lũy tới thời điểm ' + textUtils.formatTime(Date.now(), 'hour') + '</b></h6>';
            $.each(resp.data.dataNow, function () {
                html += '<p>' + this + '</p>';
            });
            html += '</div>';
            $("div[data-rel=data]").html(html);
        }
    });

};
report.gmv = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo GMV"]
    ]);
    //find data
    var search = report.search();
    ajax({
        service: '/cpservice/report/gmv.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            // Set chart options
            var options = {
                'title': 'Thống kê GMV theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);
            var dataGMV = resp.data.dataGMV;
            var dataGMVTime = resp.data.dataGMVTime;
            var timeInMs = Date.now();
            var rowHTML = '<div class="col-md-6">' +
                    '<h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>' +
                    '<div class="table-responsive">' +
                    '<table class="table">' +
                    '<tbody>' +
                    '<tr>' +
                    '<td>GMV đặt hàng:</td>' +
                    '<td>' + dataGMVTime.finalPrice.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMVTime.quantity + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV đặt hàng NL:</td>' +
                    '<td>' + dataGMVTime.finalPriceNlPayment.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMVTime.nlPayment + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV đặt hàng CoD:</td>' +
                    '<td>' + dataGMVTime.finalPriceCodPayment.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMVTime.codPayment + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV thanh toán thành công:</td>' +
                    '<td>' + dataGMVTime.finalPricePaidStatus.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMVTime.paidStatus + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV thanh toán thành công qua NL:</td>' +
                    '<td>' + dataGMVTime.finalPricePaidStatusNL.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMVTime.nlPaymentPaid + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV thanh toán thành công qua CoD:</td>' +
                    '<td>' + dataGMVTime.finalPricePaidStatusCOD.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMVTime.codPaymentPaid + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV hàng bị trả lại:</td>' +
                    '<td>' + dataGMVTime.finalPriceReturn.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMVTime.orderReturn + ' orders)</td>' +
                    '</tr>' +
                    '</tbody>' +
                    '</table>' +
                    '</div><!-- /table-responsive -->' +
                    '</div><!-- /col -->' +
                    '<div class="col-md-6">' +
                    '<h6><b>Tích lũy tới thời điểm ' + textUtils.formatTime(Date.now(), 'hour') + '</b></h6>' +
                    '<div class="table-responsive">' +
                    '<table class="table">' +
                    '<tbody>' +
                    '<tr>' +
                    '<td>GMV đặt hàng:</td>' +
                    '<td>' + dataGMV.finalPrice.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMV.quantity + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV đặt hàng NL:</td>' +
                    '<td>' + dataGMV.finalPriceNlPayment.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMV.nlPayment + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV đặt hàng CoD:</td>' +
                    '<td>' + dataGMV.finalPriceCodPayment.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMV.codPayment + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV thanh toán thành công:</td>' +
                    '<td>' + dataGMV.finalPricePaidStatus.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMV.paidStatus + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV thanh toán thành công qua NL:</td>' +
                    '<td>' + dataGMV.finalPricePaidStatusNL.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMV.nlPaymentPaid + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV thanh toán thành công qua CoD:</td>' +
                    '<td>' + dataGMV.finalPricePaidStatusCOD.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMV.codPaymentPaid + ' orders)</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<td>GMV hàng bị trả lại:</td>' +
                    '<td>' + dataGMV.finalPriceReturn.toMoney(0, ',', '.') + ' <sup class="u-price">đ</sup> (' + dataGMV.orderReturn + ' orders)</td>' +
                    '</tr>' +
                    '</tbody>' +
                    '</table>' +
                    '</div><!-- /table-responsive -->' +
                    '</div>	<!-- /col -->';

            $("div[data-rel=data]").html(rowHTML);
        }
    });

};

report.cash = function (params) {
    //vẽ breadcrumb
    layout.breadcrumb([
        ["Trang chủ", "/cp/index.html"],
        ["Báo cáo", "/cp/report.html"],
        ["Báo cáo xèng"]
    ]);

    //find data
    var search = report.search();
    ajax({
        service: '/cpservice/report/cash.json',
        data: search,
        contentType: 'json',
        type: 'post',
        done: function (resp) {
            var data = google.visualization.arrayToDataTable(resp.data.chart);
            // Set chart options
            var options = {
                'title': 'Thống kê xèng theo ngày',
                'width': 1124,
                'height': 300
            };
            var chart = new google.visualization.AreaChart(document.getElementById('chart'));
            chart.draw(data, options);

            var html = '<div class="col-md-1"></div>\
                        <div class="col-md-5">\
                            <h6><b>Từ ' + textUtils.formatTime(search.startTime, 'hour') + ' đến ' + textUtils.formatTime(search.endTime, 'hour') + '</b></h6>';
            $.each(resp.data.dataSearch, function () {
                html += '<p>' + this + '</p>';
            });
            html += '</div>\
                        <div class="col-md-1"></div>\
                        <div class="col-md-5">\
                            <h6><b>Tích lũy tới thời điểm ' + textUtils.formatTime(Date.now(), 'hour') + '</b></h6>';
            $.each(resp.data.dataNow, function () {
                html += '<p>' + this + '</p>';
            });
            html += '</div>';
            $("div[data-rel=data]").html(html);
        }
    });

};

//report.showTable =  function(){
//    document.getElementById("table-details")
//};
//var formatter = new google.visualization.NumberFormat({
//    prefix: '',
//    pattern: '#,###,###.##'
//});