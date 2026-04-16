# E-Sports Arena Reservation System
A Java CLI application with SQLite database.

## Requirements
- Java JDK 17 or higher
- The `sqlite-jdbc-3.51.3.0.jar` file (included in `lib/`)

## Project Structure
```
esports-reservation/
├── lib/
│   └── sqlite-jdbc-3.51.3.0.jar
├── src/main/java/com/esports/
│   ├── Main.java
│   ├── util/
│   │   ├── DatabaseConnection.java
│   │   └── ConsoleUtil.java
│   ├── model/
│   │   ├── Event.java
│   │   ├── Seat.java
│   │   └── Reservation.java
│   ├── dao/
│   │   ├── AdminDAO.java
│   │   ├── EventDAO.java
│   │   ├── SeatDAO.java
│   │   └── ReservationDAO.java
│   └── ui/
│       ├── UserMenu.java
│       └── AdminMenu.java
└── README.md
```

## Compile & Run

### Linux / macOS
```bash
# Compile
javac -cp lib/sqlite-jdbc-3.51.3.0.jar \
  -d out \
  $(find src -name "*.java")

# Run
java -cp "out:lib/sqlite-jdbc-3.51.3.0.jar" com.esports.Main
```

### Windows (Command Prompt)
```cmd
# Compile
for /r src %%f in (*.java) do javac -cp lib\sqlite-jdbc-3.51.3.0.jar -d out "%%f"

# Run
java -cp "out;lib\sqlite-jdbc-3.51.3.0.jar" com.esports.Main
```

## Default Admin Credentials
- **Username:** admin
- **Password:** admin123

## Notes
- The SQLite database file `esports_arena.db` is created automatically on first run.
- Each new event auto-generates 30 seats (rows A–C, seats 1–10).
- Deleting an event also removes all its seats and reservations (CASCADE).
