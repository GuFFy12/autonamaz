package org.islam.autonamaz;

import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Prayer;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonParser;

import java.util.Date;

public class NamazTimes implements Globals {
    int namazTimeWait = 600000;
    double latitude = 0;
    double longitude = 0;
    public long namazTime;

    public void getClientLocation() {
        try {
            var request = HttpRequest.get("http://ip-api.com/json/");

            if (request.code() == 200) {
                var coordinates = gson.fromJson(JsonParser.parseString(request.body()).getAsJsonObject().getAsJsonObject(), IpApiWrapper.class);

                latitude = coordinates.lat;
                longitude = coordinates.lon;
            }
        } catch (HttpRequest.HttpRequestException ignored) {}
    }

    public Prayer isNamazTime() {
        var namazTimes = calculateNamazTimes();

        long timeDiff;
        var currentTimeMillis = System.currentTimeMillis();
        if ((timeDiff = currentTimeMillis - namazTimes.isha.getTime()) >= 0 && timeDiff <= namazTimeWait) {
            namazTime = namazTimes.isha.getTime();
            return Prayer.ISHA;
        } else if ((timeDiff = currentTimeMillis - namazTimes.maghrib.getTime()) >= 0 && timeDiff <= namazTimeWait) {
            namazTime = namazTimes.maghrib.getTime();
            return Prayer.MAGHRIB;
        } else if ((timeDiff = currentTimeMillis - namazTimes.asr.getTime()) >= 0 && timeDiff <= namazTimeWait) {
            namazTime = namazTimes.asr.getTime();
            return Prayer.ASR;
        } else if ((timeDiff = currentTimeMillis - namazTimes.dhuhr.getTime()) >= 0 && timeDiff <= namazTimeWait) {
            namazTime = namazTimes.dhuhr.getTime();
            return Prayer.DHUHR;
        } else if ((timeDiff = currentTimeMillis - namazTimes.sunrise.getTime()) >= 0 && timeDiff <= namazTimeWait) {
            namazTime = namazTimes.sunrise.getTime();
            return Prayer.SUNRISE;
        } else if ((timeDiff = currentTimeMillis - namazTimes.fajr.getTime()) >= 0 && timeDiff <= namazTimeWait) {
            namazTime = namazTimes.fajr.getTime();
            return Prayer.FAJR;
        } else {
            return Prayer.NONE;
        }
    }

    public PrayerTimes calculateNamazTimes() {
        var coordinates = new Coordinates(latitude, longitude);
        var dateComponents = DateComponents.from(new Date());
        var parameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();

        return new PrayerTimes(coordinates, dateComponents, parameters);
    }
}
