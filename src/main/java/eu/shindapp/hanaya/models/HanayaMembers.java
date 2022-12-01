package eu.shindapp.hanaya.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable(tableName = "hanaya_members")
public class HanayaMembers {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String memberId;

    @DatabaseField
    private int warnCounter;

    @DatabaseField
    private int banCounter;

    @DatabaseField
    private int muteCounter;
}
