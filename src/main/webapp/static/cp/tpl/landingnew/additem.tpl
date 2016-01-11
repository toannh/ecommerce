<form class="form-horizontal" id="landing-add-form">
    <div class="row">
        <div class="form-group">
            <div class="col-xs-5">
                <input name="itemId" type="text" class="form-control" placeholder="ID sản phẩm">
                <input name="bigLandingCateId" type="hidden" class="form-control" value="<%=landingNewId%>">
            </div>
            <div class="col-xs-2">
                <button type="button" class="btn btn-primary" onclick="landingnew.saveItem('<%= landingNewId %>')" >
                    <i class="glyphicon glyphicon-edit" ></i> Thêm mới
                </button>
            </div>
        </div>

    </div>
    <div class="row">
        <table class="table table-striped table-bordered table-responsive bigLandingItemRow" id="bigLandingItemRow" style="margin-top: 10px">
            
                <tr class="success" >
                    <th style="text-align: center; vertical-align: middle;" >ID</th>
                    <th style="text-align: center; vertical-align: middle;" >Tên sản phẩm</th>
                    <th style="text-align: center; vertical-align: middle;" >Ảnh</th>
                    <th style="text-align: center; vertical-align: middle;" >Vị trí</th>
                    <th style="text-align: center; vertical-align: middle;" >Hiển thị</th>
                    <th style="text-align: center; vertical-align: middle;" >Chức năng</th>
                </tr>
            

            <tbody class="body" >
                <% if(data == null || data.length <= 0) { %>
                <tr><td class="text-center" colspan="6"><p style="color: red ;"> Chưa có sản phẩm nào! </p></td></tr>
                <% } else { %>
                <% $.each(data , function(){ %>
                <tr for="<%= this.id%>" class="<%= this.id%>">
                    <% if(typeof this.item == 'undefined' || this.item == null) { %>
                    <td colspan="2" style="color: red">Sản phẩm chính đã bị xóa.</td>
                    <% } else { %>
                    <td class="text-center" style="vertical-align: middle;"><%= this.item.id %></td>
                    <td class="text-center" style="vertical-align: middle;"><input for="<%=this.id%>" name="name" class="form-control" value="<%= this.item.name %>"/></td>
                    <% }%>
                    <td class="text-center" style="vertical-align: middle;"><img src="<%=this.image%>" width="30"/></td>
                    <td class="text-center" style="vertical-align: middle;"><input for="<%=this.id%>" name="position" class="form-control" style="width:50px; text-align: center"  value="<%= this.position %>"/></td>
                    <td class="text-center" style="vertical-align: middle;"><input for="<%=this.id%>" name="active" type="checkbox" <%=this.active?'checked="true"':''%> /></td>
                    <td class="text-center" style="vertical-align: middle">
                        <button type="button" class="btn btn-danger landingNewName" onclick="landingnew.delitem('<%= this.id %>');"  ><i class="glyphicon glyphicon-trash"></i> Xóa</button>
                    </td>
                </tr>
                <% }); } %>
            </tbody>

        </table>
        <ul class="pagination pull-right" id="pagination">
        <li><a href="#">«</a></li>
        <li><a href="#">»</a></li>
    </ul>
        

    </div>
</form>