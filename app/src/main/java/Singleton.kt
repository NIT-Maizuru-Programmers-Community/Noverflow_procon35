// Singleton.kt
object Singleton {
    var total: Int = 0
    var remainder: Int = 0
    var quotient: Int = 0
    var moeru: Int = 0
    var pet: Int = 0
    var plastic: Int = 0
    var kan: Int = 0

    fun getInstance(): Singleton {
        return this
    }
}