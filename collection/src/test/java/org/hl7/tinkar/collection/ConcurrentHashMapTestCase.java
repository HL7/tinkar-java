package org.hl7.tinkar.common.collection;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.bag.mutable.HashBag;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.parallel.ParallelIterate;
import org.hl7.tinkar.collection.ConcurrentUuidIntHashMap;
import org.junit.jupiter.api.*;

public abstract class ConcurrentHashMapTestCase extends MutableMapTestCase
{
    protected ExecutorService executor;

    @BeforeEach
    public void setUp()
    {
        this.executor = Executors.newFixedThreadPool(20);
    }

    @AfterEach
    public void tearDown()
    {
        this.executor.shutdown();
    }

    @Override
    @Test
    public void updateValue()
    {
        super.updateValue();

        ConcurrentUuidIntHashMap map = this.newUuidIntMap();
        ParallelIterate.forEach(Interval.oneTo(100), each -> map.updateValue(new UUID(0, each % 10), () -> 0, integer -> integer + 1), 1, this.executor);
        Assertions.assertEquals(Interval.zeroTo(9).toSet(),
                Sets.mutable.of(map.keySet().stream().map(uuid -> toInt(uuid)).toArray()));
        Assertions.assertEquals(FastList.newList(Collections.nCopies(10, 10)), FastList.newList(map.values()));
    }

    @Override
    @Test
    public void updateValue_collisions()
    {
        super.updateValue_collisions();

        ConcurrentUuidIntHashMap map = this.newUuidIntMap();
        MutableList<Integer> list = Interval.oneTo(100).toList().shuffleThis();
        ParallelIterate.forEach(list, each -> map.updateValue(new UUID(0, each % 50), () -> 0, integer -> integer + 1), 1, this.executor);
        Assertions.assertEquals(Interval.zeroTo(49).toSet(),
                Sets.mutable.of(map.keySet().stream().map(uuid -> toInt(uuid)).toArray()));
        Assertions.assertEquals(
                FastList.newList(Collections.nCopies(50, 2)),
                FastList.newList(map.values()),
                HashBag.newBag(map.values()).toStringOfItemToCount()
                );
    }

    @Override
    @Test
    public void updateValueWith()
    {
        super.updateValueWith();

        ConcurrentUuidIntHashMap map = this.newUuidIntMap();
        ParallelIterate.forEach(Interval.oneTo(100), each -> map.updateValueWith(new UUID(0, each % 10), () -> 0, (integer, parameter) -> {
            Assertions.assertEquals("test", parameter);
            return integer + 1;
        }, "test"), 1, this.executor);
        Assertions.assertEquals(Interval.zeroTo(9).toSet(),
                Sets.mutable.of(map.keySet().stream().map(uuid -> toInt(uuid)).toArray()));
        Assertions.assertEquals(FastList.newList(Collections.nCopies(10, 10)), FastList.newList(map.values()));
    }

    @Override
    @Test
    public void updateValueWith_collisions()
    {
        super.updateValueWith_collisions();

        ConcurrentUuidIntHashMap map = this.newUuidIntMap();
        MutableList<Integer> list = Interval.oneTo(200).toList().shuffleThis();
        ParallelIterate.forEach(list, each -> map.updateValueWith(new UUID(0, each % 100), () -> 0, (integer, parameter) -> {
            Assertions.assertEquals("test", parameter);
            return integer + 1;
        }, "test"), 1, this.executor);
        Assertions.assertEquals(Interval.zeroTo(99).toSet(),
                Sets.mutable.of(map.keySet().stream().map(uuid -> toInt(uuid)).toArray()));
        Assertions.assertEquals(
                FastList.newList(Collections.nCopies(100, 2)),
                FastList.newList(map.values()),
                HashBag.newBag(map.values()).toStringOfItemToCount()
                );
    }
}
