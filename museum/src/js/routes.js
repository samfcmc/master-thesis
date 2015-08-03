'use strict';

/*
 * Routes
 */
(function(module) {

  module.exports = function(app, controllers) {
    app.config(['$stateProvider', '$urlRouterProvider',
      function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/collections");

        $stateProvider.state('collections', {
          url: '/collections',
          templateUrl: '/views/collections.html',
          controller: controllers.Collections
        });

        $stateProvider.state('objects', {
          url: '/objects',
          templateUrl: '/views/objects.html'
        });
    }]);

  };

}(module));
