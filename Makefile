```makefile
.PHONY: build run test clean

build:
	./mvnw clean package -DskipTests

run:
	docker compose up --build

test:
	./mvnw test

clean:
	docker compose down -v