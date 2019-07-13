package io.flutter.plugins.googlemaps;

import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class TileChannelResponse implements MethodChannel.Result {
    private Map<String, Object> response;
    private boolean ready = false;

    @Override
    public void success(Object o) {
        response = (Map<String, Object>) o;
        ready = true;
    }

    @Override
    public void error(String s, String s1, Object o) {
        ready = true;
    }

    @Override
    public void notImplemented() {
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public int getWidth() {
        if (response == null) return 0;

        return (int) response.get("width");
    }

    public int getHeight() {
        if (response == null) return 0;

        return (int) response.get("height");
    }

    public byte[] getImage() {
        if (response == null) return null;

        return (byte[]) response.get("image");
    }
}
