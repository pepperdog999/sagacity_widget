// JavaScript Document
$(function(){
	/*
	*
		性别选择
	*
	*/
	g.setPageShow($("#sexInput"));
	/*
	*
		民族选择
	*
	*/
	g.setPageShow($("#nationInput"));
	/*
	*
		性别选择列表事件
	*
	*/
	//g.listBindEvent($("#sex_list"));
	/*
	*
		民族选择列表事件
	*
	*/
	//g.listBindEvent($("#nation_list"));
	
	/*
	*
		显示后的列表 点击返回 隐藏选择列表
	*
	*/
	g.goBack($("#sexBack"));
	g.goBack($("#nationBack"));
});


var g ={};

g.showObject = new Object();
g.clickObject = new Object();
g.loadingObject = $("#loading_box");

/*点击输入框  显示选择类型*/
g.setPageShow = function(obj){
	var $this = this;
	var toId = $(obj).attr("datas");
	var bodyWidth = $("body").width();
	$(obj).on("click", function(){
			$this.loadingObject.show();
			$this.showObject = $("#"+toId);
			$this.clickObject = $(this);
			//console.log($this.showObject);
			$this.showObject.ready(function(){
				$this.loadingObject.hide();
				$this.overFlowHidden();
				$this.showObject.css({left:Number(bodyWidth)+10}).show().animate({left:0},"fast", function(){$this.overFlowShow();});
			});
	});
}

/*列表事件*/
g.listBindEvent = function(obj){
	var $this = this;
	$(obj).children().bind("click", function(){
		$this.clickObject.val($(this).html());
		$this.clickObject.attr("data-id", $(this).attr("id"));
		$this.setPageHide();
	});
}

/*点击输入框  隐藏选择类型*/
g.setPageHide = function(){
	var $this = this;
	var obj = $this.showObject;
	var bodyWidth = $("body").width();
	if(obj.is(":visible")){
		$this.overFlowHidden();
		obj.animate({left:Number(bodyWidth)+10},"fast", function(){$(this).hide();obj="";$this.overFlowShow();});
	}
}

/*点击返回*/
g.goBack = function(obj){
	var $this = this;
	$(obj).on("click", function(){$this.setPageHide();});
}

/*设置滚动事件内 横向不出现滚动条*/
g.overFlowHidden = function(){
	$("body").css({"overflow-x":"hidden"});
}
/*设置滚动事件内 解锁横向出现滚动条*/
g.overFlowShow = function(){
	$("body").css({"overflow-x":"auto"});
}

/*清除列表点击后的样式*/
g.setBackground = function(obj){
	$(obj).css({"background-color": "#fff"});
}