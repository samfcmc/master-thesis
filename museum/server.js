var express = require('express');
var app = express();

app.get('/', function(req, res) {
  res.sendFile(__dirname + '/build/views/index.html');
});

app.get('/apiInfo', function(req, res) {
  res.sendFile(__dirname + '/walters.json');
});

app.use('/static', express.static(__dirname + '/build/bower_components'));
app.use('/js', express.static(__dirname + '/build/js'));
app.use('/views', express.static(__dirname + '/build/views'));

module.exports = app;
