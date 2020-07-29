const mongoose = require("mongoose");

const DeviceSchema = new mongoose.Schema({
  qr_code: {
    type: String,
    required: true,
  },
  make: {
    type: String,
    required: true,
  },
  created: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model("Device", DeviceSchema);
