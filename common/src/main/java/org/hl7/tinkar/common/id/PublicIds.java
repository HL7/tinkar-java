package org.hl7.tinkar.common.util.id;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.common.util.id.impl.*;

import java.util.List;
import java.util.UUID;

public class PublicIds {
    public static final PublicIdListFactory list = PublicIdListFactory.INSTANCE;
    public static final PublicIdSetFactory set = PublicIdSetFactory.INSTANCE;

    public static final PublicId newRandom() {
        return new PublicId1(UUID.randomUUID());
    }

    public static final PublicId of(long msb, long lsb) {
        return new PublicId1(msb, lsb);
    }
    public static final PublicId of(long msb, long lsb, long msb2, long lsb2) {
        return new PublicId2(msb, lsb, msb2, lsb2);
    }
    public static final PublicId of(long msb, long lsb, long msb2, long lsb2, long msb3, long lsb3) {
        return new PublicId3(msb, lsb, msb2, lsb2, msb3, lsb3);
    }
    public static final PublicId of(List<UUID> list) {
        return of(list.toArray(new UUID[list.size()]));
    }
    public static final PublicId of(ImmutableList<UUID> list) {
        return of(list.toArray(new UUID[list.size()]));
    }
    public static final PublicId of(UUID... uuids) {
        if (uuids.length == 1) {
            return new PublicId1(uuids[0]);
        }
        if (uuids.length == 2) {
            return new PublicId2(uuids[0], uuids[1]);
        }
        if (uuids.length == 3) {
            return new PublicId3(uuids[0], uuids[1], uuids[3]);
        }
        return new PublicIdN(uuids);
    }
    public static final PublicId of(long... uuidParts) {
        if (uuidParts.length == 2) {
            return new PublicId1(uuidParts[0], uuidParts[1]);
        }
        if (uuidParts.length == 4) {
            return new PublicId2(uuidParts[0], uuidParts[1], uuidParts[2], uuidParts[3]);
        }
        if (uuidParts.length == 6) {
            return new PublicId3(uuidParts[0], uuidParts[1], uuidParts[2], uuidParts[3], uuidParts[4], uuidParts[5]);
        }
        return new PublicIdN(uuidParts);
    }
}
