<form name="categoryFrom" class="form-horizontal" id="lcategory-form-edit">
    <input type="text" style="display: none" name="bigLandingId" value="<%=(typeof biglandingid!=='undefined')? biglandingid: ''%><%= (typeof data!=='undefined')? data.bigLandingId: ''%>" />            
    <input type="text" style="display: none" name="id" value="<%= (typeof data!=='undefined')? data.id: ''%>" />      
    <%if(typeof data!=='undefined'){ if(data.parentId!=null){ var parentId = data.parentId; }} %>
    <input type="text" style="display: none" name="parentId" value="<%=(typeof parentId!=='undefined')?parentId:''%>" />            
    <div class="form-group">
        <label class="control-label col-sm-4">Tên danh mục:</label>
        <div class="col-sm-8">
            <input name="name" value="<%= (typeof data!=='undefined')? data.name: ''%>" type="text" class="form-control" placeholder="Tên danh mục"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-4">Thứ tự:</label>
        <div class="col-sm-8">                    
            <input name="position" value="<%=(typeof data!=='undefined')? data.position: ''%>" type="text" class="form-control" placeholder="Thứ tự"/>                    
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
    <% if(typeof parentId==='undefined') { %>
   
    <div class="form-group" <%if(typeof data!=='undefined'){ if(data.id!=null){ %> style="display: none" <% }} %> >
        <label class="control-label col-sm-4">Template:</label>
        <div class="col-sm-8">
            <select name="template" class="form-control">
                <option value="0">--Chọn--</option>
                <option value="template1" <% if(typeof data!=='undefined'){ if(data.template=='template1'){ %>selected<% }} %>>Template 1</option>
                <option value="template2" <% if(typeof data!=='undefined'){ if(data.template=='template2'){ %>selected<% }} %>>Template 2</option>
                <option value="template3" <% if(typeof data!=='undefined'){ if(data.template=='template3'){ %>selected<% }} %>>Template 3</option>
                <option value="template4" <% if(typeof data!=='undefined'){ if(data.template=='template4'){ %>selected<% }} %>>Template 4</option>
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
  
   <% }} %> 
    
    <%}%> 
</form>