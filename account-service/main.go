package main

import (
	"account-service/database"
	"account-service/handler"
	model "account-service/models"
	"log"
	"strconv"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
)

func main() {

	app := fiber.New()
	app.Use(cors.New())

	app.Get("/", func(c *fiber.Ctx) error {
		return c.SendString("Hello, World!")
	})

	app.Get("/users", func(c *fiber.Ctx) error {
		users, _ := handler.GetAllUsers()
		// Return the users as JSON response
		return c.JSON(users)
	})

	app.Put("/users/:id/ChangePassword", func(c *fiber.Ctx) error {

		userID, err := strconv.ParseInt(c.Params("id"), 10, 64)
		if err != nil {
			print(err.Error())
			return c.Status(fiber.StatusBadRequest).SendString("Enter a valid user ID")
		}

		var newPassword struct {
			Password string `json:"password"`
		}
		if err := c.BodyParser(&newPassword); err != nil {
			return err
		}

		isSuccessful, err := handler.ChangePassword(userID, newPassword.Password)
		if err != nil {
			log.Println(err.Error())
		}
		if isSuccessful {
			return c.SendString("Password Updated")
		} else {
			return c.Status(fiber.StatusNotFound).SendString("User not found")
		}
	})

	//app.Put("/users/:id/UpdateUser", func(c *fiber.Ctx) error

	database.InitDB()
	var user = model.Account{}
	user.Username = "test"
	user.Name = "Fares"
	user.Password = "pass"
	user.Email = "mail"
	user.Affiliation = "IDK"
	user.Bio = "LOL"
	user.YearsOfExperience = 0
	user.Role = "Student"
	newUserId, err := handler.AddUser(user)
	if err != nil {
		log.Println("Error")
	}
	log.Println(newUserId)
	//user_test, _ := handler.GetUserById(1)
	users, _ := handler.GetAllUsers()
	log.Printf("%v", users)

	app.Listen(":3000")
}
