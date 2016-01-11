<form class="form-horizontal" id="add-note" >
    <table class="table table-striped table-bordered table-responsive" style="margin-top: 10px">
        <tr class="text-center">
            <td><label class="control-label"><b>Mã thẻ</b></label></td>
            <td>
                <label class="control-label"><%= (typeof send !== 'undefined' ? send.cardCode : '')%></label>
            </td>
        </tr>
        <tr class="text-center">
            <td><label class="control-label"><b>Số Serial</b></label></td>
            <td>
                <label class="control-label"><%= (typeof send !== 'undefined' ?  send.cardSeri : '' ) %></label>
            </td>
        </tr>
        <tr class="text-center">
            <td><label class="control-label"><b>Loại thẻ</b></label></td>
            <td>
                <label class="control-label"><%= (typeof send !== 'undefined' ? send.cardType : '')  %></label>
            </td>
        </tr>
        <tr class="text-center">
            <td><label class="control-label"><b>Mệnh giá</b></label></td>
            <td>
                <script>
                    var num = <%=(typeof send !== 'undefined' ? send.cardValue : '')%> ;
                            var changenum = num.toLocaleString();
                    $('#showVal').text(changenum + " VNĐ");
                </script>
                <label class="control-label"><p id='showVal'></p></label>
            </td>
        </tr>
        <tr class="text-center">
            <td><label class="control-label"><b>Ngày hết hạn</b></label></td>
            <td>
                <label class="control-label"><%= send.expiryDate %></label>
            </td>
        </tr>
    </table>
</form>