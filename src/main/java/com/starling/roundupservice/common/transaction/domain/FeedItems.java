package com.starling.roundupservice.common.transaction.domain;

import java.util.List;

import com.starling.roundupservice.common.transaction.domain.FeedItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class FeedItems
{
    List<FeedItem> feedItems;
}
