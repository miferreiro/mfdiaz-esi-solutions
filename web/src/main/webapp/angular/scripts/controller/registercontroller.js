// retrieve the current app
var app = angular.module('socialnetApp');

// add a new controller
app.controller('RegisterController', ['userService', '$scope', function(userService, $scope){

	var picture = "";

	$scope.register = function() {

		var role = "user";

		var User = {
			login : $scope.login,
			password : $scope.password,
			role : role,
			name : $scope.name,
			picture : picture
		};

		userService.registerUser(User, _success, _error);
	};

	function _success(response) {
		_clearFormData();
		$("#responseRegister").css("color", "#00ff00"); 
		$("#responseRegister").show(); 
		$scope.response = "User Created!";
    }

    function _error(response) {
		$("#responseRegister").css("color", "#ff0000"); 
		$("#responseRegister").show(); 
		$scope.response = "User is already created!";
    }

	//Clear the form
	function _clearFormData() {
		$scope.login = "";
		$scope.password = "";
		$scope.name = "";
		$scope.picture = "";
	};

	$scope.change = function() {

		console.log("change ");

		var file    = document.querySelector('input[type=file]').files[0];


		var reader  = new FileReader();

		reader.onloadend = function () {
			picture= reader.result;
		}

		if (file) {
			reader.readAsDataURL(file);
		} else {
			picture = "mal";
		}

		console.log("file " + file);
		console.log("picture" + picture);
	};
}]);
