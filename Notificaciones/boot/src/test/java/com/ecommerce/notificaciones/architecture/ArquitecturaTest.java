package com.ecommerce.notificaciones.architecture;
import com.tngtech.archunit.junit.*;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.core.importer.ImportOption;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
@AnalyzeClasses(packages="com.ecommerce.notificaciones",importOptions=ImportOption.DoNotIncludeTests.class)
class ArquitecturaTest {
 @ArchTest static final ArchRule dominioIndependiente=noClasses().that().resideInAPackage("..domain..")
  .should().dependOnClassesThat().resideInAnyPackage("..application..","..infrastructure..","org.springframework..","jakarta..","lombok..");
 @ArchTest static final ArchRule aplicacionIndependiente=noClasses().that().resideInAPackage("..application..")
  .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..","org.springframework..","jakarta..","lombok..");
}
