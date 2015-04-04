CheckIns = new Mongo.Collection('checkIns');

if (Meteor.isClient) {

  window.onObjectFound = function(obj) {
    console.log(obj);
    window.object = obj;
  }

  Template.checkIns.helpers({
    checkIns: function () {
      return CheckIns.find({});
    }
  });

  Template.checkIns.events({
    'click .delete': function() {
      CheckIns.remove({'_id': this._id});
    }
  });

  Template.client.events({
    'click #clientButton': function () {
      CheckIns.insert({table: 3})
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
      var obj = this.params.query.obj;
      var str = JSON.parse(obj);
      console.log(str);
      return {objectFound: obj};
    }
  });
});

Router.route('/checkIns');
