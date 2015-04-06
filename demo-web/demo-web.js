CheckIns = new Mongo.Collection('checkIns');

if (Meteor.isClient) {

  // Material design init
  Template.client.rendered = function() {
    //$.material.init();
  }

  Template.checkIns.helpers({
    checkIns: function () {
      return CheckIns.find({});
    },
    createdMoment: function() {
      console.log(this);
      return 'this._id';
    }
  });

  Template.checkIns.events({
    'click .delete': function() {
      CheckIns.remove({'_id': this._id});
    }
  });

  Template.client.events({
    'click #clientButton': function (event) {
      var table = this.table;
      if(table) {
        $('#confirmation').modal('show')
        var created = new Date();
        var toInsert = {table: table, created: created};
        CheckIns.insert(toInsert);
      }
      else {
        throw "No table number";
      }
    }
  });

  Template.client.helpers({
    object: function() {
      return window.object;
    }
  });

}

if (Meteor.isServer) {
  Meteor.startup(function () {

  });
}

/*
 * Routing
 */
Router.route('/', function() {
  this.render('client', {
    data: function() {
      var table = this.params.query.table;
      return {table: table};
    }
  });
});

Router.route('/checkIns');
