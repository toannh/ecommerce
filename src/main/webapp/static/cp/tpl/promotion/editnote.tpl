<form class="form-horizontal" id="add-notes" >
    <input type="hidden" name="promotionId" value="<%=data.promotionId%>" />
    <div class="form-group">
        <label class="control-label col-sm-2">Ghi chú</label>
        <div class="col-sm-10">
            <textarea name="note" class="form-control" rows="5"><%=data.note%></textarea>
        </div>
    </div>
</form>