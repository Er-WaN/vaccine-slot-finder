package com.wandev.vaccineslotfinder;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.wandev.vaccineslotfinder");

        noClasses()
            .that()
            .resideInAnyPackage("com.wandev.vaccineslotfinder.service..")
            .or()
            .resideInAnyPackage("com.wandev.vaccineslotfinder.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.wandev.vaccineslotfinder.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
