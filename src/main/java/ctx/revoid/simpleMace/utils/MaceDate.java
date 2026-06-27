package ctx.revoid.simpleMace.utils;

import java.util.UUID;

public class MaceDate {
    private UUID uuid;
    private String name;
    private String date;

    public MaceDate(String uuid, String name, String date) {
        this.uuid = UUID.fromString(uuid);
        this.name = name;
        this.date = date;
    }

    public UUID getUUID() { return uuid; }
    public String getName() { return name; }
    public String getDate() { return date; }
}
