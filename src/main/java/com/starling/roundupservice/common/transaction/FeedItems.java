package com.starling.roundupservice.common.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class FeedItems
{
    List<FeedItem> feedItems;
}
