package database

import (
	"database/sql"
	"log"
	"os"

	_ "github.com/mattn/go-sqlite3"
)

var DB *sql.DB

func InitDB() error {
	os.Remove("database.db")

	file, err := os.Create("database.db")
	if err != nil {
		log.Fatal(err.Error())
	}
	defer file.Close()

	db, err := sql.Open("sqlite3", "database.db")
	if err != nil {
		log.Fatal(err.Error())
	}

	stmt, err := db.Prepare(`
	CREATE TABLE IF NOT EXISTS Account (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	username TEXT UNIQUE NOT NULL,
	name TEXT NOT NULL,
	password TEXT NOT NULL,
	email TEXT UNIQUE,
	affiliation TEXT,
	bio TEXT,
	yoe INTEGER,
	role TEXT NOT NULL);`)

	if err != nil {
		log.Fatal(err.Error())
	}

	_, err = stmt.Exec()

	DB = db

	return err

}
