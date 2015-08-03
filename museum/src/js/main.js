'use strict';

/*
 * Main: Entry point
 */

(function(require, angular) {

  var app = angular.module('app', ['ui.router', 'ngResource']);

  var resources = require('./resources')(app);
  var controllers = require('./controllers')(app, resources);
  require('./routes')(app, controllers);

  app.run(['$rootScope', '$http', function($rootScope, $http) {
    $http.get('apiInfo').success(function(response) {
      $rootScope.apiInfo = response;
    })
  }]);

}(require, angular));
