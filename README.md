# NEXORA
Finanzas personales y de pequeños negocios, 100 % offline. Kotlin · Compose · Material 3 · Room · MVVM.

## Estado
Fase 1 (fundación): proyecto, tema, navegación, Room y dashboard base.

## Compilar (GitHub Actions)
1. Sube el proyecto a GitHub (rama `main`).
2. Pestaña **Actions** → workflow **Build APK** → *Run workflow*.
3. Al terminar, descarga el artefacto `nexora-debug-apk`.

## Release firmado
Nunca subas tu keystore (`*.jks` está en `.gitignore`). La firma se configura en la Fase 7 con secretos de GitHub.
