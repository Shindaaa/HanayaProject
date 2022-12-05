package eu.shindapp.hanaya.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.shindapp.hanaya.HanayaCore;
import lombok.Data;

import java.sql.SQLException;
import java.util.List;

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

    public static List<HanayaSanctions> findById(String userId) {
        try {
            return HanayaCore.getHanayaSanctionsDao().queryBuilder().where().eq("userId", userId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save() {
        try {
            HanayaCore.getHanayaSanctionsDao().createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            HanayaCore.getHanayaSanctionsDao().delete(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
