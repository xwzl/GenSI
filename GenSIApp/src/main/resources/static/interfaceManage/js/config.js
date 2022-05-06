/**
 * INSPINIA - Responsive Admin Theme
 * Copyright 2015 Webapplayers.com
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */

function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {

    $urlRouterProvider.otherwise("/layouts");
//    $urlRouterProvider.otherwise("/login");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $stateProvider
        .state('manage', {
            abstract: true,
            url: "/manage",
            templateUrl: "views/common/content.html",
            data: {pageTitle: 'Ftoul'}
        }).state('manage.interface', {
        url: "/interface",
        templateUrl: "views/pages/interface.html",
        controller: 'PageController',
        controllerAs: 'updatePrice',
        data: {pageTitle: 'Ftoul'}
//    ,resolve: {
//            loadPlugin: function ($ocLazyLoad) {
//                return $ocLazyLoad.load([
//                    {
//                    	files: ['css/plugins/datepicker/datepicker3.css','js/plugins/datepicker/bootstrap-datepicker.js']
//                    }
//                ]);
//            }
//        }
    }).state('manage.history', {
        url: "/history",
        templateUrl: "views/pages/history.html",
        controller: 'PageController',
        controllerAs: 'history',
        data: {pageTitle: '调用查询'}
    }).state('manage.transIdLogFile', {
        url: "/transIdLogFile",
        templateUrl: "views/pages/transIdLogFile.html",
        controller: 'PageController',
        controllerAs: 'transIdLogFile',
        data: {pageTitle: 'transId日志'}
    }).state('manage.druidManage', {
        url: "/druidManage",
        templateUrl: "views/pages/druidManage.html",
        controller: 'PageController',
        controllerAs: 'transIdLogFile',
        data: {pageTitle: '数据库连接池监控'}
    }).state('layouts', {
        url: "/layouts",
        templateUrl: "views/layouts.html",
        controller: 'PageController',
        controllerAs: 'layouts',
        data: {pageTitle: 'Ftoul'},
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                return $ocLazyLoad.load([
                    {
                        files: ['css/plugins/iCheck/custom.css', 'js/plugins/iCheck/icheck.min.js']
                    }
                ]);
            }
        }
    });
}

/**
 * 说明：
 * $http post支持消息体传参的相关配置
 * 对中文参数使用了 encodeURIComponent 转码utf-8 后台要用URLDecoder.decode解码
 * 使用.then(function(data){})封装回调函数,返回的内容在data.data中 data还有一些响应状态的参数。使用.success(function(data){})封装，返回的内容就在data中。
 * ���HTTP post������촫�ν��ܲ���������  $http.post("module4OneAction/exportRegisterInfo.do",{starttime:'2015'}).then(function(data){}, function(){});
 *                               $http.post(url,���������顿).then(function(data){�ص�����})
 */
var httpconfig = function ($httpProvider) {
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

    $httpProvider.defaults.transformRequest = [function (data) {
        var param = function (obj) {
            var query = '';
            var name, value, fullSubName, subName, subValue, innerObj, i;

            for (name in obj) {
                value = obj[name];

                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value !== undefined && value !== null) {
                    query += encodeURIComponent(encodeURIComponent(name)) + '='
                        + encodeURIComponent(encodeURIComponent(value)) + '&';
                }
            }

            return query.length ? query.substr(0, query.length - 1) : query;
        };

        return angular.isObject(data) && String(data) !== '[object File]'
            ? param(data)
            : data;
    }];
};

angular
    .module('inspinia')
    .config(config)
    .config(httpconfig)
    .run(function ($rootScope, $state) {
        $rootScope.$state = $state;
    });



