<form name="itemFrom" class="form-horizontal" id="item-form">
    
    <div class="form-group">     
        <label class="control-label col-sm-3">Danh mục cha:</label>
        <div class="col-sm-9" style="padding: 0 30px;">
            <div id="selectcategorys"></div>
            <input type="hidden" value="<%= (typeof data!=='undefined')? data.categoryId: ''%>"  name="categoryId" class="categoryId" id="categoryId"/>
        </div>
    </div>        
    <div class="alert alert-danger" role="alert" id="message_err_map" style="display : none;margin-top: 20px;">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <span class="sr-only">Error:</span>
        Bạn chưa chọn danh mục lá.
    </div>
</form>