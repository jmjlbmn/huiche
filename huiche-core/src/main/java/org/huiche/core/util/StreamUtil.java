package org.huiche.core.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 扩展jdk8 stream处理的工具类,目前仅分组收集器toList转换类型的扩展
 *
 * @author Maning
 */
@UtilityClass
public class StreamUtil {
    public static <T, R> Collector<T, List<T>, List<R>> toList(Function<List<T>, List<R>> finisher) {
        return new Collector<T, List<T>, List<R>>() {
            @Override
            public Supplier<List<T>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<T>, T> accumulator() {
                return List::add;
            }

            @Override
            public BinaryOperator<List<T>> combiner() {
                return (left, right) -> {
                    left.addAll(right);
                    return left;
                };
            }

            @Override
            public Function<List<T>, List<R>> finisher() {
                return finisher;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.unmodifiableSet(Collections.emptySet());
            }
        };
    }
}
