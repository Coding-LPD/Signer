var nsp;
var namespace = '/sign';
var events = {
  sign: 'sign',
  notice: 'notice'
};

function setSocket(io) {
  
  nsp = io.of(namespace);

  nsp.on('connect', function (client) {
    console.log('client connect');
    listen(client);
  });

}

function listen(client) {
  client.on('disconnect', disconnect);
  client.on('error', handleError);
}

function disconnect() {
  console.log('client disconnect');
}

function handleError(err) {
  console.log('socket error: ' + err);
}

function send(event, data) {
  nsp.emit(event, data);
}

exports.setSocket = setSocket;
exports.events = events;
exports.send = send;