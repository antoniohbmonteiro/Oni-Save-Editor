import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm {}

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.materialIconsExtended)
                // JSON (kotlinx-serialization) centralizado no libs.versions.toml
                implementation(libs.kotlinxSerializationJson)
                implementation(libs.kermit)

                // Se você quiser usar coroutines comum em todos os targets:
                // implementation(libs.kotlinx.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
            }
        }
        // Se precisar dependências específicas de JS/WASM, crie os blocos:
        // val jsMain by getting { dependencies { } }
        // val wasmJsMain by getting { dependencies { } }
    }
}

compose.resources{
    publicResClass = true
    generateResClass = always
}

compose.desktop {
    application {
        mainClass = "br.com.antoniomonteiro.oni.saveeditor.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "br.com.antoniomonteiro.oni.saveeditor"
            packageVersion = "1.0.0"
        }
    }
}
