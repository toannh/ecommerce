<form class="form-horizontal" id="add-ward-form">
    <input type="hidden" name="id" id="id" value="<%= (typeof(data) !== 'undefined')? data.id:'' %>" />
    <input type="hidden" name="districtId" for="districtId" value="<%= (typeof(districtId) !== 'undefined')? districtId:'' %>" />
    <div class="form-group">
        <label class="control-label col-sm-3">Phường / xã</label>
        <div class="col-sm-9">
            <input name="name" type="text" class="form-control" value="<%= (typeof(data) !== 'undefined')? data.name:'' %>" placeholder="Tên quận / huyện"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-3">Vị trí</label>
        <div class="col-sm-9">
            <input name="position" type="text" class="form-control" value="<%= (typeof(data) !== 'undefined')? data.position:'' %>" placeholder="Vị trí"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-md-3">Mã ship chung:</label>
        <div class="col-sm-9">
            <input name="scId" type="text" class="form-control" placeholder="Nhập mã ship chung" value="<%= (typeof(data) !== 'undefined')? data.scId:'' %>" />
        </div>
    </div>
</form>


