package tobyspring.splearn;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

//test 클래스들은 Arch테스트에서 제외
@AnalyzeClasses(packages = "tobyspring.splearn", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {
    @ArchTest
    void HexagonalArchitecture(JavaClasses classes) {
        Architectures.layeredArchitecture()
                .consideringAllDependencies()  //모든 의존 관계에 있어서
                //각 레이어 정의
                .layer("domain").definedBy("tobyspring.splearn.domain..")
                .layer("application").definedBy("tobyspring.splearn.application..")
                .layer("adapter").definedBy("tobyspring.splearn.adapter..")
                //domain 레이어는 application, adapter 레이어에 의해서만 접근 가능
                .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
                //application 레이어는 adapter 레이어에 의해서만 접근 가능
                .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
                //adapter 레이어는 다른 레이어에 의해서 접근 불가
                .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
                .check(classes);
    }
}
