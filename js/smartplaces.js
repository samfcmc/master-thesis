window.SmartPlaces = {
  tagFound: function() {
    throw "You need to define a call back for when a tag is found. " +
      "Call SmartPlaces.onTagFound(callback)";
  },
  hello: function() {
    alert("hello, just testing");
  },
  onTagFound: function(callback) {
    // The mobile app will call tagFound callback
    this.tagFound = callback;
  }
};
