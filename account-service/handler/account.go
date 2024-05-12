package handler

import (
	"errors"
	"fmt"
	"strconv"
	"strings"

	"account-service/database"
	model "account-service/models"
	"account-service/services"

	"github.com/gofiber/fiber/v2"
	_ "github.com/mattn/go-sqlite3"
	"golang.org/x/crypto/bcrypt"
)

func HashPassword(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), 14)
	return string(bytes), err
}

func CheckPasswordHash(password, hash string) bool {
	err := bcrypt.CompareHashAndPassword([]byte(hash), []byte(password))
	return err == nil
}

func GetJwtTokenFromHeader(cookieStr string) (string, error) {
	cookies := make(map[string]string)
	for _, cookie := range strings.Split(cookieStr, ";") {
		parts := strings.SplitN(cookie, "=", 2)
		if len(parts) == 2 {
			cookies[parts[0]] = parts[1]
		}
	}

	if cookies["jwt"] == "" {
		return "", errors.New("Empty token body")
	}
	return cookies["jwt"], nil

}

func Login(c *fiber.Ctx) error {
	type LoginInput struct {
		Identifier string `json:"identifier"`
		Password   string `json:"password"`
	}

	type AccountData struct {
		ID       int64
		Password string
		Role     string
	}

	var request = LoginInput{}
	err := c.BodyParser(&request)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Failed to parse request")
	}

	db := database.DB
	stmt, err := db.Prepare(`SELECT id, role, password FROM Account WHERE username = ? OR email = ?`)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error processing request")
	}
	defer stmt.Close()

	account := AccountData{}
	err = stmt.QueryRow(request.Identifier, request.Identifier).Scan(
		&account.ID,
		&account.Role,
		&account.Password,
	)

	if err != nil {
		fmt.Println(err.Error())
		return c.Status(fiber.StatusInternalServerError).SendString("User not found")
	}

	passValid := CheckPasswordHash(request.Password, account.Password)
	if !passValid {
		return c.SendString("Invalid password or username")
	}

	token, err := services.NewAccessToken(account.ID, account.Role)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error creating access token")
	}

	cookie := new(fiber.Cookie)
	cookie.Name = "jwt"
	cookie.Value = token
	c.Cookie(cookie)

	return c.SendString("Successfully logged in!")
}

func AddUser(c *fiber.Ctx) error {

	var account = model.Account{}
	err := c.BodyParser(&account)
	if err != nil {
		return c.SendString("Error parsing account")
	}

	db := database.DB
	stmt, err := db.Prepare(`INSERT INTO Account (username, name, password, email, affiliation, bio, yoe, role)
	VALUES (?,?,?,?,?,?,?,?)`)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error processing request")
	}
	defer stmt.Close()

	account.Password, err = HashPassword(account.Password)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error processing request")
	}
	_, err = stmt.Exec(account.Username, account.Name, account.Password, account.Email, account.Affiliation, account.Bio, account.YearsOfExperience, account.Role)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error adding account, try a different username or email")
	}

	return c.SendString("Account successfully created!")
}

func GetAllUsers(c *fiber.Ctx) error {
	db := database.DB
	stmt, err := db.Prepare(`SELECT * FROM Account`)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error processing request")
	}
	defer stmt.Close()

	rows, err := stmt.Query()
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error executing query")
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
			return c.Status(fiber.StatusInternalServerError).SendString("Error processing request")
		}
		accounts = append(accounts, account)
	}

	return c.JSON(accounts)
}

func GetUserById(id int) (model.Account, error) {
	db := database.DB
	stmt, err := db.Prepare(`SELECT * FROM Account WHERE id = ?`)
	if err != nil {
		// log.Fatal(err.Error())
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

func ChangePassword(c *fiber.Ctx) error {

	id, err := strconv.ParseInt(c.Params("id"), 10, 64)
	if err != nil {
		print(err.Error())
		return c.Status(fiber.StatusBadRequest).SendString("Invalid user id")
	}

	var newPassword struct {
		Password string `json:"password"`
	}
	if err := c.BodyParser(&newPassword); err != nil {
		return err
	}

	db := database.DB
	stmt, err := db.Prepare("UPDATE Account SET password = ? WHERE id = ?")
	if err != nil {
		//log.Fatal(err.Error())
	}
	result, err := stmt.Exec(newPassword, id)
	if err != nil {
		//log.Fatal(err)
	}
	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error parsing rows")
	}
	if rowsAffected == 0 {
		return c.SendString("Account not found!")
	} else {
		return c.SendString("Password updated successfully!")
	}
}

func UpdateUser(c *fiber.Ctx) error {

	cookieStr := c.Get("Cookie")
	if cookieStr == "" {
		return c.Status(fiber.StatusForbidden).SendString("Not authorized")
	}

	token, err := GetJwtTokenFromHeader(cookieStr)
	if err != nil {
		return c.Status(fiber.StatusForbidden).SendString("Not authorized")
	}

	payload, err := services.VerifyToken(token)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Failed to verify token")
	}

	fmt.Println(payload)

	var account = model.Account{}
	err = c.BodyParser(&account)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error parsing fields")
	}

	db := database.DB
	stmt, err := db.Prepare(`
		UPDATE Account
		SET username = ?, name = ?, email = ?, affiliation = ?, bio = ?, yoe = ?, role = ?
		WHERE id = ?`)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error processing request")
	}
	defer stmt.Close()

	result, err := stmt.Exec(account.Username, account.Name, account.Email, account.Affiliation, account.Bio, account.YearsOfExperience, account.Role, 122112)
	if err != nil {
		fmt.Println(err.Error())
		return c.Status(fiber.StatusInternalServerError).SendString("Error updating account details")
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Error processing request")
	}

	if rowsAffected > 0 {
		return c.SendString("Account details updated successfully!")
	}
	return c.Status(fiber.StatusInternalServerError).SendString("Account doesn't exist")
}
