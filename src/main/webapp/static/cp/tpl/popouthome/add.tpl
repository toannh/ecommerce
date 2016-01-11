
<form class="form-horizontal" id="form-add">
    <input type="hidden" name="id" value="<%=(typeof data!=='undefined')? data.id: ''%>"/>
    <div class="form-group">
        <label class="control-label col-sm-2">Tiêu đề:</label>
        <div class="col-sm-10">
            <input name="title" value="<%= (typeof data!=='undefined')? data.title: ''%>" type="text" class="form-control" placeholder="Tiêu đề"/>
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-2">Ảnh:</label>
        <div class="col-sm-10">
            <input type="file" style="display:none" id="lefile" name="imagefile">
            <div style="width:300px;" class="input-group">
                <input type="text" class="form-control" id="photoCover">
                <a onclick="$('#lefile').click();" class="btn btn-default input-group-addon">Chọn</a>   
            </div>
            <p>Kích thước chuẩn: 481x500px</p>
        </div>

    </div>
    <% if(typeof data!=='undefined' && data.image !== null) {%>
        <div class="form-group">
            <label class="control-label col-sm-2"> </label>
            <div class="col-sm-10">
                <img src="<%= (typeof data!=='undefined')? data.image: ''%>" width="100" alt="Ảnh"/>
            </div>     
        </div>
        <%}%>
    <div class="form-group">
        <label class="control-label col-sm-2">Đường dẫn:</label>
        <div class="col-sm-10">
            <input name="url" value="<%=(typeof data!=='undefined')? data.url: ''%>" type="text" class="form-control" placeholder="Đường dẫn link"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Kiểu hiển thị POP:</label>
        <div class="col-sm-10">
            <select name="type" class="form-control">
                <option value="0">Chọn hiển thị</option>
                <option value="1" <% if(typeof data!=='undefined'){ %><%=(data.type==1)? 'selected': ''%> <%}%>>Người mua</option>
                <option value="2"  <% if(typeof data!=='undefined'){ %><%=(data.type==2)? 'selected': ''%><%}%>>Người bán</option>
            </select>
        </div>
    </div>
    <div class="form-group" style="margin-bottom: 40px;">
        <label class="control-label col-sm-2" style="padding-left: 0">Trạng thái:</label>
        <div class="col-sm-10">
            <label>
                <input type="checkbox" <% if(typeof data!=='undefined'){ %><%=(data.active)? 'checked': ''%><%}%> id="active" name="active">&nbsp;&nbsp;
                       Hiển thị
            </label>
        </div>
    </div>

</form>
