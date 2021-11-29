const sqlite3 = require('sqlite3').verbose()

const DBSOURCE = "db.sqlite"

const sqlite = new sqlite3.Database(DBSOURCE, (err) => {
    if (err) {
        // Cannot open database
        console.error(err.message);
        throw err;
    } else {
        console.log('Connected to the SQLite database.');
        const profesionalesTable = `CREATE TABLE IF NOT EXISTS profesionales (
            profesional_id INTEGER PRIMARY KEY AUTOINCREMENT,
            usuario TEXT,
            contrasenia TEXT,
            nombre TEXT, 
            email TEXT,
            legajo INTEGER
        )`;
        const pacientesTable = `CREATE TABLE IF NOT EXISTS pacientes (
            paciente_id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            edad INTEGER,
            email TEXT,
            telefono INTEGER,
            direccion_calle TEXT,
            direccion_numero INTEGER,
            direccion_obs TEXT,
            direccion_longitud REAL,
            direccion_latitud REAL
        )`;
        const visitasTable = `CREATE TABLE IF NOT EXISTS visitas (
            visita_id INTEGER PRIMARY KEY AUTOINCREMENT,
            fecha DATE,
            profesional_id INTEGER NOT NULL,
            paciente_id INTEGER NOT NULL,
            diagnostico TEXT,
            sintomas TEXT,
            estado INTEGER,
            FOREIGN KEY (profesional_id) 
              REFERENCES profesionales (profesional_id) 
                 ON DELETE NO ACTION
                 ON UPDATE NO ACTION,
            FOREIGN KEY (paciente_id) 
              REFERENCES pacientes (paciente_id) 
                 ON DELETE NO ACTION
                 ON UPDATE NO ACTION
        )`;
        sqlite.run(profesionalesTable, (err) => { if (err) { console.error(err); }
            else {
                const insert = 'INSERT INTO profesionales (usuario, contrasenia, nombre, email, legajo) VALUES (?,?,?,?,?)';
                sqlite.run(insert, ["admin", "asd", "admin","admin@example.com", 1]);
                sqlite.run(insert, ["user", "asd", "user","user@example.com", 2]);
                sqlite.run(insert, ["Juan", "asd", "Juan Perez","med1@example.com", 3]);
                sqlite.run(insert, ["Pepe", "asd", "Pepe Medina","med2@example.com", 4]);
                }
            });
        sqlite.run(pacientesTable, (err) => { if (err) { console.error(err); }
            else {
                const insert = 'INSERT INTO pacientes (nombre, edad, email, telefono, direccion_calle, direccion_numero) VALUES (?,?,?,?,?,?)';
                sqlite.run(insert, ["Micaela", 24,"med1@example.com", 4654321, 'Avellaneda', 96]);
                sqlite.run(insert, ["Sofia", 32,"med2@example.com", 9843514, 'Rivadavia', 45]);
                }
            });
        sqlite.run(visitasTable, (err) => { if (err) { console.error(err); }
			else {
				const insert = 'INSERT INTO visitas (fecha, profesional_id, paciente_id, diagnostico, sintomas, estado) VALUES (?,?,?,?,?,?)';
				sqlite.run(insert, [new Date(), 1, 1, "Gripe", "Mocos", 1]);
				sqlite.run(insert, [new Date(2021, 9, 1), 2, 2, "Covid", "Tos", 0]);
            }
        });
    }
});


module.exports = {sqlite};
