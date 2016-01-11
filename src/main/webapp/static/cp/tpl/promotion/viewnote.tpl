<form class="form-horizontal" id="add-note" >
    <% if(data!=null){  $.each(data, function(i) { %>
    <div class="form-group" style=" margin-top: 1px;  margin-bottom: 0px;">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 class="panel-title" id="panel-title"><%=data[i].administrator%> - 
                    <% if(data[i].updateTime<=0){ %>
                Tạo lúc: <%=textUtils.formatTime(data[i].createTime,'hour')%>
                <% }else{ %>
                   Cập nhật lúc: <%=textUtils.formatTime(data[i].updateTime,'hour')%>
                <% } %>
                </h3>
            </div>
            <div class="panel-body">
                 <%=data[i].note%>
            </div>
        </div>
    </div>
    <% }); }else{ %>
    <div class="form-group">Không có nhân viên chăm sóc nào.</div>
    <% } %>
</form>