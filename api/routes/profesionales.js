const express = require('express');
const {sqlite} = require('../database/sqlite');
const router = express.Router();

router.route('/')
    .get(async (req, res, next) => {
      sqlite.all("SELECT * FROM profesionales", [], (err, rows) => {
        if(err) {
          res.status(400).json(err.message);
          throw err;
        }
        console.log("GET /profesionales");
        res.status(200).json(rows);
      });
    });

router.route('/:id')
    .get(async (req, res, next) => {
        const {id} = req.params;
        sqlite.get("SELECT * FROM profesionales WHERE profesional_id = ?", [id], (err, rows) => {
            if(err) {
                res.status(400).json(err.message);
                throw err;
            }
            console.log("GET /profesionales/" + id);
            res.status(200).json(rows);
        });
    });

router.route('/:id/proximasVisitas')
    .get(async (req, res, next) => {
        const { id } = req.params;
        const sql = `SELECT visita_id, fecha, nombre as paciente, 
            direccion_calle || ' ' || CAST(direccion_numero as TEXT) as direccionPaciente,
            estado as estado_visita
            FROM visitas 
            JOIN pacientes ON visitas.paciente_id = pacientes.paciente_id  
            WHERE profesional_id = ?
            ORDER BY visita_id ASC, estado_visita DESC`;
        sqlite.all(sql, [id], (err, rows) => {
            if(err) {
                res.status(400).json(err.message);
                throw err;
            }
            console.log("GET /profesionales/"+id+"/proximasVisitas");
            res.status(200).json(rows);
        });
    });

router.route('/getProfesionalByUser/:user')
    .get(async (req, res, next) => {
        const { user } = req.params;
        const sql = `SELECT * FROM profesionales WHERE usuario = ? ORDER BY 1 ASC LIMIT 1`;
        sqlite.get(sql, [user], (err, rows) => {
            if(err) {
                res.status(400).json(err.message);
                throw err;
            }
            console.log("GET /profesionales/getProfesionalByUser/" + user);
            res.status(200).json(rows);
        });
    });

router.route('/login')
    .post(async (req, res, next) => {
        const {usuario, contrasenia} = req.body;
        sqlite.get("SELECT * FROM profesionales WHERE usuario = ? AND contrasenia = ? LIMIT 1", [usuario, contrasenia], (err, rows) => {
            if(err) {
                res.status(400).json(err.message);
                throw err;
            }
            console.log("POST /login/");
            res.status(200).json(rows);
        });
    });

module.exports = router;
