CheckIns = new Mongo.Collection('checkIns');

if (Meteor.isClient) {

  Template.checkIns.helpers({
    checkIns: function () {
      return CheckIns.find({});
    }
  });

  Template.checkIns.events({
    'click .delete': function() {
      console.log(this);
      CheckIns.remove({'_id': this._id});
    }
  });

  Template.client.events({
    'click #clientButton': function (event) {
      var table = this.table;
      if(table) {
        CheckIns.insert({table: table});
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
