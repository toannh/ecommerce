<form name="itemFrom" class="form-horizontal" id="item-form-shopCate">
    
    <div class="form-group">     
        <label class="control-label col-sm-3">Danh mục cha:</label>
        <div class="col-sm-9" style="padding: 0 30px;">
            <div id="selectShopCategorys"></div>
            <input type="hidden" value="<%= (typeof data!=='undefined')? data.categoryId: ''%>"  name="shopCategoryId" class="shopCategoryId" id="shopCategoryId"/>
        </div>
    </div>        
    <div class="alert alert-danger" role="alert" id="message_err_shopmap" style="display : none;margin-top: 20px;">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <span class="sr-only">Error:</span>
        Bạn chưa chọn danh mục lá.
    </div>
</form>