<form class="form-horizontal" id="add-note" >
    <table class="table table-striped table-bordered table-responsive" style="margin-top: 10px">
        <tr class="text-center">
            <td><label class="control-label"><b>Tiêu đề tin nhắn</b></label></td>
            <td><%= send.name %></td>
        </tr>
        <tr class="text-center">
            <td><label class="control-label"><b>Trạng thái chạy</b></label></td>
            <td><%= (typeof send.run != 'undefined' && send.run == 'true' ? 'Đang chạy' : 'Đang xử lý') %></td>
        </tr>
        <tr class="text-center">
            <td><label class="control-label"><b>Trạng thái hoàn thành</b></label></td>
            <td><%= (typeof send.done != 'undefined' && send.done == 'true' ? 'Chạy hoàn thành' : 'Đang xử lý') %></td>
        </tr>
    </table>
    <table class="table table-striped table-bordered table-responsive">
        <%= send.phone %>
    </table>
</form>