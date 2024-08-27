package com.tttm.Whear.App.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class HistorySearchRequest {
    private String userId;

    private List<String> listHistorySearch;
}
