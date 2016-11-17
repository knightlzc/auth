$(function() {

	$("#side-menu a").click(function(){
		var url = $(this).attr("url");
		var hasChild = $(this).attr("hasChild");
		if( null != url && "" != url && hasChild=="false" ){
			loadRightPage (url);
		}
	});
	    
});

function loadRightPage (url) {
	$('#page-wrapper').load(url);
};

function loadRightPageWithParams(url,params) {
	$('#page-wrapper').load(url,params,function(){
		
	});
};
