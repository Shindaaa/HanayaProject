package eu.shindapp.hanaya.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.shindapp.hanaya.HanayaCore;
import lombok.Data;

import java.sql.SQLException;

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

    public static HanayaMembers findById(String userId) {
        try {
            return HanayaCore.getHanayaMembersDao().queryBuilder().where().eq("userId", userId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save() {
        try {
            HanayaCore.getHanayaMembersDao().createOrUpdate(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            HanayaCore.getHanayaMembersDao().delete(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
