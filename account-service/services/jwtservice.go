package services

import (
	"fmt"

	jwt "github.com/golang-jwt/jwt/v5"
)

var secretKey string = "very-very-secret-key"

func NewAccessToken(id int64, role string) (string, error) {
	claims := jwt.MapClaims{
		"id":   id,
		"role": role,
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)

	tokenString, err := token.SignedString([]byte(secretKey))
	if err != nil {
		fmt.Println(err.Error())
		return "Error creating token", err
	}

	return tokenString, nil
}

func VerifyToken(tokenString string) (map[string]string, error) {
	claims := jwt.MapClaims{}
	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(secretKey), nil
	})

	if err != nil {
		return nil, err
	}

	if !token.Valid {
		return nil, err
	}

	payload := make(map[string]string, len(claims))
	for key, val := range claims {
		payload[key] = val.(string)
	}

	return payload, nil
}
