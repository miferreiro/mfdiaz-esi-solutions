// retrieve the current app
var app = angular.module('socialnetApp');

// add a new controller
app.controller('WallController', 
['postService', '$scope', '$rootScope', '$cookieStore',
function(postService, $scope, $rootScope, $cookieStore){
    
    $rootScope.globals = $cookieStore.get('globals')
    $scope.login = $rootScope.globals.currentUser.login;
    $rootScope.currentLogin = $rootScope.globals.currentUser.login;
    var authdata = $rootScope.globals.currentUser.authdata;
    $rootScope.avatar = $rootScope.avatar;
    $scope.posts = [];

    wall();

    function wall() {
        var login = $scope.login;
        postService.getWall(login, successWall, errorWall);
    };

    function successWall(posts) {
        $scope.posts = posts.data;        
    };

    function errorWall(response) {
        $("#response").css("color", "#ff0000"); 
		$("#response").show(); 
        $scope.response = "Error wall";   
    };

    $scope.like = function(idPost) {
        var login = $scope.login;
        postService.likePost(idPost, login, successLike, errorLike);         
    };

    function successLike(response) {
        wall();
    };

    function errorLike(response) {
        $("#response").css("color", "#ff0000"); 
		$("#response").show(); 
        $scope.response = "Error like";   
    };

    $scope.liked = function(usersLike, currentUser) {
        return usersLike.some(user => user.login === currentUser);
    };

}]);
