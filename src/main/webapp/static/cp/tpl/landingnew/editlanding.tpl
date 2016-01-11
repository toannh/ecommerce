<form name="LandingNewForm" class="form-horizontal" id="landingNewForm">
    <input type="text" style="display: none" name="id" value="<%= (typeof data!=='undefined')? data.id: ''%>" />            
    <div class="form-group">
        <label class="control-label col-sm-4">Tên Landing:</label>
        <div class="col-sm-8">
            <input name="name" value="<%= (typeof data!=='undefined')? data.name: ''%>" type="text" class="form-control" placeholder="Tên landing"/>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-4">Mô tả:</label>
        <div class="col-sm-8">
            <textarea class="form-control" name="description" placeholder="Nhập mô tả"><%= (typeof data!=='undefined')? data.description: ''%></textarea>

        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Màu sắc:</label>
        <div class="col-sm-8">
            <input name="color" id="colorpicker" value="<%= (typeof data!=='undefined')? data.color: ''%>" class="form-control"  />
        </div>
    </div>


    <div class="form-group">
        <label class="control-label col-sm-4"> Ảnh</label>
        <div style="display: none"><input type="file" style="display:none" id="lefile" name="bannerCenter"></div>

        <div style="width:300px;padding:0 15px;" class="input-group">
            <input type="text" class="form-control" id="photoCover" name="bannerCenter"/>
            <a onclick="$('#lefile').click();" class="btn btn-default input-group-addon">Chọn</a>    
        </div>
        <label class="control-label col-sm-9"> <% if(typeof(data) !== 'undefined'){ %>
            <img src="<%=data.bannerCenter%>" style="padding-left: 20px;"/>
            <% } %>      </label>


    </div>

    <div class="form-group">
        <label class="control-label col-sm-4">Trạng thái:</label>
        <div class="col-sm-8">
            <label>
                <input type="checkbox" name="active" <% if(typeof data!=='undefined'){ if(data.active==true){ %>checked<% }} %>>&nbsp;&nbsp;
                       Hiển thị
            </label>
        </div>
    </div>

</form>