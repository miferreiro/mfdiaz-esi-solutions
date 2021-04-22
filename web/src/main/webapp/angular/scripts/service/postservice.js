angular.module('socialnetApp')
.factory('postService', ['$http', function($http){

    var service = {};

    service.getWall = getWall;
    service.likePost = likePost;
    service.createLink = createLink;
    service.createPhoto = createPhoto;
    service.createVideo = createVideo;

    return service;

    function getWall(login, onSuccess, onFail) {

        var method = "GET";
        var url = "http://localhost:8080/web-0.0.1-SNAPSHOT/api/user/" + login + "/wall";        

		$http({
            method : method,
            url : url
        }).then( onSuccess, onFail );
    };

    function likePost(idPost, login, onSuccess, onFail) {
        
        var method = "PUT";
        var url = "http://localhost:8080/web-0.0.1-SNAPSHOT/api/post/" + idPost + "/like/" + login;

		$http({
            method : method,
            url : url
        }).then( onSuccess, onFail );
    };

    function createLink(idPost, url, login, onSuccess, onFail) {

        var method = "POST";
        var urlHttp = "http://localhost:8080/web-0.0.1-SNAPSHOT/api/post/link/" + login;

        var post = {
            id : idPost,
            date : new Date(),
            user : {
                login : login
            },
            url : url
        };
        
		$http({
            method : method,
            url : urlHttp,
            data : angular.toJson(post),
            headers : {
                'Content-Type' : 'application/json'
            }
        }).then( onSuccess, onFail );
    };

    function createPhoto(idPost, content, login, onSuccess, onFail) {

        var method = "POST";
        var url = "http://localhost:8080/web-0.0.1-SNAPSHOT/api/post/photo/" + login;
        
        var post = {
            id : idPost,
            date : new Date(),
            user : {
                login : login
            },
            content : content
        };
        
		$http({
            method : method,
            url : url,
            data : angular.toJson(post),
            headers : {
                'Content-Type' : 'application/json'
            }
        }).then( onSuccess, onFail );
    };

    function createVideo(idPost, duration, login, onSuccess, onFail) {

        var method = "POST";
        var url = "http://localhost:8080/web-0.0.1-SNAPSHOT/api/post/video/" + login;

        var post = {
            id : idPost,
            date : new Date(),
            user : {
                login : login
            },
            duration : duration
        };
        
		$http({
            method : method,
            url : url,
            data : angular.toJson(post),
            headers : {
                'Content-Type' : 'application/json'
            }
        }).then( onSuccess, onFail );
    };
}]);