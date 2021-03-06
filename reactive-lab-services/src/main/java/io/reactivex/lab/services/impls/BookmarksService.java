package io.reactivex.lab.services.impls;

import io.reactivex.lab.services.MiddleTierService;
import io.reactivex.lab.services.common.Random;
import io.reactivex.lab.services.common.SimpleJson;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.text.sse.ServerSentEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class BookmarksService extends MiddleTierService {

    @Override
    protected Observable<Void> handleRequest(HttpServerRequest<?> request, HttpServerResponse<ServerSentEvent> response) {
        List<String> videoIds = request.getQueryParameters().get("videoId");

        int latency = 1;
        if (Random.randomIntFrom0to100() > 80) {
            latency = 10;
        }

        return Observable.from(videoIds).map(videoId -> {
            Map<String, Object> video = new HashMap<>();
            video.put("videoId", videoId);
            video.put("position", (int) (Math.random() * 5000));
            return video;
        }).flatMap(video -> {
            return response.writeAndFlush(new ServerSentEvent("", "data", SimpleJson.mapToJson(video)));
        }).delay(latency, TimeUnit.MILLISECONDS); // simulate latency
    }
}
