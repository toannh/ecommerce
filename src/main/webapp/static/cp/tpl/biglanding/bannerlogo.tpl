<form class="form-horizontal" id="image-form-banner1" role="form" >
    <input type="hidden" name="id" value="<%=(typeof(data) !== 'undefined')? data.id:'' %>"/>
    <input type="hidden" name="targetIdBanner" value="<%=(typeof(data) !== 'undefined')? data.id:'' %>"/>
    <div class="form-group">
        <label class="control-label col-sm-4"> Logo</label>
        <input type="file" style="display:none" id="lefile" onchange="biglanding.uploadBannerLD(1);" name="image">
        <div style="width:300px;" class="input-group">
            <input type="text" class="form-control" id="photoCover" name="photoCover"/>
            <a onclick="$('#lefile').click();" class="btn btn-default input-group-addon">Chọn File</a>    
        </div>
        <label class="control-label col-sm-4"> </label>
        <% if(typeof(data) !== 'undefined' && data.logoBanner!=null && data.logoBanner!=''){ %>
        <img src="<%=data.logoBanner%>" width="200" style="padding-top: 5px;"/>
        <% } else{ %>   
        <img src="" width="200" style="padding-top: 5px; display: none" /> 
        <% } %>    

    </div>
</form>
<form class="form-horizontal" id="image-form-banner2" role="form" >
    <div class="form-group">
        <label class="control-label col-sm-4"> Banner chính</label>
        <input type="file" style="display:none" id="lefileheartbanner" onchange="biglanding.uploadBannerLD(2);" name="image">
        <div style="width:300px;" class="input-group">
            <input type="text" class="form-control" id="photoCover" name="photoCover"/>
            <a onclick="$('#lefileheartbanner').click();" class="btn btn-default input-group-addon">Chọn File</a>    
        </div>
        <label class="control-label col-sm-4"> </label>
        <% if(typeof(data) !== 'undefined' && data.heartBanner!=null && data.heartBanner!=''){ %>
        <img src="<%=data.heartBanner%>" width="200" style="padding-top: 5px;"/>
        <% } else{ %>   
        <img src="" width="200" style="padding-top: 5px; display: none" /> 
        <% } %>     

    </div>
</form>
<form class="form-horizontal" id="image-form-banner3" role="form" >
    <div class="form-group">
        <label class="control-label col-sm-4"> Banner phụ</label>

        <input type="file" style="display:none" id="lefilecenterbanner" onchange="biglanding.uploadBannerLD(3);" name="image">
        <div style="width:300px;" class="input-group">
            <input type="text" class="form-control" id="photoCoverThumb" name="photoCoverThumb"/>
            <a onclick="$('#lefilecenterbanner').click();" class="btn btn-default input-group-addon">Chọn File</a>    
        </div>
        <label class="control-label col-sm-4"> </label>
        <% if(typeof(data) !== 'undefined' && data.centerBanner!=null && data.centerBanner!=''){ %>
        <img src="<%=data.centerBanner%>" width="200" style="padding-top: 5px;"/>
        <% } else{ %>   
        <img src="" width="200" style="padding-top: 5px; display: none" /> 
        <% } %>     

    </div>
</form>
<form class="form-horizontal" id="background-form" role="form">
    <div class="form-group">
        <label class="control-label col-sm-4">Màu nền</label>
        <div style="width:300px;" class="input-group">
            <input name="background" value="<%= (typeof data!=='undefined')? data.background: ''%>" type="text" class="form-control" placeholder="Nhập mã màu ví dụ #5448D5" onchange="biglanding.changeBackground(<%=(typeof(data) !== 'undefined')? data.id:'' %>)"/>
        </div>
    </div>
</form>
<form class="form-horizontal" id="image-form-banner" role="form" >
    <div class="form-group">
        <label class="control-label col-sm-4">Ẩn/Hiện Banner phụ:</label>
        <div style="width:300px;" class="input-group">
            <label>
                <input type="checkbox" onclick="biglanding.changeBannerCenterActive( < %= (typeof (data) !== 'undefined')? data.id:'' % > );" name="centerBannerActive" <% if(typeof data!=='undefined'){ if(data.centerBannerActive==true){ %>checked<% }} %>>&nbsp;&nbsp;
                       Hiển thị
            </label>
        </div>
    </div>
</form>
