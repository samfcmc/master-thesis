'use strict';

/*
 * Controllers
 */

(function(module) {

  module.exports = function(app, resources) {
    var museumResource = resources.Museum;
    var collections = require('./collections')(app, museumResource);

    return {
      Collections: collections
    };
  };

}(module));
