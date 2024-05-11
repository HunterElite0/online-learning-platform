package main

import (
	"account-service/database"
	"account-service/handler"
	model "account-service/models"
	"log"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
)

func main() {

	app := fiber.New()
	app.Use(cors.New())

	app.Get("/", func(c *fiber.Ctx) error {
		return c.SendString("Hello, World!")
	})

	database.InitDB()
	user := model.Account{
		// Initialize the fields of the Account struct here
		Username:           "test",
		Name:               "Fares",
		Password:           "pass",
		Email:              "mail",
		Affiliation:        "IDK",
		Bio:                "LOL",
		YearsOfExperience:  0,
		Role:               "Student",
	}
	newUserId, err := handler.AddUser(user)
	if err != nil {
		log.Println("Error")
	}
	log.Println(newUserId)
	// handler.GetAllUsers()
	

	app.Listen(":3000")
}
