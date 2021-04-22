// retrieve the current app
var app = angular.module('socialnetApp');

// add a new controller
app.controller('PostController', 
['postService', '$scope', '$rootScope', '$cookieStore', '$location',
function(postService, $scope, $rootScope, $cookieStore, $location){

    $rootScope.globals = $cookieStore.get('globals');
    $scope.login = $rootScope.globals.currentUser.login;
    $rootScope.avatar = $rootScope.avatar;

    $scope.createLink = function() {
        var id = $scope.id;
        var url = $scope.url;
        var login = $scope.login;

        postService.createLink(id, url, login, _success, _error);
    };

    $scope.createPhoto = function() {
        var id = $scope.id;
        var content = $scope.content;
        var login = $scope.login;

        postService.createPhoto(id, content, login, _success, _error);
    };

    $scope.createVideo = function() {
        var id = $scope.id;
        var duration = $scope.duration;
        var login = $scope.login;

        postService.createVideo(id, duration, login, _success, _error);
    };

    function _success(response) {
        _clearFormData();
        $location.path('/wall'); 
        $("#response").css("color", "#00ff00"); 
		$("#response").show(); 
        $scope.response = "Post created!";    
    }
    
    function _error(response) {  
        $("#responsePostCreation").css("color", "#ff0000"); 
		$("#responsePostCreation").show(); 
		$scope.response = "Post is already created!"; 
    }

    //Clear the forms
	function _clearFormData() {
		$scope.id = "";
		$scope.url = "";
		$scope.content = "";
		$scope.duration = "";
	};
}]);