package eu.shindapp.hanaya.utils;

import eu.shindapp.hanaya.objects.DiscordUserObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class BannerUtils {

    private OkHttpClient client;
    private Request request;
    private Call call;
    private Response response;

    public String getBannerUrl(String userId) {
        try {
            client = new OkHttpClient();
            request = new Request.Builder()
                    .url("https://discord.com/api/v10/users/" + userId)
                    .addHeader("Authorization", "Bot " + new ConfigUtils().getString("bot-token"))
                    .build();
            call = client.newCall(request);
            response = call.execute();

            if (!response.isSuccessful()) {
                return "Unexpected code " + response;
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            DiscordUserObject discordUserObject = new DiscordUserObject().parseFromJson(jsonObject);

            if (discordUserObject.getBannerId() == null) {
                return "https://cdn.discordapp.com/attachments/856076221687660574/1048610903555321856/banner_002_hii.png";
            }

            if (discordUserObject.getBannerId().startsWith("a_")) {
                return "https://cdn.discordapp.com/banners/" + userId + "/" + discordUserObject.getBannerId() + ".gif?size=512";
            }

            return "https://cdn.discordapp.com/banners/" + userId + "/" + discordUserObject.getBannerId() + ".png?size=512";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
