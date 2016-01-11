<style>
    #wpseosnippet .title {
        
        overflow: hidden;
        width: 512px;
        color: #1e0fbe;
        font-size: 18px!important;
        line-height: 1.2;
        white-space: nowrap;
        text-overflow: ellipsis;
    }
    #wpseosnippet {
        width: auto;
        max-width: 520px;
        margin: 0 0 10px;
        padding: 0 5px;
        font-family: Arial,Helvetica,sans-serif;
        font-style: normal;
    }
    #wpseosnippet .url {
        color: #006621;
        font-size: 13px;
        line-height: 16px;
    }
    #wpseosnippet .desc {
        font-size: small;
        line-height: 1.4;
        word-wrap: break-word;
    }
    #wpseosnippet .desc {
        font-size: small;
        line-height: 1.4;
        word-wrap: break-word;
    }
    #wpseosnippet .desc .autogen {
        color: #777;
    }
</style>
<form name="categoryFrom" class="form-horizontal" id="category-form-edit-seo" style="width: 100%;">
    <input type="text" style="display: none" name="id" value="<%= (typeof data!=='undefined')? data.id: ''%>" />            
    <div class="form-group ancestors" style="margin-bottom: 0px;">                  
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Xem trước:</label>
        <div class="col-sm-9">                    
            <div id="wpseosnippet">
                <a class="title" id="wpseosnippet_title" href="#"><%= (typeof data!=='undefined')? data.title: '...'%></a><br/>
                <% var namelinks = textUtils.createAlias(data.name);%>
                <span class="url"><%=baseUrl +'/mua-ban/'+data.id+'/'+namelinks+'.html'%></span>
                <p class="desc"><span class="autogen"></span><span class="content"><%= (typeof data!=='undefined')? data.description: '...'%></span></p>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Tiêu đề SEO:</label>
        <div class="col-sm-9">                    
            <input name="title" onkeyup="category.countCharacters('title')" onkeypress="category.countCharacters('title')" onkeydown="category.countCharacters('title')" maxlength="70" type="text" class="form-control" placeholder="Title Seo" value="<%= (typeof data!=='undefined')? data.title: ''%>" />
            <span style="color: #999999">Tiêu đề hiển thị trong công cụ tìm kiếm được giới hạn đến 70 ký tự, <span style="color: green; font-weight: bold;" class="count_title">70</span> ký tự còn lại.</span>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Mô tả SEO:</label>
        <div class="col-sm-9">                    
            <textarea name="description" class="form-control" onkeyup="category.countCharacters('description')" onkeypress="category.countCharacters('description')" onkeydown="category.countCharacters('description')" maxlength="156" placeholder="Description Seo"><%= (typeof data!=='undefined')? data.description: ''%></textarea>
            <span style="color: #999999">Các meta description sẽ được giới hạn 156 ký tự, <span style="color: green; font-weight: bold;" class="count_description">156</span> ký tự còn lại.</span>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Từ khóa gợi ý:</label>
        <div class="col-sm-9">                    
            <textarea name="metaDescription" class="form-control" placeholder="Meta Description"><%= (typeof data!=='undefined')? data.metaDescription: ''%></textarea>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Nội dung SEO:</label>
        <div class="col-sm-9">                    
            <textarea name="content" id ="txt_content" class="form-control" placeholder="Nội dung"><%= (typeof data!=='undefined')? data.content: ''%></textarea>                
        </div>
    </div>

</form>