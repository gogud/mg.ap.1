package com.example.mg_win.cloudfaceapi.Utils;

public interface FaceDetectResponse {
    void processDetectFinish(FaceDetect.FaceBounds[] output);
}
