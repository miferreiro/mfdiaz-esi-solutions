// create the angularjs application
'use strict';
var app = angular.module('socialnetApp', ['ngRoute', 'ngCookies']);

app.config(['$routeProvider', function($routeProvider) {

    $routeProvider
    .when('/post/link',{
        templateUrl: 'views/post/link.view.html',
        controller: 'PostController'
    })
    .when('/post/photo',{
        templateUrl: 'views/post/photo.view.html',
        controller: 'PostController'
    })
    .when('/post/video',{
        templateUrl: 'views/post/video.view.html',
        controller: 'PostController'
    })
    .when('/wall',{
        templateUrl: 'views/wall/wall.view.html',
        controller: 'WallController'
    })
    .when('/register',{
        templateUrl: 'views/register/register.view.html',
        controller: 'RegisterController'
    })
    .when('/',{
        templateUrl: 'views/home.html',
        controller: 'HomeController'
    })
    .otherwise({
        redirectTo: '/'
    });
}])


.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore , $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; 
        }
 
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/' &&                 
                $location.path() !== '/register' && 
                !$rootScope.globals.currentUser) {
                $location.path('/');
            } 
        });
}]);

// app.config(function($httpProvider) {
//     $httpProvider.interceptors.push(function($q) {
//       return {
//         request: function(request) {
//             console.log("request" + request.status);
//             return request;
//           },
//         responseError: function(res) {
//             console.log("asdadasd");
//           /* This is the code that transforms the response. `res.data` is the
//            * response body */
//             if(res.status === 401) {
//                 console.log("401");
//             } else {
//                 console.log("Status " + res.status);
//             }

//             return $q.reject(res);
//         }
//       };
//     });
//   });