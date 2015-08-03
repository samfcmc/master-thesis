'use strict';

(function(module) {

  var name = 'MuseumResource'

  module.exports = function(app) {
    app.factory(name, ['$resource', '$rootScope', function($resource, $rootScope) {
      var baseUrl = 'http://api.thewalters.org/v1';
      var collectionsUrl = baseUrl + '/collections'

      return $resource(baseUrl,
        {apiKey: $rootScope.apiInfo.apiKey},
        {
          'collections': {
            url: collectionsUrl,
            method: 'GET'
          }
        }
      );
    }]);

    return name;

  };

}(module));
