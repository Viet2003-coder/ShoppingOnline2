const express = require("express");
const admin = require("firebase-admin");
const bodyParser = require("body-parser");
const cors = require("cors");

const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://bookstoreoline-default-rtdb.asia-southeast1.firebasedatabase.app"
});

const app = express();
app.use(cors());
app.use(bodyParser.json());

/**
 * Confirm order + send notification
 */
app.post("/confirm-order", async (req, res) => {
  try {
    const { userId, orderId } = req.body;

    if (!userId || !orderId) {
      return res.status(400).json({ error: "Missing userId or orderId" });
    }

    // 1. Update order status
    await admin.database()
      .ref(`orders/${userId}/${orderId}`)
      .update({
        status: "CONFIRMED",
        confirmedAt: Date.now()
      });

    // 2. Get FCM token
    const tokenSnap = await admin.database()
      .ref(`users/${userId}/fcmToken`)
      .once("value");

    const token = tokenSnap.val();
    if (!token) {
      return res.json({ success: true, message: "No FCM token" });
    }

    // 3. Send notification
    const message = {
      token,
      notification: {
        title: "Đơn hàng đã được xác nhận",
        body: "Nhấn để xem chi tiết đơn hàng"
      },
      data: {
        orderId,
        type: "ORDER_CONFIRMED"
      }
    };

    const response = await admin.messaging().send(message);

    res.json({ success: true, response });

  } catch (e) {
    console.error(e);
    res.status(500).json({ error: e.message });
  }
});

app.listen(3000, () => {
  console.log("🔥 Backend running at http://localhost:3000");
});
