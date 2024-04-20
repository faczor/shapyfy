package com.shapyfy.core.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamUtils {

    public static <T> Stream<T> ofNullable(List<T> list) {
        return Optional.ofNullable(list).stream().flatMap(Collection::stream);
    }
}
