package com.starling.roundupservice.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class ClientErrorResponse
{
    final boolean success;
    final List<Map<String, String>> errors;
}
