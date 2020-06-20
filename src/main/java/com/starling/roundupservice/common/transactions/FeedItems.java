package com.starling.roundupservice.common.transactions;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class FeedItems {

  List<FeedItem> feedItems;

}
