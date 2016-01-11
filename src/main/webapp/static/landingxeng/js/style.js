$(document).ready(function(){
	
	// call tooltip
	$('.cdt-tooltip').tooltip();
	
	//menu top scroll
	$(window).scroll(function(){
		if($(this).scrollTop() >= 67){
			$('.navbar-default').addClass("navigator-fixed");
			$('body').addClass("padding-header");
		}
		if($(this).scrollTop() < 67){
			$('.navbar-default').removeClass("navigator-fixed");	
			$('body').removeClass("padding-header");
		}
	});
	
	// menu click
	$('a', $('.navbar-nav li')).click(function() {
		var parent = $(this).parent();
		$('li', $('.navbar-nav')).removeClass('active');
		$(parent).addClass('active');
		var id = $(this).attr('href');
		var navHeight = $(".navbar-default").height();
		$("html, body").animate({scrollTop: $(id).offset().top - navHeight}, 700);
		return false;
	});
	
});

(function() {
	
	var viewport = $(window).width();
    if (viewport > 768) {
		
		$('.bs-caption h3').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp');   }});
		$('.bs-caption h4').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp delayp2');   }});
		$('.bs-list').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp delayp4');   }});
		$('.bs-card').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated bounceIn delayp6');   }});
		$('.bg-red h3').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp');   }});
		$('.bg-red p').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp delayp2');   }});
		$('.bc-img').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp delayp6');   }});
		$('.bc-more').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated bounceIn');   }});
		$('.bg-blue h3').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp');   }});
		$('.bb-list').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInRight delayp2');   }});
		$('.howto-title').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp');   }});
		$('.howto-list').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInLeft delayp2');   }});
		$('.support-title').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp');   }});
		$('.support-desc').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp delayp2');   }});
		$('.support-list').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInRight delayp4');   }});
		$('.footer-more').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated fadeInUp');   }});
		$('.footer-button').one('inview', function (event, visible) {
    	if (visible) {   $(this).addClass('animated bounceIn delayp2');   }});
		
	}
	
})(jQuery);


