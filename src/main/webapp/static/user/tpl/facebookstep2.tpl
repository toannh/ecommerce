                   <div class="modal-body">
                        <div class="fb-post">
                            <div class="fp-steps">
                                <div class="fp-step step1">
                                    <div class="fb-number">1</div>
                                    <div class="fb-name">Nói Gì đó về sản phẩm này</div>
                                    <div class="fb-bullet"></div>
                                </div><!-- fp-step -->
                                <div class="fp-step step2 active">
                                    <div class="fb-number">2</div>
                                    <div class="fb-name">Chọn nhóm Bạn muốn đăng</div>
                                    <div class="fb-bullet"></div>
                                </div><!-- fp-step -->
                                <div class="fp-step step3">
                                    <div class="fb-number">3</div>
                                    <div class="fb-name">Hoàn thành</div>
                                    <div class="fb-bullet"></div>
                                </div><!-- fp-step -->
                                <div class="fp-help">
                                    <a href="#"><i class="fa fa-question"></i>Hướng dẫn</a>
                                </div><!-- fp-help -->
                            </div><!-- fp-steps -->
                            <div class="check-item">
                                <div class="checkbox"><label><input name="checkall" type="checkbox" value="" /> Nhóm group fb của bạn</label></div>
                            </div>
                            <div class="fp-group">
                                <% $.each(data, function () { %> 
                                <div class="check-item">
                                	<div class="checkbox"><label><input for="grFace" type="checkbox" value="<%= this.id %>" /> <%= this.name %></label></div>
                                </div>
                                
                                <% }); %>
                            </div><!-- fp-group -->
                            <div class="fp-view">
                            	<div class="row">
                                	<div class="col-md-4">
                                    	<p>Bạn đã chọn đăng: <b><span class="text-danger" for="count">0</span> nhóm.</b></p>
                                        <p>Tài khoản xèng hiện có: <b><span class="text-danger"><%= xeng %></span> xèng.</b></p>
                                    </div><!-- col -->
                                    <div class="col-md-4">
                                    	<p>Số xèng phải trả cho 1 nhóm: <b><span class="text-danger">20</span> xèng.</b></p>
                                    </div><!-- col -->
                                    <div class="col-md-4 text-right">
                                    	<p>Tổng thanh toán: <b><span class="text-danger" for="total">200</span> xèng.</b></p>
                                        <p><a class="fp-pushxeng" href="https://www.chodientu.vn/user/tai-khoan-xeng.html">Nạp thêm xèng<i class="fa fa-refresh"></i></a></p>
                                    </div><!-- col -->
                                </div><!-- row -->
                            </div><!-- fp-view -->
                        </div><!-- fp-post --> 
                    </div><!-- end modal-body 
                    <div class="modal-footer">
                    	<button class="btn btn-danger" type="button">Quay về</button>
                        <button class="btn btn-primary" type="button"  data-target="#Modalfbstep3" data-toggle="modal">Thực hiện</button>
                    </div>-->