package email;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by GsolC on 2/24/2017.
 */

public class LongOperation extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try {

//            GMailSender sender = new GMailSender("sender.sendermail.com", "senders password");
//            sender.sendMail("subject",
//                    "body",
//                    "sender.sendermail.com",
//                    "reciepients.recepientmail.com");
//
            GMailSender sender = new GMailSender("5raan6@gmail.com", "franship101");
            sender.sendMail(" mSkola Customer Feedback",
                    params[0], params[1],
                    "5raan6@gmail.com,mountedwingscsltd@gmail.com");

        } catch (Exception e) {
            Log.e("error", e.getMessage(), e);
            return "Email Not Sent";
        }
        return "Email Sent";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("LongOperation", result + "");
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
