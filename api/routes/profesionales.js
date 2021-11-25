const express = require('express');
const {sqlite} = require('../database/sqlite');
const router = express.Router();

router.route('/')
    .get(async (req, res, next) => {
      sqlite.all("SELECT * FROM profesionales", [], (err, rows) => {
        if(err) {
          res.status(400).json({"error": err.message});
          throw err;
        }
        console.log("Solicitud realizada /profesionales");
        res.status(200).json(rows);
      });
    });

router.route('/:id')
    .get(async (req, res, next) => {
        const {id} = req.params;
        sqlite.get("SELECT * FROM profesionales WHERE profesional_id = ?", [id], (err, rows) => {
            if(err) {
                res.status(400).json({"error": err.message});
                throw err;
            }
            console.log("Solicitud realizada /profesionales/" + id);
            res.status(200).json(rows);
        });
    });

router.route('/:id/proximasVisitas')
    .get(async (req, res, next) => {
        const { id } = req.params;
        const sql = `SELECT visita_id, fecha, nombre as paciente, 
            direccion_calle || ' ' || CAST(direccion_numero as TEXT) as direccionPaciente,
            realizada as estado_visita
            FROM visitas 
            JOIN pacientes ON visitas.paciente_id = pacientes.paciente_id  
            WHERE profesional_id = ?`;
        sqlite.all(sql, [id], (err, rows) => {
            if(err) {
                res.status(400).json({"error": err.message});
                throw err;
            }
            console.log("Solicitud realizada /profesionales/"+id+"/proximasVisitas");
            res.status(200).json(rows);
        });
    });

module.exports = router;
