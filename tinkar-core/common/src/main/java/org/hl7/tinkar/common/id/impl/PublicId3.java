package org.hl7.tinkar.common.id.impl;

import org.hl7.tinkar.common.id.PublicId;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.LongConsumer;

public class PublicId3 extends PublicIdA {

    protected final long msb;
    protected final long lsb;
    protected final long msb2;
    protected final long lsb2;
    protected final long msb3;
    protected final long lsb3;

    public PublicId3(UUID uuid, UUID uuid2, UUID uuid3) {
        this.msb = uuid.getMostSignificantBits();
        this.lsb = uuid.getLeastSignificantBits();
        this.msb2 = uuid2.getMostSignificantBits();
        this.lsb2 = uuid2.getLeastSignificantBits();
        this.msb3 = uuid3.getMostSignificantBits();
        this.lsb3 = uuid3.getLeastSignificantBits();
    }

    public PublicId3(long msb, long lsb, long msb2, long lsb2, long msb3, long lsb3) {
        this.msb = msb;
        this.lsb = lsb;
        this.msb2 = msb2;
        this.lsb2 = lsb2;
        this.msb3 = msb3;
        this.lsb3 = lsb3;
    }

    @Override
    public int uuidCount() {
        return 3;
    }

    @Override
    public void forEach(LongConsumer consumer) {
        consumer.accept(msb);
        consumer.accept(lsb);
        consumer.accept(msb2);
        consumer.accept(lsb2);
        consumer.accept(msb3);
        consumer.accept(lsb3);
    }

    @Override
    public UUID[] asUuidArray() {
        return new UUID[]{new UUID(msb, lsb), new UUID(msb2, lsb2), new UUID(msb3, lsb3)};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof PublicId publicId) {
            if (o instanceof PublicId1 publicId1) {
                return publicId1.equals(this);
            }
            if (o instanceof PublicId2 publicId2) {
                return publicId2.equals(this);
            }
            if (o instanceof PublicId3 publicId3) {
                return msb == publicId3.msb && lsb == publicId3.lsb ||
                        msb == publicId3.msb2 && lsb == publicId3.lsb2 ||
                        msb == publicId3.msb3 && lsb == publicId3.lsb3 ||

                        msb2 == publicId3.msb && lsb2 == publicId3.lsb ||
                        msb2 == publicId3.msb2 && lsb2 == publicId3.lsb2 ||
                        msb2 == publicId3.msb3 && lsb2 == publicId3.lsb3 ||

                        msb3 == publicId3.msb && lsb3 == publicId3.lsb ||
                        msb3 == publicId3.msb2 && lsb3 == publicId3.lsb2 ||
                        msb3 == publicId3.msb3 && lsb3 == publicId3.lsb3;

            }
            UUID[] thisUuids = asUuidArray();
            return Arrays.stream(publicId.asUuidArray()).anyMatch(uuid -> {
                for (UUID thisUuid : thisUuids) {
                    if (uuid.equals(thisUuid)) {
                        return true;
                    }
                }
                return false;
            });
        }
        return false;
    }


    @Override
    public int hashCode() {
        return Arrays.hashCode(new long[] {msb, lsb, msb2, lsb2, msb3, lsb3});
    }

}