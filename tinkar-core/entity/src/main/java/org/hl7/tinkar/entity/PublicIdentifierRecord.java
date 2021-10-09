package org.hl7.tinkar.entity;

import org.hl7.tinkar.common.id.PublicId;
import org.hl7.tinkar.common.util.uuid.UuidUtil;

import java.util.UUID;

/**
 * Utility class to simplify writing identifier data to entity records.
 */
public record PublicIdentifierRecord(long mostSignificantBits,
                                     long leastSignificantBits,
                                     long[] additionalUuidLongs) {
    public static PublicIdentifierRecord make(PublicId publicId) {
        UUID[] uuids = publicId.asUuidArray();

        if (uuids.length > 1) {
            UUID[] additionalUuids = new UUID[uuids.length - 1];
            for (int i = 1; i < uuids.length; i++) {
                additionalUuids[i - 1] = uuids[i];
            }
            long[] additionalUuidLongs = UuidUtil.asArray(additionalUuids);
            return new PublicIdentifierRecord(uuids[0].getMostSignificantBits(), uuids[0].getLeastSignificantBits(), additionalUuidLongs);
        }
        return new PublicIdentifierRecord(uuids[0].getMostSignificantBits(), uuids[0].getLeastSignificantBits(), null);
    }
}
