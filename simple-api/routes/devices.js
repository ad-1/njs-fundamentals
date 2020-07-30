const express = require("express");
const Device = require("../models/Device");

const router = express.Router();

// GET ALL

router.get("/", async (req, res) => {
  try {
    const devices = await Device.find();
    res.json(devices);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

// GET SPECIFIC

router.get("/:id", getDevice, (req, res) => {
  res.json(res.device);
});

// CREATE

router.post("/", async (req, res) => {
  const device = new Device({
    qr_code: req.body.qr_code,
    make: req.body.make,
  });
  console.log(`creating new device: ${device}`);
  device
    .save()
    .then((data) => {
      res.status(201).json(data);
    })
    .catch((err) => {
      res.status(400).json({ message: err });
    });
});

// UPDATE

router.patch("/:id", getDevice, async (req, res) => {
  if (req.body.qr_code != null) {
    res.device.qr_code = req.body.qr_code;
  }
  if (req.body.make != null) {
    res.device.make = req.body.make;
  }
  try {
    const updatedDevice = await res.device.save();
    res.status(201).json(updatedDevice);
  } catch (err) {
    res.status(400).json({ message: err.message });
  }
});

// DELETE

router.delete("/:id", getDevice, async (req, res) => {
  try {
    await res.device.remove();
    res.json({ message: "Device removed" });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

// MIDDLEWARE

async function getDevice(req, res, next) {
  let device;
  try {
    const id = req.params.id;
    device = await Device.findById(id);
    if (device == null) {
      return res.status(404).json({ message: `No device with id ${id}` });
    }
  } catch (err) {
    return res.status(500).json({ message: err.message });
  }
  res.device = device;
  next();
}

module.exports = router;
