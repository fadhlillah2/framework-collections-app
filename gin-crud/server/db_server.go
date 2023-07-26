package server

import (
	"database/sql"
	"log"
	// "io/ioutil"

	"github.com/spf13/viper"
)

func InitDatabase(config *viper.Viper) *sql.DB {
	connectionString := config.GetString("database.connection_string")
	maxIdleConnections := config.GetInt("database.max_idle_connections")
	maxOpenConnections := config.GetInt("database.max_open_connections")
	connectionMaxLifetime := config.GetDuration("database.connection_max_lifetime")
	driverName := config.GetString("database.driver_name")

	if connectionString == "" {
		log.Fatalf("Database connect in string is missing")
	}

	dbHandler, err := sql.Open(driverName, connectionString)
	if err != nil {
		log.Fatalf("Error while initializing database: %v", err)
	}

	dbHandler.SetMaxIdleConns(maxIdleConnections)
	dbHandler.SetMaxOpenConns(maxOpenConnections)
	dbHandler.SetConnMaxLifetime(connectionMaxLifetime)

	err = dbHandler.Ping()
	if err != nil {
		dbHandler.Close()
		log.Fatalf("Error while validating database: %v", err)
	} else {
		log.Println("Database connected")
	}

	// // Read the schema file
	// schema, err := ioutil.ReadFile("db-generator/schema.sql")
	// if err != nil {
	// 	log.Fatal(err)
	// }

	// // Execute the schema
	// _, err = dbHandler.Exec(string(schema))
	// if err != nil {
	// 	log.Fatal(err)
	// }

	log.Println("Schema executed successfully!")

	return dbHandler
}
