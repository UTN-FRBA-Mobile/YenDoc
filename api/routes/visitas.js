const express = require('express');
const {sqlite} = require('../database/sqlite');
const router = express.Router();

router.route('/')
    .get(async (req, res, next) => {
        sqlite.all("SELECT * FROM visitas", [], (err, rows) => {
            if(err) {
                res.status(400).json(err.message);
                throw err;
            }
            console.log("GET /visitas/");
            res.status(200).json(rows);
        });
    });

router.route('/:id')
    .get(async (req, res, next) => {
        const {id} = req.params;
        sqlite.get("SELECT * FROM visitas WHERE visita_id = ?", [id], (err, rows) => {
            if(err) {
                res.status(400).json(err.message);
                throw err;
            }
            console.log("GET /visitas/" + id);
            res.status(200).json(rows);
        });
    });

router.route('/estado')
    .post(async (req, res, next) => {
        const {visita_id, flag, diagnostico} = req.body;
        console.log(diagnostico);
        console.log(req.body);
        sqlite.run("UPDATE visitas SET estado = ?, diagnostico = ? WHERE visita_id = ?", [flag, diagnostico, visita_id], (err) => {
            if(err) {
                res.status(400).json(err.message);
                throw err;
            }
            console.log("POST /visitas/estado/");
            res.status(200).json(this.changes);
        });
    });

module.exports = router;
