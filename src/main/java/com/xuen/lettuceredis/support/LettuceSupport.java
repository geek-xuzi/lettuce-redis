package com.xuen.lettuceredis.support;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zheng.xu
 * @since 2017-05-15
 */
public class LettuceSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(LettuceSupport.class);


    /**
     * @param inputs 需要聚合的future list
     * @param aggregationFunction 聚合函数
     * @param <Input> future 返回值
     * @param <OutPut> 聚合函数返回值
     * @return 集合结果的future
     */
    public static <Input, OutPut> CompletionStage<OutPut> combine(
            List<CompletionStage<Input>> inputs,
            AggregationFunction<Input, OutPut> aggregationFunction) {
        return combine(inputs, aggregationFunction, MoreExecutors.directExecutor());
    }

    public static <Input, Output> CompletionStage<Output> combine(
            List<CompletionStage<Input>> inputs,
            AggregationFunction<Input, Output> aggregationFunction,
            Executor executor) {

        Preconditions
                .checkArgument(inputs != null && inputs.size() > 0, "inputs must not be empty");

        ErrorAsNullBiFunction<Input> nullFunc = new ErrorAsNullBiFunction<>();
        List<Input> chain = new ArrayList<>(inputs.size());
        initListWithNull(chain, inputs.size());
        CompletionStage<Input> previous = null;

        for (int i = 0; i < inputs.size(); i++) {
            final int idx = i;
            CompletionStage<Input> trans = inputs.get(i).handleAsync(nullFunc, executor);
            if (previous != null) {

                previous = previous.thenCombineAsync(trans, (prev, current) -> {
                    chain.set(idx, current);
                    return current;
                }, executor);
            } else {
                previous = trans.thenApplyAsync(input -> {
                    chain.set(0, input);
                    return input;
                }, executor);
            }

        }

        return previous.thenApplyAsync(input -> aggregationFunction.apply(chain), executor);
    }

    private static <Input> void initListWithNull(List<Input> chain, int size) {
        for (int i = 0; i < size; i++) {
            chain.add(null);
        }
    }

    /**
     * 阻塞的获取future的值
     */
    public static <T> T get(CompletionStage<T> stage)
            throws ExecutionException, InterruptedException {
        if (stage instanceof Future) {
            return (T) ((Future) stage).get();
        } else {
            throw new UnsupportedOperationException(
                    String.format("operation is not supported by %s", stage.getClass()));
        }
    }


    public static void main(String[] args) throws InterruptedException {
        CompletionStage<Integer> future1 = CompletableFuture.supplyAsync(() -> 1);
        CompletionStage<Integer> future2 = CompletableFuture.supplyAsync(() -> 2);
        CompletionStage<Integer> future3 = CompletableFuture.supplyAsync(() -> 3);
        ArrayList<CompletionStage<Integer>> inputs = Lists
                .newArrayList(future1, future2, future3);
        CompletionStage<String> combine = LettuceSupport.combine(inputs, chain -> chain.get(0) + chain.get(1) + chain.get(2) + "");
        try {
            String s = LettuceSupport.get(combine);
            System.out.println(s);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
