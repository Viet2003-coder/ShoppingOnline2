const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendNotificationToToken = functions.https.onRequest(
  async (req, res) => {
    const { token, title, body } = req.body;

    if (!token) {
      return res.status(400).send("Missing FCM token");
    }

    const message = {
      token: token,
      notification: {
        title: title || "Thông báo mới",
        body: body || "Bạn có thông báo mới",
      },
    };

    try {
      const response = await admin.messaging().send(message);
      res.status(200).send({
        success: true,
        response,
      });
    } catch (error) {
      res.status(500).send({
        success: false,
        error: error.message,
      });
    }
  }
);
