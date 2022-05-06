//过滤器
var nullFilter=function(){
	return function(str){
		if(str==undefined||str==null||str.trim()=='null'||str.trim()==''){
			return '';
		}
		return str;
	}
}
//红包类型过滤器
var briberyType=function(){
	return  function(str){
		switch (str) {
		case '1':
            return "首次充值";
		case '2':
            return "首次投资";
		case '3':
            return "蜂狂合伙人奖励";
		case '4':
            return "大转盘抽奖";
		case '5':
            return "开通汇付";
		case '6':
            return "蜂币兑换";
		case '8':
            return "平台赠送";
		case '9':
            return "心动礼包";
		case '11':
			return "抢红包";
		case '12':
			return "抢红包";
		case '13':
			return "抢红包";
		case '14':
            return "抢红包";
		case '15':
            return "邀请好友奖励";
		case '16':
            return "还款赠送";
		case '18':
            return "518活动";
		case '19':
            return "端午礼包";
		case '20':
            return "理财季第一期";
		case '21':
            return "充值赠送";
		case '22':
            return "微信九宫格";
		case '23':
            return "砸金蛋";
		case '24':
            return "秋收活动";
		case '25':
            return "中秋活动";
		case '26':
            return "国庆活动";
		case '27':
            return "生日福利";
		case '28':
            return "组合理财活动";
		case '29':
            return "万圣节活动";
		case '30':
            return "网贷天眼活动";
		case '31':
            return "双11活动";
		default:
			return  str;
		}
	}
}

//理财基金获取过滤器
var fundget=function(){
	return function(str){
		 switch (str) {
		 case '0':
			 return '注册赠送';
		 case '1':
			 return '管理员赠送';
		 case '2':
			 return '还款赠送';
		 case '3':
			 return '抢理财基金';
		 case '4':
			 return '首次投资赠送';
		 case '5':
			 return '毕业季理财活动赠送';
		 case '6':
			 return '微信九宫格抽奖';
		 case '7':
			 return '首充赠送';
		 case '8':
			 return '普通充值赠送';
		 case '9':
			 return '砸金蛋活动';
		 default:
			return  str;
		 }
	}
}

//是否新手标
var isnew=function(){
	return function(str){
		 switch (str) {
		 case '0':
			 return '普通标';
		 case '1':
			 return '新手标';			
		 default:
			return  str;
		 }
	}
}

//是否投资成功
var isok=function(){
	return function(str){
		 switch (str) {
		 case '0':
			 return '投资未成功';
		 case '1':
			 return '投资成功';			
		 default:
			return  "其他";
		 }
	}
}

//是否有收益
var ishave=function(){
	return function(str){
		 switch (str) {
		 case "true":
			 return '无收益';
		 case "false":
			 return '有收益';			
		 default:
			return  "其他";
		 }
	}
}


//充值成功与否
var isRecharge=function(){
	return function(str){
		 switch (str) {
		 case "2":
			 return '充值失败';
		 case "1":
			 return '充值成功';			
		 default:
			return  "其他";
		 }
	}
}

//PC端/移动端登陆
var islogon=function(){
	return function(str){
		 switch (str) {
		 case "2":
			 return 'PC端登陆';
		 case "320":
			 return '移动端登陆';			
		 default:
			return  "其他";
		 }
	}
}

//时间单位：年月日
var isperiod=function(){
	return function(str){
		 switch (str) {
		 case "0":
			 return '月';
		 case "1":
			 return '日';	
		 case "-1":
			 return '年';
		 default:
			return  '';
		 }
	}
}


//用户注册使用的终端类型
var islogin=function(){
	return function(str){
		 switch (str) {
		 case "0":
			 return 'PC端注册';
		 case "3":
			 return '移动端注册';	
		 case "1":
			 return '安卓注册';
		 case "2":
			 return 'IOS注册';
		 case "-1":
			 return '未知设备';
		 default:
			return  "其他";
		 }
	}
}

//类型过滤器
var getsource=function(){
	return  function(str){
		switch (str) {
		case '1':
            return "开通汇付";
		case '2':
            return "首冲";
		case '3':
            return "手动赠送";
		case '4':
            return "小贷通";
		case '5':
            return "V计划";
		case '6':
            return "A计划";
		case '7':
            return "B计划";
		case '8':
            return "C计划";
		case '9':
            return "民生通";
		case '10':
            return "政信通";
		case '11':
            return "企信通";
		case '14':
            return "D计划";
		case '20':
            return "疯狂合伙人邀请好友赠送";	
		default:
			return  str;
		}
	}
}

var getActivity_type = function(){
	return function(str){
		var res = "";
		switch(str){
		 	case "1":
	            res = "天天登录抽奖";
	            break;
			case "2":
				res = "15/45天活动";
				break;
			case "3":
				res = "30/60天活动";
				break;
			case "4":
				res = "端午节活动";
				break;
			case "10":
				res = "7天活动";
				break;
			case "11":
				res = "15天活动";
				break;
			case "12":
				res = "21天活动";
				break;
			case "13":
				res = "30天活动";
				break;
			case "14":
				res = "微信九宫格抽奖活动";
				break;
			case "17":
				res = "万圣节活动";
				break;
			case "19":
				res = "铜蛋";
				break;
			case "20":
				res = "银蛋";
				break;
			case "21":
				res = "金蛋";
				break;
			default:
				res =  str;
				break;
		 }
	 return res;
	}
}

var getActivity_login_type = function(){
	return function(str){
		 var res = "";
		 switch(str){
			case "2":
				res = "pc登陆";
				break;
			case "320":
				res = "微信登陆";
				break;
			default:
				res =  str;
				break;
		 }
		 return res;
	}
}


angular
    .module('inspinia')
    .filter('briberyType',briberyType)
    .filter('nullFilter',nullFilter)
    .filter('fundget',fundget)   
    .filter('isnew',isnew) 
    .filter('isok',isok)     
    .filter('getsource',getsource) 
    .filter('ishave',ishave) 
    .filter('islogon',islogon) 
    .filter('islogin',islogin) 
    .filter('isperiod',isperiod) 
    .filter('getActivity_type',getActivity_type) 
    .filter('getActivity_login_type',getActivity_login_type) 
    .filter('isRecharge',isRecharge) ;
