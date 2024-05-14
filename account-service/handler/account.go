package handler

import (
	"errors"
	"fmt"
	"strings"

	"account-service/database"
	model "account-service/models"
	"account-service/services"

	"github.com/gofiber/fiber/v2"
	_ "github.com/mattn/go-sqlite3"
	"golang.org/x/crypto/bcrypt"
)

type AccountData struct {
	Username          string `json:"username"`
	Name              string `json:"name"`
	Email             string `json:"email"`
	Affiliation       string `json:"affiliation"`
	Bio               string `json:"bio"`
	YearsOfExperience int    `json:"yoe"`
}

func HashPassword(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), 8)
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
		return "", errors.New("empty token body")
	}
	return cookies["jwt"], nil

}

func Login(c *fiber.Ctx) error {
	type LoginInput struct {
		Identifier string `json:"identifier"`
		Password   string `json:"password"`
	}

	type TokenData struct {
		ID       int64
		Password string
		Role     string
	}

	type JwtCookie struct {
		Name  string `json:"name"`
		Value string `json:"token"`
	}

	var request = LoginInput{}
	err := c.BodyParser(&request)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Failed to parse request")
	}

	db := database.DB
	stmt, err := db.Prepare(`SELECT id, role, password FROM Account WHERE username = ? OR email = ?`)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error processing request")
	}
	defer stmt.Close()

	account := TokenData{}
	err = stmt.QueryRow(request.Identifier, request.Identifier).Scan(
		&account.ID,
		&account.Role,
		&account.Password,
	)

	if err != nil {
		fmt.Println(err.Error())
		return c.Status(fiber.StatusInternalServerError).JSON("User not found")
	}

	passValid := CheckPasswordHash(request.Password, account.Password)
	if !passValid {
		return c.Status(fiber.StatusForbidden).JSON("Invalid password or username")
	}

	token, err := services.NewAccessToken(account.ID, account.Role)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error creating access token")
	}

	cookie := JwtCookie{
		Name:  "jwt",
		Value: token,
	}

	return c.Status(fiber.StatusOK).JSON(fiber.Map{"cookie": cookie})
}

func AddUser(c *fiber.Ctx) error {

	var account = model.Account{}
	err := c.BodyParser(&account)
	if err != nil {
		return c.JSON("Error parsing account")
	}

	db := database.DB
	stmt, err := db.Prepare(`INSERT INTO Account (username, name, password, email, affiliation, bio, yoe, role)
	VALUES (?,?,?,?,?,?,?,?)`)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error processing request")
	}
	defer stmt.Close()

	account.Password, err = HashPassword(account.Password)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error processing request")
	}

	if strings.ToLower(account.Role) == "student" {
		account.YearsOfExperience = -1
	}

	_, err = stmt.Exec(account.Username, account.Name, account.Password, account.Email, account.Affiliation, account.Bio, account.YearsOfExperience, account.Role)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error adding account, try a different username or email")
	}

	return c.JSON("Account successfully created!")
}

func GetAllUsers(c *fiber.Ctx) error {
	db := database.DB
	stmt, err := db.Prepare(`SELECT * FROM Account`)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error processing request")
	}
	defer stmt.Close()

	rows, err := stmt.Query()
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error executing query")
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
			return c.Status(fiber.StatusInternalServerError).JSON("Error processing request")
		}
		accounts = append(accounts, account)
	}

	return c.JSON(accounts)
}

func GetUserById(c *fiber.Ctx) error {

	id := c.Params("id")

	db := database.DB
	stmt, err := db.Prepare(`SELECT username, name, email, affiliation, bio, yoe FROM Account WHERE id = ?`)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON("Error processing request")
	}
	defer stmt.Close()

	var account = AccountData{}
	err = stmt.QueryRow(id).Scan(
		&account.Username,
		&account.Name,
		&account.Email,
		&account.Affiliation,
		&account.Bio,
		&account.YearsOfExperience,
	)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Invalid user id")
	}

	return c.JSON(account)
}

func ChangePassword(c *fiber.Ctx) error {

	cookieString := c.Get("Cookie")
	if cookieString == "" {
		return c.Status(fiber.StatusForbidden).JSON("Not authorized")
	}

	token, err := GetJwtTokenFromHeader(cookieString)
	if err != nil {
		return c.Status(fiber.StatusForbidden).JSON("Not authorized")
	}

	payload, err := services.VerifyToken(token)
	if err != nil {
		print(err.Error())
		return c.Status(fiber.StatusBadRequest).JSON("Invalid user id")
	}

	var obj map[string]string
	if err := c.BodyParser(&obj); err != nil {
		return err
	}

	newPassword, err := HashPassword(obj["password"])
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error processing request")
	}

	db := database.DB
	stmt, err := db.Prepare("UPDATE Account SET password = ? WHERE id = ?")
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON("Error processing request")
	}
	result, err := stmt.Exec(newPassword, payload.ID)
	if err != nil {
		return c.Status(fiber.StatusBadRequest).JSON("Invalid user id")
	}
	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error parsing rows")
	}
	if rowsAffected == 0 {
		return c.JSON("Account not found!")
	} else {
		return c.JSON("Password updated successfully!")
	}
}

func UpdateUser(c *fiber.Ctx) error {

	cookieStr := c.Get("Cookie")
	if cookieStr == "" {
		return c.Status(fiber.StatusForbidden).JSON("Not authorized")
	}

	token, err := GetJwtTokenFromHeader(cookieStr)
	if err != nil {
		return c.Status(fiber.StatusForbidden).JSON("Not authorized")
	}

	payload, err := services.VerifyToken(token)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Failed to verify token")
	}

	var account = model.Account{}
	err = c.BodyParser(&account)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error parsing fields")
	}

	db := database.DB
	stmt, err := db.Prepare(`
		UPDATE Account
		SET username = ?, name = ?, email = ?, affiliation = ?, bio = ?, yoe = ?
		WHERE id = ?`)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error processing request")
	}
	defer stmt.Close()

	result, err := stmt.Exec(account.Username, account.Name, account.Email, account.Affiliation, account.Bio, account.YearsOfExperience, payload.ID)
	if err != nil {
		fmt.Println(err.Error())
		return c.Status(fiber.StatusInternalServerError).JSON("Error updating account details")
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON("Error processing request")
	}

	if rowsAffected > 0 {
		return c.JSON("Account details updated successfully!")
	}
	return c.Status(fiber.StatusInternalServerError).JSON("Account doesn't exist")
}
