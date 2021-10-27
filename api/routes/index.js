var express = require('express');
//var bd = require('../database/bd');
const sqlite = require('../database/sqlite');
var router = express.Router();


/* GET home page. */
router.route('/')
.get(async (req, res, next) => {
  res.render('../public/index.html', { title: 'Inicio' }).send({
    error: false,
    codigo: 200,
    mensaje: 'Recibido GET'
  });
});


module.exports = router;
