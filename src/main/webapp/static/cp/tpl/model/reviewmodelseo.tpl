<form class="form-horizontal" id="review-model" >
    <input type="hidden" name="modelId" value="<%= (typeof data!=='undefined')? data.modelId: ''%>">
    <input type="hidden" name="title" value="<%= (typeof data!=='undefined')? data.modelId: ''%>">
    <input type="hidden" name="description" value="<%= (typeof data!=='undefined')? data.modelId: ''%>">
    <div class="form-group">
        <label class="control-label col-sm-4">Trạng thái</label>
        <div class="col-sm-8" id="noReview">
            <label class="control-label col-sm-3"><input name="active" type="radio" <%=(typeof data!=='undefined' && data.active==true)? 'checked': ''%> value="true" />Duyệt</label>
            <label class="control-label col-sm-6" style="margin-bottom: 10px"><input name="active"  <%=(typeof data!=='undefined' && data.active==false)? 'checked': ''%> type="radio" value="false"/>Yêu cầu sửa lại</label>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Ghi chú</label>
        <div class="col-sm-8">
            <textarea name="note" class="form-control" placeholder="Nhập ghi chú duyệt" ><%= (typeof data!=='undefined')? data.note: ''%></textarea>
        </div>

    </div>

</form>