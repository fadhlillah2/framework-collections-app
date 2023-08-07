./generate_jwtKeys.sh
cd amazon-cart
mvn clean compile package
cd ../amazon-jwt
mvn clean compile package
cd ../
docker compose up