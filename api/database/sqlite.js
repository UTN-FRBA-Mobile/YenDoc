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
                sqlite.run(insert, ["fferrantino", "asd", "Felipe Ferrantino","fferrantino@example.com", 1001]);
                sqlite.run(insert, ["ggutierrez", "guti", "Gabriel Gutierrez","ggutierrez@example.com", 1002]);
                sqlite.run(insert, ["jperez", "pere", "Juan Perez","jperez@example.com", 1003]);
                sqlite.run(insert, ["jmedina", "medi", "Jose Medina","jmedina@example.com", 1004]);
                sqlite.run(insert, ["ssosa", "santis", "Santiago Sosa","ssosa@example.com", 1005]);
                }
            });
        sqlite.run(pacientesTable, (err) => { if (err) { console.error(err); }
            else {
                const insert = 'INSERT INTO pacientes (nombre, edad, email, telefono, direccion_calle, direccion_numero, direccion_latitud, direccion_longitud) VALUES (?,?,?,?,?,?,?,?)';
                sqlite.run(insert, ["Micaela Mendez", 24,"micamendez@example.com", 4654321, 'Caracas', 1417, -34.61426,-58.46855]);
                sqlite.run(insert, ["Sofia benitez", 32,"sofib@example.com", 9843514, 'Luis Viale', 2982, -34.62001,-58.47597]);
                sqlite.run(insert, ["Maria Sol Perez", 36,"msperez@example.com", 9843515, 'Moron', 2352, -34.62164,-58.46497]);
                sqlite.run(insert, ["Joaquin Romero", 29,"joacoromero@example.com", 9843516, 'Gral. Cesar diaz', 2579, -34.61196,-58.47497]);
                sqlite.run(insert, ["Patricio Roldan", 40,"proldan@example.com", 9843517, 'Nazarre', 2638, -34.59814,-58.48794]);
                sqlite.run(insert, ["Roberto Miranda", 35,"roberto.miranda@example.com", 9843518, 'Pedro Moran', 2874, -34.59251,-58.49589]);
                sqlite.run(insert, ["Sebastian Juarez", 44,"sebajuarez@example.com", 9843519, 'Asuncion', 2147, -34.58642,-58.48825]);
                sqlite.run(insert, ["Fernando Zapata", 29,"fzapata@example.com", 9843510, 'Ballivian', 3174, -34.58362,-58.48512]);
                sqlite.run(insert, ["Ernestina Mateu", 32,"ernestina.mateu@example.com", 9843511, 'La Pampa', 5516, -34.58221,-58.48424]);
                sqlite.run(insert, ["Rene Flores", 35,"reneflores@example.com", 9843512, 'Barzana', 1906, -34.58147,-58.48624]);
                sqlite.run(insert, ["Andrea Peralta", 35,"aperalta1@example.com", 9843513, 'Avalos', 1559, -34.58226,-58.47948]);
                sqlite.run(insert, ["Matias Leiva", 39,"matias.leiva@example.com", 9843524, 'Berlin', 3949, -34.58168,-58.47848]);
                sqlite.run(insert, ["Pedro Jofre", 51,"pjofre@example.com", 9843534, 'Cadiz', 4125, -34.58059,-58.47989]);
                }
            });
        sqlite.run(visitasTable, (err) => { if (err) { console.error(err); }
			else {
				const insert = 'INSERT INTO visitas (fecha, profesional_id, paciente_id, diagnostico, sintomas, estado) VALUES (?,?,?,?,?,?)';
				sqlite.run(insert, [new Date(2021, 11, 28), 1, 1, "No se encontro al paciente", "Presenta tos, dolor de cabeza,y congestion", 2]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 1, 2, "Cuadro alergico", "Presenta mucha congestion, dolor de cabeza, y ojos llorosos", 1]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 1, 3, "Bronquitis", "Presenta tos fuerte, y respiracion agitada", 1]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 1, 4, "", "Presenta fuerte dolor de garganta", 0]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 2, 5, "Angina", "Indica dolor al tragar, y fiebre", 1]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 2, 6, "", "Manifiesta dolor de cabeza, resfrio, y dolor de cabeza", 0]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 2, 7, "", "Manifiesta fuerte dolor de cabeza, tos,y fiebre", 0]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 3, 8, "Gripe", "Indica tos, y mucho cansancio corporal", 1]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 3, 9, "", "Indica mucho cansancio corporal y fiebre", 0]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 3, 10, "", "Presenta fuerte dolor de garganta, y fiebre", 0]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 4, 11, "Cuadro alergico", "Presenta secrecion nasal, estornudos, y dolor de cabeza", 1]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 4, 12, "No se encontro al paciente", "Presenta congestion nasal, y fuerte dolor de cabeza ", 2]);
                      sqlite.run(insert, [new Date(2021, 11, 28), 4, 13, "", "Presenta tos, dolor muscular, y febricula", 0]);
            }
        });
    }
});


module.exports = {sqlite};
