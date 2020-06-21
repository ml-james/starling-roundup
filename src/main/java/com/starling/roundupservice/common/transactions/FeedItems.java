package com.starling.roundupservice.common.transactions;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class FeedItems {

  List<FeedItem> feedItems;

}
