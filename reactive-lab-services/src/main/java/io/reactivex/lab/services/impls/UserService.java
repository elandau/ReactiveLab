package io.reactivex.lab.services.impls;

import io.reactivex.lab.services.MiddleTierService;
import io.reactivex.lab.services.common.SimpleJson;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.text.sse.ServerSentEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class UserService extends MiddleTierService {

    @Override
    protected Observable<Void> handleRequest(HttpServerRequest<?> request, HttpServerResponse<ServerSentEvent> response) {
        List<String> userIds = request.getQueryParameters().get("userId");
        if (userIds == null || userIds.size() == 0) {
            return writeError(request, response, "At least one parameter of 'userId' must be included.");
        }
        return Observable.from(userIds).map(userId -> {
            Map<String, Object> user = new HashMap<>();
            user.put("userId", userId);
            user.put("name", "Name Here");
            user.put("other_data", "goes_here");
            return user;
        }).flatMap(user -> {
            return response.writeAndFlush(new ServerSentEvent("1", "data", SimpleJson.mapToJson(user)));
        }).delay(((long) (Math.random() * 20) + 1500), TimeUnit.MILLISECONDS); // simulate latency 
    }
}
