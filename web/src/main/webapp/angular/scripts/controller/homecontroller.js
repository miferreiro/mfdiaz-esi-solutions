// retrieve the current app
var app = angular.module('socialnetApp');

// add a new controller
app.controller('HomeController', 
['userService', '$scope', '$location', '$rootScope',
function(userService, $scope, $location, $rootScope){
    
  userService.clearCredentials();
  $rootScope.currentLogin = "";
  var login = "";
  var password = "";
  $rootScope.avatar = "";

  $scope.loginFunction = function() {
    login = $scope.login;
    password = $scope.password;
    userService.checkLogin(login, password, _success, _error);
  };

  function _success(response) {
    _clearFormData();
    var user = response.data.filter(function(user) {
      return user.login == login;
    });
    $rootScope.avatar = user[0].picture;
    userService.setCredentials(login, password);
    $location.path('/wall');
  }

  function _error(response) {

    console.log("_error Response ", response);

    if (response.status === 401) {
      $("#responseLogin").css("color", "#ff0000"); 
      $("#responseLogin").show(); 
      $scope.response = "Not logged 401" + response.statusText;
    } else if (response.status === 403) {
      $("#responseLogin").css("color", "#ff0000"); 
      $("#responseLogin").show(); 
      $scope.response = "Not logged 403" + response.statusText;
    } else {
      $("#responseLogin").css("color", "#ff0000"); 
      $("#responseLogin").show(); 
      $scope.response = "Not logged " + response.statusText;
    }
  }

  $rootScope.logout = function() {
    userService.clearCredentials();
    login = "";
    password = "";
    $location.path('/');
  };

  //Clear the form
	function _clearFormData() {
		$scope.login = "";
		$scope.password = "";
	};
}]);
