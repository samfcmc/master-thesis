'use strict';

/*
 * Controllers
 */

(function(module) {

  module.exports = function(app, resources) {
    var museumResource = resources.Museum;
    var collections = require('./collections')(app, museumResource);
    var objects = require('./objects')(app, museumResource);

    return {
      Collections: collections,
      Objects: objects
    };
  };

}(module));
