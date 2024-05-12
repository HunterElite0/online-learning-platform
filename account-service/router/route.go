package router

import (
	"account-service/handler"

	"github.com/gofiber/fiber/v2"
)

func SetupRoutes(app *fiber.App) {

	account := app.Group("/account")
	account.Post("/create", handler.AddUser)
	account.Get("/users", handler.GetAllUsers)
	account.Get("/login", handler.Login)
	account.Put("/update", handler.UpdateUser)

}
