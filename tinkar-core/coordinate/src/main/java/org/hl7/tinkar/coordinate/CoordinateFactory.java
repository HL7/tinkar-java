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



package org.hl7.tinkar.coordinate;

//~--- JDK imports ------------------------------------------------------------

import java.time.temporal.TemporalAccessor;
import java.util.Collection;

import java.util.EnumSet;
import java.util.List;

//~--- non-JDK imports --------------------------------------------------------
import org.hl7.tinkar.component.Concept;
import org.hl7.tinkar.coordinate.edit.EditCoordinate;
import org.hl7.tinkar.coordinate.language.LanguageCoordinate;
import org.hl7.tinkar.coordinate.logic.LogicCoordinate;
import org.hl7.tinkar.coordinate.stamp.StampFilter;
import org.hl7.tinkar.coordinate.view.ViewCoordinate;
import org.hl7.tinkar.terms.State;

//~--- interfaces -------------------------------------------------------------

/**
 * A factory to obtain {@code StampCoordinate} objects.
 * @author kec
 */
//@Contract
public interface CoordinateFactory
         {
   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return the edits the coordinate
    */
   EditCoordinate createClassifierSolorOverlayEditCoordinate();

   /**
    * active only
    * latest on dev path.
    *
    * @return the taxonomy coordinate
    */
   ViewCoordinate createDefaultInferredView();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return the taxonomy coordinate
    */
   ViewCoordinate createDefaultStatedView();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return the edits the coordinate
    */
   EditCoordinate createDefaultUserMetadataEditCoordinate();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return the edits the coordinate
    */
   EditCoordinate createDefaultUserSolorOverlayEditCoordinate();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return a {@code StampFilter} representing the latest active version on the development path
    * of components in all modules. Inactive versions are not included in computed results.
    */
   StampFilter createDevelopmentLatestActiveOnlyStampFilter();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return a {@code StampCoordinate} representing the latest version on the development path
    * of components in all modules.
    */
   StampFilter createDevelopmentLatestStampFilter();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @param stampFilter the stamp coordinate
    * @param languageCoordinate the language coordinate
    * @param logicCoordinate the logic coordinate
    * @return the taxonomy coordinate
    */
   ViewCoordinate createInferredView(StampFilter stampFilter,
                                     LanguageCoordinate languageCoordinate,
                                     LogicCoordinate logicCoordinate);

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return a {@code StampCoordinate} representing the latest active version on the master path
    * of components in all modules. Inactive versions are not included in computed results.
    */
   StampFilter createMasterLatestActiveOnlyStampFilter();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return a {@code StampCoordinate} representing the latest version on the master path
    * of components in all modules.
    */
   StampFilter createMasterLatestStampFilter();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @param stampPath Specifies the concept that identifies the path.
    * @param moduleSpecificationList List of allowed modules to include in version computations.
    *           An empty list is a wildcard (will include all modules)
    * @param modulePriorityList the module priority list for versions. Used to adjudicate which component to
    * return when more than one version is available. For example, if two modules
    * have versions the same component, which one do you prefer to return? An empty list is a wildcard (no module preference
    * @param allowedStateSet allowed states to be included in version computations based on the returned {@code StampCoordinate}
    * @param dateTimeText the text to parse such as "2007-12-03T10:15:30", which is specified by the ISO-8601 extended offset date-time format.
    * @return a new instance that implements {@code StampCoordinate} with the provided temporal information
    */
   StampFilter createStampFilter(Concept stampPath, List<Concept> moduleSpecificationList, List<Concept> modulePriorityList, EnumSet<State> allowedStateSet, CharSequence dateTimeText);
   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @param stampPath Specifies the concept that identifies the path.
    * @param moduleSpecificationList List of allowed modules to include in version computations.
    *          An empty list is a wildcard (will include all modules)
    * @param modulePriorityList the module priority list for versions. Used to adjudicate which component to
    * return when more than one version is available. For example, if two modules
    * have versions the same component, which one do you prefer to return? An empty list is a wildcard (no module preference)
    * @param allowedStateSet allowed states to be included in version computations based on the returned {@code StampCoordinate}
    * @param temporal the temporal object to specify the time on a path for the returned  {@code StampCoordinate}
    * @return a new instance that implements {@code StampCoordinate} with the provided temporal information
    */
   StampFilter createStampFilter(Concept stampPath,
                                 Collection<Concept> moduleSpecificationList,
                                 List<Concept> modulePriorityList,
                                 EnumSet<State> allowedStateSet,
                                 TemporalAccessor temporal);

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @param stampPath Specifies the concept that identifies the path.
    * @param moduleSpecificationList List of allowed modules to include in version computations.
    *          An empty list is a wildcard (will include all modules)
    * @param modulePriorityList the module priority list for versions. Used to adjudicate which component to
    * return when more than one version is available. For example, if two modules
    * have versions the same component, which one do you prefer to return? An empty list is a wildcard (no module preference)
    * @param allowedStateSet allowed states to be included in version computations based on the returned {@code StampCoordinate}
    * @param year the year to represent, from MIN_YEAR to MAX_YEAR
    * @param month the month-of-year to represent, from 1 (January) to 12 (December)
    * @param dayOfMonth the day-of-month to represent, from 1 to 31
    * @param hour the hour-of-day to represent, from 0 to 23
    * @param minute the minute-of-hour to represent, from 0 to 59
    * @param second the second-of-minute to represent, from 0 to 59
    * @return a new instance that implements {@code StampCoordinate} with the provided temporal information
    */
   StampFilter createStampFilter(Concept stampPath,
                                 Collection<Concept> moduleSpecificationList,
                                 List<Concept> modulePriorityList,
                                 EnumSet<State> allowedStateSet,
                                 int year,
                                 int month,
                                 int dayOfMonth,
                                 int hour,
                                 int minute,
                                 int second);

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @return the standard EL profile logic coordinate.
    */
   LogicCoordinate createStandardElProfileLogicCoordinate();

   /**
    * Creates a new ImmutableCoordinate object.
    *
    * @param stampFilter the stamp coordinate
    * @param languageCoordinate the language coordinate
    * @param logicCoordinate the logic coordinate
    * @return the taxonomy coordinate
    */
   ViewCoordinate createStatedView(StampFilter stampFilter,
                                   LanguageCoordinate languageCoordinate,
                                   LogicCoordinate logicCoordinate);
}

