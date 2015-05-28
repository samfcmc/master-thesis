CheckIns = new Mongo.Collection('checkIns');
Orders = new Mongo.Collection('orders');

if (Meteor.isClient) {

  var SmartPlaceConfiguration = false;

  Meteor.startup(function() {

    Meteor.call('parse', function(error, result) {
      if(error) {
        throw error;
      }
      else {
        var parse = JSON.parse(result);
        var appId = parse.appId;
        var jsKey = parse.jsKey;
        Parse.initialize(appId, jsKey);
        console.log('Parse initialized');
        SmartPlaceConfiguration = Parse.Object.extend('SmartPlacesConfiguration',
          {});
      }
    });
  });

  // Material design init
  Template.client.rendered = function() {
    //$.material.init();
  }

  Template.checkIns.helpers({
    checkIns: function () {
      return CheckIns.find({}, {sort: {created: -1}});
    },
  });

  Template.check.helpers({
    createdMoment: function() {
      return moment(this.created).fromNow();
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
    },

    'click #menuButton': function (event) {
      var configurationId = this.smartPlaceConfigurationId;
      console.log(configurationId);

      if(configurationId) {
        var query = new Parse.Query(SmartPlaceConfiguration);
        query.get(configurationId, {
          success: function(configuration) {
            console.log('Success');
            console.log(configuration);
            var object = configuration.get('object');
            console.log(object);
            var menu = object.menu
            console.log(menu);
            if(menu) {
              Session.set('menu', menu);
            }
            //
          },
          error: function (object, error) {
            console.log(error);
          }
        });
      }
      else {
        throw "Configuration id not defined";
      }
    }
  });

  Template.client.helpers({
    menu: function() {
      return Session.get('menu');
    }
  });

  Template.item.events({
    "click .order": function(event, template){
      console.log(this);
      var table = Session.get('table');
      console.log(table);
      var order = this;
      order.table = table;
      order.created = new Date();
      Orders.insert(order);
      $('#oderConfirmation').modal('show');
    }
  });

  Template.order.helpers({
    createdMoment: function() {
      return moment(this.created).fromNow();
    }
  });

  Template.orders.helpers({
    orders: function() {
      return Orders.find({}, {sort: {created: -1}});
    }
  });

  Template.orders.events({
    "click .delete": function(event, template){
       Orders.remove({'_id': this._id});
    }
  });

  Template.category.events({
    'click #categoryLink': function(event) {
      console.log(this);
    },
    'click #orderButton': function(event) {
      console.log(this);
      var table = Session.get('table');
      console.log(table);
      var order = this;
      order.table = table;
      order.created = new Date();
      Orders.insert(order);
      $('#oderConfirmation').modal('show');
    }
  });

}

if (Meteor.isServer) {
  var parse = {};
  Meteor.startup(function () {
    parse = Assets.getText("parse.json");
  });

  Meteor.methods({
    parse: function(){
       return parse;
    }
  });
}

/*
 * Routing
 */
Router.route('/', function() {
  this.render('client', {
    data: function() {
      var table = this.params.query.table;
      if(table) {
        Session.set('table', table);
      }
      var smartPlaceConfigurationId = this.params.query.smartPlaceConfiguration;
      return {table: table,
        smartPlaceConfigurationId: smartPlaceConfigurationId};
    }
  });
});

Router.route('/checkIns');

Router.route('/orders');
