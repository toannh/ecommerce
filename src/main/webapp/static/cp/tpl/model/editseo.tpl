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
<form name="categoryFrom" class="form-horizontal" id="model-form-edit-seo" style="width: 100%;">
    <input type="text" style="display: none" name="modelId" value="<%= (typeof data!=='undefined')? data.id: ''%>" />            
    <div class="form-group ancestors" style="margin-bottom: 0px;">                  
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Xem trước:</label>
        <div class="col-sm-9">                    
            <div id="wpseosnippet"> 
                <a class="title" id="wpseosnippet_title" href="#"><%= (typeof data.modelSeo!=='undefined' && data.modelSeo!=null)? data.modelSeo.title: '...'%></a><br/>
                <% var namelinks = textUtils.createAlias(data.name);%>
                <span class="url"><%=baseUrl +'/model/'+data.id+'/'+namelinks+'.html'%></span>
                <p class="desc"><span class="autogen"></span><span class="content"><%= (typeof data.modelSeo!=='undefined' && data.modelSeo!=null)? data.modelSeo.description: '...'%></span></p>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Tiêu đề SEO:</label>
        <div class="col-sm-9">                    
            <input name="title" onkeyup="model.countCharacters('title')" onkeypress="model.countCharacters('title')" onkeydown="model.countCharacters('title')" maxlength="70" type="text" class="form-control" placeholder="Title Seo" value="<%= (typeof data.modelSeo!=='undefined' && data.modelSeo!=null)? data.modelSeo.title: ''%>" />
            <span style="color: #999999">Tiêu đề hiển thị trong công cụ tìm kiếm được giới hạn đến 70 ký tự, <span style="color: green; font-weight: bold;" class="count_title">70</span> ký tự còn lại.</span>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Mô tả SEO:</label>
        <div class="col-sm-9">                    
            <textarea name="description" class="form-control" onkeyup="model.countCharacters('description')" onkeypress="model.countCharacters('description')" onkeydown="model.countCharacters('description')" maxlength="156" placeholder="Description Seo"><%= (typeof data.modelSeo!=='undefined' && data.modelSeo!=null)? data.modelSeo.description: ''%></textarea>
            <span style="color: #999999">Các meta description sẽ được giới hạn 156 ký tự, <span style="color: green; font-weight: bold;" class="count_description">156</span> ký tự còn lại.</span>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Nội dung SEO:</label>
        <div class="col-sm-9">                    
            <textarea name="content" id="txt_content" class="form-control" placeholder="Làm giàu nội dung"><%= (typeof data.modelSeo!=='undefined' && data.modelSeo!=null)? data.modelSeo.content: ''%></textarea>                
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2">Thuộc tính Model Seo:</label>
        <div class="col-sm-9">                    
            <textarea name="contentProperties" id="txt_contents" class="form-control" placeholder="Nội dung"><%= (typeof data!=='undefined')? data.contentProperties: ''%></textarea>                
            <label class="control-label"><input type="checkbox" name="propertiesFag"<%if(typeof data.modelSeo!=='undefined' && data.modelSeo!=null && (data.modelSeo.contentProperties!=null && data.modelSeo.contentProperties!='')) { %>checked <% } %>><span style="color: red">Sử dụng tính năng thuộc tính Model Seo</span> </label>
        </div>
    </div>
   

</form>