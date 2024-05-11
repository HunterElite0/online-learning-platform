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
	VALUES (?,?,?,?,?,?,?,?)`)
	log.Println("Here")
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
		err := rows.Scan(
			&account.ID,
			&account.Username,
			&account.Name,
			&account.Password,
			&account.Email,
			&account.Affiliation,
			&account.Bio,
			&account.YearsOfExperience,
			&account.Role,
		)
		if err != nil {
			log.Fatal(err.Error())
		}
		accounts = append(accounts, account)
	}

	return accounts, nil
}

func GetUserById(id int) (model.Account, error) {
	db := database.DB
	stmt, err := db.Prepare(`SELECT * FROM Account WHERE id = ?`)
	if err != nil {
		log.Fatal(err.Error())
	}
	defer stmt.Close()

	var account model.Account
	err = stmt.QueryRow(id).Scan(
		&account.ID,
		&account.Username,
		&account.Name,
		&account.Password,
		&account.Email,
		&account.Affiliation,
		&account.Bio,
		&account.YearsOfExperience,
		&account.Role,
	)
	if err != nil {
		return model.Account{}, err
	}

	return account, nil
}

func ChangePassword(id int64, newPassword string) (bool, error) {
	db := database.DB
	result, err := db.Exec("UPDATE Account SET password = ? WHERE id = ?", newPassword, id)
	if err != nil {
		log.Fatal(err)
	}
	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return false, err
	}
	if rowsAffected == 0 {
		return false, err
	} else {
		return true, err
	}
}

func UpdateUser(id int64, newUser model.Account) (bool, error) {
	db := database.DB

	result, err := db.Exec(`
		UPDATE Account
		SET username = ?, name = ?, password = ?, email = ?, affiliation = ?, bio = ?, yoe = ?, role = ?
		WHERE id = ?`,
		newUser.Username,
		newUser.Name,
		newUser.Password,
		newUser.Email,
		newUser.Affiliation,
		newUser.Bio,
		newUser.YearsOfExperience,
		newUser.Role,
		id,
	)
	if err != nil {
		log.Fatal(err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return false, err
	}

	return rowsAffected > 0, nil
}
