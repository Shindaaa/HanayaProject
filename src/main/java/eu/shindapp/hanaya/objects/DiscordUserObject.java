package eu.shindapp.hanaya.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class DiscordUserObject {

    @Getter
    @Setter
    String id;

    @Getter @Setter
    String userName;

    @Getter @Setter
    String avatarId;

    @Getter @Setter
    String discriminator;

    @Getter @Setter
    String bannerId;

    public DiscordUserObject(String id, String userName, String avatarId, String discriminator, String bannerId) {
        this.id = id;
        this.userName = userName;
        this.avatarId = avatarId;
        this.discriminator = discriminator;
        this.bannerId = bannerId;
    }

    public DiscordUserObject() {
        super();
    }

    public DiscordUserObject parseFromJson(JSONObject jsonObject) {
        if (!jsonObject.get("banner").equals(null)) {
            return new DiscordUserObject(
                    jsonObject.getString("id"),
                    jsonObject.getString("username"),
                    jsonObject.getString("avatar"),
                    jsonObject.getString("discriminator"),
                    jsonObject.getString("banner"));
        } else {
            return new DiscordUserObject(
                    jsonObject.getString("id"),
                    jsonObject.getString("username"),
                    jsonObject.getString("avatar"),
                    jsonObject.getString("discriminator"),
                    null);
        }
    }
}
