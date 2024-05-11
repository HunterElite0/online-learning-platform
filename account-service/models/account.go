package models

type Account struct {
	ID                int64
	Username          string
	Name              string
	Password          string
	Email             string
	Affiliation       string
	Bio               string
	YearsOfExperience int
	Role              string
}
