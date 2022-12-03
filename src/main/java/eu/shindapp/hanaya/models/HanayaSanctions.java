package eu.shindapp.hanaya.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "hanaya_sanctions")
public class HanayaSanctions {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String userId;

    @DatabaseField
    private String type;

    @DatabaseField
    private String modId;

    @DatabaseField
    private String reason;
}
