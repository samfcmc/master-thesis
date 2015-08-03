/*
 * Resources: CRUD operations
 */

(function(module) {

  module.exports = function(app) {

    var Museum = require('./museum')(app);

    return {
      Museum: Museum
    };

  };

}(module));
