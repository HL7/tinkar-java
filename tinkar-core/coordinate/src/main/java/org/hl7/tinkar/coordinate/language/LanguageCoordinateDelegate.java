/*
 * Copyright 2017 Organizations participating in ISAAC, ISAAC's KOMET, and SOLOR development include the 
         US Veterans Health Administration, OSHERA, and the Health Services Platform Consortium..
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
package org.hl7.tinkar.coordinate.language;

import org.eclipse.collections.api.list.ImmutableList;
import org.hl7.tinkar.common.id.IntIdList;
import org.hl7.tinkar.terms.ConceptFacade;
import org.hl7.tinkar.terms.PatternFacade;

/**
 *
 * @author kec
 */
public interface LanguageCoordinateDelegate extends LanguageCoordinate {
   /**
    * Gets the language coordinate.
    *
    * @return a LanguageCoordinate that specifies how to manage the retrieval and display of language.
    * and dialect information.
    */
   LanguageCoordinate getLanguageCoordinate();

   @Override
   default LanguageCoordinateRecord toLanguageCoordinateRecord() {
      return getLanguageCoordinate().toLanguageCoordinateRecord();
   }

   @Override
   default ImmutableList<ConceptFacade> descriptionTypePreferenceList() {
      return getLanguageCoordinate().descriptionTypePreferenceList();
   }

   @Override
   default ImmutableList<PatternFacade> dialectPatternPreferenceList() {
      return getLanguageCoordinate().dialectPatternPreferenceList();
   }

   @Override
   default IntIdList modulePreferenceNidListForLanguage() {
      return getLanguageCoordinate().modulePreferenceNidListForLanguage();
   }

   @Override
   default ImmutableList<ConceptFacade> modulePreferenceListForLanguage() {
      return getLanguageCoordinate().modulePreferenceListForLanguage();
   }

   @Override
   default ConceptFacade languageConcept() {
      return getLanguageCoordinate().languageConcept();
   }

   @Override
   default IntIdList descriptionTypePreferenceNidList() {
      return getLanguageCoordinate().descriptionTypePreferenceNidList();
   }

   @Override
   default IntIdList dialectPatternPreferenceNidList() {
      return getLanguageCoordinate().dialectPatternPreferenceNidList();
   }

   @Override
   default int languageConceptNid() {
      return getLanguageCoordinate().languageConceptNid();
   }
}
