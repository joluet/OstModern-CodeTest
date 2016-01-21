package com.ostmodern.codetest.ui.model;

import com.ostmodern.codetest.api.model.Item;
import com.ostmodern.codetest.api.model.SetList;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;

public class CategoryMapper {

    private final static Observable.Transformer<SetList, List<Category>> categoryTransformer =
            observable -> observable
                    .map(setList -> setList.sets)
                    .flatMap(Observable::from)
                    .map(set -> {
                        final String imageUrl = getImageUrl(set.imageUrls);
                        return new Category(set.uid, set.title, set.summary, imageUrl, extractEpisodeUrls(set.items), extractDividers(set.items));
                    })
                    .toList();

    private static List<String> extractEpisodeUrls(Item[] items) {
        List<String> urls = new LinkedList<>();
        for (Item item : items) {
            if (item.type.equals("episode")) {
                urls.add(item.url);
            }
        }
        return urls;
    }

    private static List<Divider> extractDividers(Item[] items) {
        List<Divider> dividers = new LinkedList<>();
        int pos = 0;
        for (Item item : items) {
            if (item.type.equals("divider")) {
                dividers.add(new Divider(pos, item.heading));
            }
            pos++;
        }
        return dividers;
    }

    private static String getImageUrl(String[] imageUrls) {
        if (imageUrls.length > 0) {
            return imageUrls[0];
        } else {
            return "";
        }
    }

    /**
     * Provides a Transformer to map Set objects that are returned by the API to
     * Category objects that are used in the UI model.
     *
     * @return Transformer that maps a SetList object to a list of Category objects
     */
    @SuppressWarnings("unchecked")
    public static Observable.Transformer<SetList, List<Category>> mapSetToCategory() {
        return categoryTransformer;
    }


}
