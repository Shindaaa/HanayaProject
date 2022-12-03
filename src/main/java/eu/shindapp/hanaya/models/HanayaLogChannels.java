package eu.shindapp.hanaya.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "hanaya_log_channels")
public class HanayaLogChannels {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String guildId;

    @DatabaseField
    private String channelId;

    @DatabaseField
    private String type;
}
