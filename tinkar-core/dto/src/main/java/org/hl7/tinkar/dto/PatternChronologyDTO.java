/*
 * Copyright 2020 kec.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hl7.tinkar.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.hl7.tinkar.common.id.PublicId;
import org.hl7.tinkar.component.PatternChronology;
import org.hl7.tinkar.component.PatternVersion;
import org.hl7.tinkar.dto.binary.*;

/**
 * @author kec
 */
@RecordBuilder
public record PatternChronologyDTO(PublicId publicId,
                                   ImmutableList<PatternVersionDTO> patternVersions)
        implements PatternChronology<PatternVersionDTO>, DTO, Marshalable {

    private static final int localMarshalVersion = 3;

    public static PatternChronologyDTO make(PatternChronology<? extends PatternVersion> patternChronology) {
        MutableList<PatternVersionDTO> versions = Lists.mutable.ofInitialCapacity(patternChronology.versions().size());
        for (PatternVersion patternVersion : patternChronology.versions()) {
            versions.add(PatternVersionDTO.make(patternVersion));
        }
        return new PatternChronologyDTO(patternChronology.publicId(),
                versions.toImmutable());
    }

    @Unmarshaler
    public static PatternChronologyDTO make(TinkarInput in) {
        if (localMarshalVersion == in.getTinkerFormatVersion()) {
            PublicId publicId = in.getPublicId();
            return new PatternChronologyDTO(
                    publicId, in.readPatternVersionList(publicId));
        } else {
            throw new UnsupportedOperationException("Unsupported version: " + in.getTinkerFormatVersion());
        }
    }

    @Override
    public ImmutableList<PatternVersionDTO> versions() {
        return patternVersions.collect(patternVersionDTO -> patternVersionDTO);
    }

    @Override
    @Marshaler
    public void marshal(TinkarOutput out) {
        out.putPublicId(publicId());
        out.writePatternVersionList(patternVersions);
    }
}
