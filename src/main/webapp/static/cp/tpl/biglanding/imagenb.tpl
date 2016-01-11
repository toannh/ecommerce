<form id="image-form-i" style="display:none">
    <input type="file" style="display:none" id="lefile" name="image" onchange="biglanding.uploadImageItem();">
    <input type="text" style="display:none" name="targetId" value="<%=(typeof data!=='undefined' && data!=null )? data.id: ''%>">
    <input type="text" style="display:none" name="targetIdFix" value="<%=(typeof data!=='undefined' && data!=null )? data.id: ''%>">
</form>
<div class="row">
    <div class="form-group">
        <div class="col-xs-5">
            <input name="itemId" type="text" class="form-control" placeholder="Nhập ID sản phẩm">
        </div>
        <div class="col-xs-2">
            <button type="button" class="btn btn-primary" onclick="biglanding.saveItemNB('<%=bigLandingCateId %>','<%=position%>');" >
                <i class="glyphicon glyphicon-edit" ></i> Lấy sản phẩm
            </button>
        </div>
    </div>
</div>
<form id="image-form" class="form-horizontal" role="form" style="margin-top: 10px">
    <div class="form-group">
        <div class="col-xs-5">
            <input name="downloadImageItem" type="text" class="form-control" placeholder="Điền Url ảnh muốn lấy về hoặc đường dẫn file">
            <input name="urlOld" type="hidden" value="" class="form-control">
            <input name="widthF" type="hidden" value="<%=width%>" class="form-control">
            <input name="heightF" type="hidden" value="<%=height%>" class="form-control">
        </div>
        <div class="col-xs-6">
            <div class="btn-group">
                <button type="button" class="btn btn-warning" style="text-transform : none" onclick="biglanding.downloadImageItem();">
                    <span class="fa fa-image"></span>Lấy ảnh về</button>
                <button type="button" class="btn btn-success" style="text-transform : none" onclick="$('#lefile').click();" >
                    <span class="glyphicon glyphicon-upload"></span>Upload từ máy</button>
            </div>

        </div>
    </div>
    <div class="form-group" style="width: 100%; min-height: 300px; margin-left: 5px; max-width: 600px">
        <label class="control-label col-xs-8" style="text-align: left;">
            <span style="font-weight: bold">Kích thước mặc định:</span> [width]: <%=width%>px | [height]: <%=height%>px 
        </label><div class="clearfix"></div>
        <div class="row">
            <div style="width: 100%">
                <a class="thumbnail" style="min-height: 200px; max-width: 600px">
                    <img id="photo" for="<%=(typeof data!=='undefined' && data!=null )? data.sellerImage: ''%>" src="<%=(typeof data!=='undefined' && data!=null )? data.image: ''%>" />
                </a>
            </div>
            <div class="col-xs-8 col-md-3" style="display: none">
                <label class="control-label col-xs-10" style="text-align: left">x1 <input type="text" id="x1" class="form-control" disabled="true" name="x" value="0"></label>
                <label class="control-label col-xs-10" style="text-align: left">y1<input type="text" id="y1" class="form-control" disabled="true" name="y" value="0"></label>
                <label class="control-label col-xs-10" style="text-align: left">Width<input type="text" id="w" class="form-control" disabled="true" name="width" value="0"></label>
                <label class="control-label col-xs-10" style="text-align: left">Height<input type="text" id="h" class="form-control" disabled="true" name="height" value="0"></label>
                <label class="control-label col-xs-10" style="text-align: left">position<input type="text" id="position" class="form-control" disabled="true" name="position" value="<%=position%>"></label>
                <%
                var temp = '';
                if(tem=='1'){
                temp = 'template1';
                }
                if(tem=='2'){
                temp = 'template2';
                }
                if(tem=='3'){
                temp = 'template3';
                }
                if(tem=='4'){
                temp = 'template4';
                }
                %>
                <label class="control-label col-xs-10" style="text-align: left">template<input type="text" id="template" class="form-control" disabled="true" name="template" value="<%=temp%>"></label>
            </div>

        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">ID</label>
        <div class="col-sm-5">
            <input name="itemIdFix" value="<%=(typeof data!=='undefined' && data!=null )? data.itemId: ''%>" disabled="disabled" type="text" class="form-control" placeholder="Điền Id sản phẩm tại đây">
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Tiêu đề</label>
        <div class="col-sm-5">
            <input name="title" value="<%=(typeof data!=='undefined' && data!=null )? data.name: ''%>" for="<%=(typeof data!=='undefined' && data!=null )? data.id: ''%>" type="text" class="form-control bigLandingName<%=(typeof data!=='undefined' && data!=null )? data.id: ''%>" placeholder="Tiêu đề sản phẩm">
        </div>
    </div>
</form>

