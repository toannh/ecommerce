<table class="table">
    <tbody>
        <%  if(data!=null && data.properties!=null){ 
           
            $.each(data.properties,function(i){
             var values = "";   
              if(data.properties[i].categoryPropertyValueIds!=null ){
                    var categoryPropertyValueIds = data.properties[i].categoryPropertyValueIds;
                    
                    $.each(categoryPropertyValueIds,function(j){
                 
                           var catePropertyValues = data.catePropertyValues;
                               
                           $.each(catePropertyValues,function(l){
                          
                               if(catePropertyValues[l].categoryPropertyId==data.properties[i].categoryPropertyId && catePropertyValues[l].id == categoryPropertyValueIds[j] && catePropertyValues[l].name!==null && catePropertyValues[l].name.trim()!=''){
                                       values+='<li>'+catePropertyValues[l].name+'</li>';
                                     
                               }
                           });    
                    });
              }
        %>
             <% if(values!=null && values.trim()!=''){ %>
                    <tr>
                        <td class="col1">
                            <% 
                            $.each(data.cateProperties,function(x){ 
                             if(data.cateProperties[x].id==data.properties[i].categoryPropertyId){
                            %>
                            <%=data.cateProperties[x].name%>
                     <% } }); %>
                    </td>
                    <td class="col2">
                        <ul>
                            <%=values%>
                        </ul>
                    </td>
                    </tr>
            <% } %>
            
                    <% if((data.properties[i].categoryPropertyValueIds==null || data.properties[i].categoryPropertyValueIds<=0) && data.properties[i].inputValue!=null&& data.properties[i].inputValue!=''){

                    %>
                                                        <tr>
                                                            <td class="col1">
                                                                 <% 
                                                                $.each(data.cateProperties,function(z){ 
                                                                 if(data.cateProperties[z].id==data.properties[i].categoryPropertyId){
                                                                %>
                                                                <%=data.cateProperties[z].name%>
                                                            <% } }); %>
                                                            </td>
                                                            <td class="col2">
                                                                <ul>
                                                                    <%=data.properties[i].inputValue%>
                                                                </ul>
                                                            </td>
                                                        </tr>
                    <% }%>
            
        <% }); 
        
        } %>
  
</tbody>
</table>