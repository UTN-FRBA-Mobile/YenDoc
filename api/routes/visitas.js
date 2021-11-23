const express = require('express');
const {sqlite} = require('../database/sqlite');
const router = express.Router();

router.route('/')
    .get(async (req, res, next) => {
        sqlite.all("SELECT * FROM visitas", [], (err, rows) => {
            if(err) {
                res.status(400).json({"error": err.message});
                throw err;
            }
            res.status(200).json({"message": "Success", "data": rows });
        });
    });

module.exports = router;
