package net.xsapi.panat.xsevent.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.xsapi.panat.xsevent.events.model.utils.XSScore;

import java.io.IOException;

public class XSScoreTypeAdapter extends TypeAdapter<XSScore> {
    @Override
    public void write(JsonWriter out, XSScore xsScore) throws IOException {
        if (xsScore == null) {
            out.nullValue();
        } else {
            out.beginObject();
            out.name("player");
            out.value(xsScore.getPlayerName());
            out.name("score");
            out.value(xsScore.getScore());
            out.endObject();
        }
    }

    @Override
    public XSScore read(JsonReader in) throws IOException {
        in.beginObject();
        String playerName = null;
        double score = 0;

        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("player")) {
                playerName = in.nextString();
            } else if (name.equals("score")) {
                score = in.nextDouble();
            } else {
                in.skipValue();
            }
        }

        in.endObject(); // ต้องเพิ่มบรรทัดนี้

        if (playerName == null) {
            throw new IOException("Missing 'player' field in JSON");
        }

        XSScore xsScore = new XSScore(playerName);
        xsScore.setScore(score);

        return xsScore;
    }


}



