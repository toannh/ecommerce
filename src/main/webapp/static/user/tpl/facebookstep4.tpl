                   <div class="modal-body">
                        <div class="fb-post">
                            <div class="fp-steps">
                                <div class="fp-label">
                                	<i class="fa fa-file-word-o"></i>
                                    Chi tiết tin đăng
                                </div><!-- fp-label -->
                                <div class="fp-help">
                                    <a href="#"><i class="fa fa-question"></i>Hướng dẫn</a>
                                </div><!-- fp-help -->
                            </div><!-- fp-steps -->
                            <div class="fp-list">
                                <div class="fp-table table-responsive">
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th width="5%">STT</th>
                                                <th width="15%">Sản phẩm</th>
                                                <th width="25%" class="text-left">Thông tin</th>
                                                <th width="25%">Trạng thái đăng<br />(thành công/tổng số nhóm)</th>
                                                <th width="10%">Mã đăng</th>
                                                <th width="15%">Thời gian đăng</th>
                                                <th width="5%"></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            
                                            <% var count = 0; %>
                                            <% $.each(data, function () { %> 
                                            <% count++; %>
                                            <tr>
                                                <td><%= count %></td>
                                                <td><div class="table-image"><img src="<%= this.image %>" alt="img" /></div></td>
                                                <td class="text-left">
                                                    <p><%= this.name %></p>
                                                    <p><%= this.message %></p>
                                                </td>
                                                <td>
                                                    <p><b><span class="text-primary"><%= this.sucessGroup %></span>/<%= this.totalGroup %></b> nhóm</p>
                                                    <p>(thành công/tổng số nhóm)</p>
                                                </td>
                                                <td><p class="text-primary"><%= this.id %></p></td>
                                                <td><p class="text-primary"><%= this.timeFinish %></p></td>
                                                <td><a class="text-primary" onclick="upfacebook.detailLink('<%= this.resultLink %>');">Chi tiết</a></td>
                                            </tr>
                                            <% }); %>
                                            
                                        </tbody>
                                    </table>  
                                </div><!-- table-responsive -->
                                <div class="fp-desc">
                                	Trong trường hợp đăng bán group facebook thất bại chúng tôi sẽ tự hoàn trả xèng<br />về tài khoản của bạn !
                                </div><!-- fp-desc --> 
                            </div><!-- fp-list --> 
                        </div><!-- fp-post --> 
                    </div><!-- end modal-body -->