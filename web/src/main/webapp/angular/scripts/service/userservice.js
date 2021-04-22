'use strict';
angular.module('socialnetApp')
.factory('userService', ['$http', '$rootScope', '$cookieStore',
 function($http, $rootScope, $cookieStore){

	var service = {};
	
    service.registerUser = registerUser;
    service.checkLogin = checkLogin;
    service.setCredentials = setCredentials;
    service.clearCredentials = clearCredentials;
	
	return service;
		
    function registerUser(User, onSuccess, onFail) {

        var method = "POST";
        var url = "http://localhost:8080/web-0.0.1-SNAPSHOT/api/user";        
        
		$http({
            method : method,
            url : url,
            data : angular.toJson(User),
            headers : {
                'Content-Type' : 'application/json'
            }
        }).then( onSuccess, onFail );
    };

    function checkLogin(login, password, onSuccess, onFail) {

        var method = "GET";
        var url = "http://localhost:8080/web-0.0.1-SNAPSHOT/api/user";
        
        $http.defaults.headers.common['Authorization'] = 'Basic ' + btoa(login + ":" + password);
        // $http.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
		$http({
            method : method,
            url : url
        }).then(onSuccess, onFail );
    };

    function setCredentials (login, password) {
        var authdata = btoa(login + ':' + password);

        $rootScope.globals = {
            currentUser: {
                login: login,
                authdata: authdata
            }
        };

        $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; // jshint ignore:line
        $cookieStore.put('globals', $rootScope.globals);
    };

    function clearCredentials () {
        $rootScope.globals = {};
        $cookieStore.remove('globals');
        $http.defaults.headers.common['Authorization'] = 'Basic ';
    };
}]);
