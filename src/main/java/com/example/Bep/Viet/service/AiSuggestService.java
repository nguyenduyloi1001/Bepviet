package com.example.Bep.Viet.service;

import com.example.Bep.Viet.request.AiSuggestRequest;
import com.example.Bep.Viet.response.AiSuggestResponse;

public interface AiSuggestService {
    AiSuggestResponse suggest(AiSuggestRequest request);
}
