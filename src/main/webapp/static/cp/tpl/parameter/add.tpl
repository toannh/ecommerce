<form class="form-horizontal" id="parameter-add-form">
    <input type="hidden" name="keyConvention" id="keyConvention" value="<%= (typeof(data) !== 'undefined')? data.keyConvention:'' %>" />
    <div class="form-group">
        <label class="control-label col-md-3">Key quy ước</label>
        <div class="col-md-9" id="selectcategorys">
            <input name="keyConvention" type="text" class="form-control" placeholder="Nhập key quy ước" value="<%= (typeof(data) !== 'undefined')? data.keyConvention:'' %>" />
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-md-3">Key </label>
        <div class="col-md-9" id="selectcategorys">
            <input name="key" type="text" class="form-control" placeholder="Nhập key " value="<%= (typeof(data) !== 'undefined')? data.key:'' %>" />
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-md-3">Value</label>
        <div class="col-md-9" id="selectcategorys">
            <input name="value" type="text" class="form-control" placeholder="Nhập value" value="<%= (typeof(data) !== 'undefined')? data.value:'' %>" />
        </div>
    </div>
</form>