package handler

import (
	"log"

	"account-service/database"
	model "account-service/models"
	_ "github.com/mattn/go-sqlite3"
)

func AddUser(account model.Account) (int64, error) {
	log.Println(account)
	db := database.DB
	stmt, err := db.Prepare(`INSERT INTO Account (username, name, password, email, affiliation, bio, yoe, role)
	VALUES (?,?,?,?,?,?,?,?,?)`)
	if err != nil {
		log.Fatal(err.Error())
	}
	defer stmt.Close()

	result, err := stmt.Exec(account.Username, account.Name, account.Password, account.Email, account.Affiliation, account.Bio, account.YearsOfExperience, account.Role)

	if err != nil {
		log.Fatal(err.Error())
	}

	inserted, err := result.LastInsertId()
	if err != nil {
		log.Fatal(err.Error())
	}

	return inserted, nil
}

func GetAllUsers() ([]model.Account, error) {
	db := database.DB
	stmt, err := db.Prepare(`SELECT * FROM Account`)
	if err != nil {
		log.Fatal(err.Error())
	}
	defer stmt.Close()

	rows, err := stmt.Query()
	if err != nil {
		log.Fatal(err.Error())
	}
	defer rows.Close()

	var accounts []model.Account
	for rows.Next() {
		var account model.Account
		err := rows.Scan(&account.ID, account.Username, &account.Name, account.Email, &account.Affiliation, &account.Bio, &account.Role, &account.Password, &account.YearsOfExperience)
		if err != nil {
			log.Fatal(err.Error())
		}
		accounts = append(accounts, account)
	}

	return accounts, nil
}
