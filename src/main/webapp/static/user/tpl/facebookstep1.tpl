<div class="modal-body">
                        <div class="fb-post">
                            <div class="fp-steps">
                                <div class="fp-step step1 active">
                                    <div class="fb-number">1</div>
                                    <div class="fb-name">Nói Gì đó về sản phẩm này</div>
                                    <div class="fb-bullet"></div>
                                </div><!-- fp-step -->
                                <div class="fp-step step2">
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
                            <% $.each(data, function () { %> 
                            <div class="fp-entry">
                                <div class="fe-title"><i class="fa fa-pencil-square"></i>Viết bài</div>
                                <textarea name="message" rows="4" class="form-control" placeholder="Nói gì đó về bài đăng này..."></textarea>
                                <div class="grid">
                                    <div class="img"><a href="#"><img src="<%= this.images[0] %>" alt="img" /></a></div>
                                    <div class="g-content">
                                        <div class="g-row">
                                            <%= this.name %>
                                        </div>
                                        <div class="g-row">
                                            <%= this.detail %>
                                        </div>
                                        <div class="g-row">
                                            <span class="text-uppercase">Chodientu.vn</span>
                                        </div>
                                    </div>
                                </div><!-- grid -->
                            </div><!-- fp-entry -->
                            <% }); %>
                        </div><!-- fp-post --> 
                    </div><!-- end modal-body -->