require("dotenv").config();

const express = require("express");
const app = express();
const port = 3000;

const mongoose = require("mongoose");

mongoose.connect(process.env.DB_URI, {
  useUnifiedTopology: true,
  useNewUrlParser: true,
});

const db = mongoose.connection;

db.on("error", (error) => console.error(error));
db.on("open", () => console.log("Connected to Database"));

app.use(express.json());

const deviceRoute = require("./routes/devices");
app.use("/devices", deviceRoute);

app.listen(port, () =>
  console.log(`Server started. App listening at http://localhost:${port}`)
);
