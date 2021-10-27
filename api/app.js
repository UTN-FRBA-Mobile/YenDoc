const createError = require('http-errors');
const express = require('express');
const path = require('path');
const bodyparser = require('body-parser')
/*var cookieParser = require('cookie-parser');
var logger = require('morgan');*/

const indexRouter = require('./routes/index');
const personasRouter = require('./routes/personas');
const pacientesRouter = require('./routes/pacientes');

const app = express();

app.use(bodyparser.json());
app.use(bodyparser.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/personas', personasRouter);
app.use('/pacientes', pacientesRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  console.log(err);
  res.json({'ERROR': '500'});
});

app.listen (3001, () => {
  console.log("Escuchando en puerto 3001");
});

module.exports = app;
