package net.voids.unethicalite.utils.api;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class VoidPredicates
{
    public static <T> Predicate<T> distinctByProperty(Function<? super T, ?> propertyExtractor)
    {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(propertyExtractor.apply(t));
    }

}
