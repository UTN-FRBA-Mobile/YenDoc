const express = require('express');
const {sqlite} = require('../database/sqlite');
const router = express.Router();

router.route('/')
    .get(async (req, res, next) => {
        sqlite.all("SELECT * FROM pacientes", [], (err, rows) => {
            if(err) {
                res.status(400).json(err.message);
                throw err;
            }
            console.log("GET /pacientes/");
            res.status(200).json(rows);
        });
    });

router.route('/:id')
    .get(async (req, res, next) => {
        const {id} = req.params;
        sqlite.get("SELECT * FROM pacientes WHERE paciente_id = ?", [id], (err, rows) => {
            if(err) {
                res.status(400).json({"error": err.message});
                throw err;
            }
            console.log("GET /pacientes/" + id);
            res.status(200).json(rows);
        });
    });

module.exports = router;
