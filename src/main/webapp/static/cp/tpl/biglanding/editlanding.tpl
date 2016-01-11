<form name="categoryFrom" class="form-horizontal" id="lcategory-form-edit">
    <input type="text" style="display: none" name="id" value="<%= (typeof data!=='undefined')? data.id: ''%>" />            
    <div class="form-group">
        <label class="control-label col-sm-4">Tên Landing:</label>
        <div class="col-sm-8">
            <input name="name" value="<%= (typeof data!=='undefined')? data.name: ''%>" type="text" class="form-control" placeholder="Tên landing"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Thời gian bắt đầu người mua:</label>
        <div class="col-sm-8">
            <input name="startTime" placeholder="Thời gian bắt đầu người mua" id="startTime" type="hidden" value="<%= (typeof data!=='undefined')? data.startTime: ''%>" class="form-control timeSelect"  />
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Thời gian kết thúc người mua:</label>
        <div class="col-sm-8">
            <input name="endTime" placeholder="Thời gian kết thúc người mua" id="endTime" type="hidden" value="<%= (typeof data!=='undefined')? data.endTime: ''%>" class="form-control timeSelect"  />
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Thời gian bắt đầu người bán:</label>
        <div class="col-sm-8">
            <input name="startTimeSeller" placeholder="Thời gian bắt đầu người bán" id="startTime" type="hidden" value="<%= (typeof data!=='undefined')? data.startTimeSeller: ''%>" class="form-control timeSelect"  />
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Thời gian kết thúc người bán:</label>
        <div class="col-sm-8">
            <input name="endTimeSeller" placeholder="Thời gian kết thúc người bán" id="endTime" type="hidden" value="<%= (typeof data!=='undefined')? data.endTimeSeller: ''%>" class="form-control timeSelect"  />
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Mô tả Item/CheckOut/DetailOrder:</label>
        <div class="col-sm-8">
            <input name="description" value="<%= (typeof data!=='undefined')? data.description: ''%>" type="text" class="form-control" placeholder="Nhập mô tả"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Mô tả Hóa đơn bán hàng</label>
        <div class="col-sm-8">
            <textarea name="descriptionOrder" class="form-control" rows="5" cols="45" placeholder="Mô tả cho trang hóa đơn bán hàng, duyệt vận COD, Vận chuyển hàng loạt.."><%= (typeof data!=='undefined')? data.descriptionOrder: ''%></textarea>
        </div>
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
    <div class="form-group"  <%if(typeof data!=='undefined'){ if(data.id!=null){ %> style="display: none" <% }} %> >
         <label class="control-label col-sm-4">Template:</label>
        <div class="col-sm-8">
            <select name="landingTemplate" class="form-control">
                <option value="0">--Chọn--</option>
                <option value="template1" <% if(typeof data!=='undefined'){ if(data.landingTemplate=='template1'){ %>selected<% }} %>>Template 1</option>
            </select>
        </div>
    </div>
    <div class="form-group" <%if(typeof data!=='undefined'){ if(data.id!=null){ %> style="display: none" <% }} %> >
         <label class="control-label col-sm-4">Demo:</label>
        <div class="col-sm-8 templateHTML">
        </div>
    </div>
    <%if(typeof data!=='undefined'){ if(data.id!=null){ %> 
    <div class="form-group">
        <label class="control-label col-sm-4">Template:</label>
        <div class="col-sm-8" style="color: red">
            Template không được sửa vì nó ảnh hưởng tới giao diện bên ngoài
        </div>
    </div>

    <% } } %> 

</form>