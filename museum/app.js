var app = require('./server');

var port = process.env.PORT || 9000;

app.listen(port, function() {
  console.log('App up and running');
  console.log('Listening on port ' + port);
});
