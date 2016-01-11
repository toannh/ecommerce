<form class="form-horizontal" id="mapcate-add-form">
    <div class="form-group">
        <label class="control-label col-sm-3">Mã danh mục gốc</label>
        <div class="col-sm-9">
            <input name="origCateId" type="text" class="form-control" placeholder="Nhập mã danh mục nguồn"  />
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-3">Mã danh mục đích</label>
        <div class="col-sm-9">
            <input name="destCateId" type="text" class="form-control" placeholder="Nhập mã danh mục đích"  />
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-3">Trạng thái hoạt động</label>
        <div class="col-sm-9">
            <select name="active" type="text" class="form-control">
                <option value="1">Đang hoạt động</option>
                <option value="2">Tạm khoá</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-3">
            <label><input id="check-new-prop" onchange="mapcate.showNewPropInput();" type="checkbox" value="false">Tự động tạo thuộc tính mới bằng tên danh mục nguồn</label>
        </div> 
        <div class="col-sm-9">
        <input id="newPropName" style="display: none;" type="text" class="form-control" placeholder="Nhập tên thuộc tính mới"  />
        </div></div>
</form>