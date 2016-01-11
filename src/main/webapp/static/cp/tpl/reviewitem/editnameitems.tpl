<form class="form-inline">
    <div class="form-group">
        Thêm trước:
        <label class="sr-only" for="exampleInputEmail2">Trước : </label>
        <input type="text" class="form-control" id="prefix_name" placeholder="Enter name">
    </div>
    <div class="form-group">
        Thêm sau:
        <label class="sr-only" for="exampleInputEmail2">Sau : </label>
        <input type="text" class="form-control" id="suffix_name" placeholder="Enter name">
    </div>
    <div class="alert alert-danger" role="alert" id="message_err_name" style="display : none;margin-top: 20px;">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <span class="sr-only">Error:</span>
        Chưa điền name.
    </div>
</form>