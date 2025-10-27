package br.com.antoniomonteiro.oni.saveeditor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform