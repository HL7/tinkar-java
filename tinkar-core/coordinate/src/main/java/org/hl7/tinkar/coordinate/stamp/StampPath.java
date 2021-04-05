/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributions from 2013-2017 where performed either by US government 
 * employees, or under US Veterans Health Administration contracts. 
 *
 * US Veterans Health Administration contributions by government employees
 * are work of the U.S. Government and are not subject to copyright
 * protection in the United States. Portions contributed by government 
 * employees are USGovWork (17USC §105). Not subject to copyright. 
 * 
 * Contribution by contractors to the US Veterans Health Administration
 * during this period are contractually contributed under the
 * Apache License, Version 2.0.
 *
 * See: https://www.usa.gov/government-works
 * 
 * Contributions prior to 2013:
 *
 * Copyright (C) International Health Terminology Standards Development Organisation.
 * Licensed under the Apache License, Version 2.0.
 *
 */



package org.hl7.tinkar.coordinate.stamp;

//~--- JDK imports ------------------------------------------------------------

import java.util.Arrays;

import org.eclipse.collections.api.set.ImmutableSet;
import org.hl7.tinkar.common.id.PublicId;
import org.hl7.tinkar.entity.Entity;
import org.hl7.tinkar.terms.ConceptFacade;

//~--- interfaces -------------------------------------------------------------

/**
 * The Interface StampPath.
 *
 * @author kec
 */
public interface StampPath
        extends Comparable<StampPath> {
   /**
    * Gets the path concept nid.
    *
    * @return the nid of the concept that defines this STAMP path.
    */
   int pathConceptNid();

   default PublicId pathCoordinateId() {
      return Entity.provider().getEntityFast(pathConceptNid());
   }

   default ConceptFacade pathConcept() {
      return Entity.getFast(pathConceptNid());
   }

   @Override
   default int compareTo(StampPath that) {
      if (this.pathConceptNid() != that.pathConceptNid()) {
         return Integer.compare(this.pathConceptNid(), that.pathConceptNid());
      }
      if (this.getPathOrigins().size() != that.getPathOrigins().size()) {
         return Integer.compare(this.getPathOrigins().size(), that.getPathOrigins().size());
      }
      StampPosition[] thisOrigins = (StampPosition[]) this.getPathOrigins().toArray();
      Arrays.sort(thisOrigins);
      StampPosition[] thatOrigins = (StampPosition[]) that.getPathOrigins().toArray();
      Arrays.sort(thatOrigins);
      return Arrays.compare(thisOrigins, thatOrigins);
   }

   /**
    * Gets the path origins.
    *
    * @return The origins of this path.
    */
   ImmutableSet<StampPositionImmutable> getPathOrigins();

   StampPathImmutable toStampPathImmutable();

   /**
    *
    * @return a StampFilterImmutable representing the latest on this path, with no author constraints.
    */
   default StampFilterRecord getStampFilter() {
      return StampPathImmutable.getStampFilter(this);
   }
}

