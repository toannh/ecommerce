<form name="landingNewSlideForm" class="form-horizontal" id="landingNewSlideForm">
    <input type="text" style="display: none" name="id" value="<%= (typeof data!=='undefined')? data.id: ''%>" />            
    <input type="text" style="display: none" name="landingNewId" value="<%= (typeof data!=='undefined')? data.landingNewId: landingNewId%>" />            
    <div class="form-group">
        <label class="control-label col-sm-4">Name:</label>
        <div class="col-sm-8">
            <input name="name" value="<%= (typeof data!=='undefined')? data.name: ''%>" type="text" class="form-control" placeholder="Tên landing"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Link dẫn:</label>
        <div class="col-sm-8">
            <input name="url" value="<%= (typeof data!=='undefined')? data.url: ''%>" type="text" class="form-control" placeholder="Link dẫn"/>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-4">Vị trí:</label>
        <div class="col-sm-8">
            <input name="position" value="<%= (typeof data!=='undefined')? data.position: '0'%>" type="text" class="form-control" placeholder="Vị trí slide ảnh"/>
        </div>
    </div>


    
    <div class="form-group"> 
        <label class="control-label col-sm-4"> Ảnh</label>
        <div style="display: none"><input type="file" style="display:none" id="lefile" name="banner"></div>

        <div style="width:300px;padding:0 15px;" class="input-group">
            <input type="text" class="form-control" id="photoCover" name="banner"/>
            <a onclick="$('#lefile').click();" class="btn btn-default input-group-addon">Chọn</a>    
        </div>
        <label class="control-label col-sm-9"> <% if(typeof(data) !== 'undefined'){ %>
        <img src="<%= (typeof data!=='undefined')? data.image: ''%>" style="padding-left: 20px;"/>
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